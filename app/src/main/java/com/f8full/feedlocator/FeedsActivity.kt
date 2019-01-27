package com.f8full.feedlocator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.f8full.feedlocator.utils.InjectorUtils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by F8Full on 2019-01-19. Copyright (c) -- All rights reserved
 */
class FeedsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

    private var descriptorMap: HashMap<Int, BitmapDescriptor> = HashMap()

    private val model:FeedActivityViewModel
    get() {
        //TODO: FeedActivityViewModelFactory provokes download (repo injection)
        val modelFactory = InjectorUtils.provideFeedActivityViewModelFactory()
        return ViewModelProviders.of(this, modelFactory).get(FeedActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeds)

        //launch bitmapDescriptors building
        //TODO: Observe on readyness and get map fragment and get map
        coroutineScopeIO.launch {
            MapsInitializer.initialize(this@FeedsActivity)
            //Create required descriptors from
            descriptorMap[666] = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)

            model.postMapAssetsReady(true)
        }

        model.isMapAssetsReady.observe(this, Observer { mapAssetsReady ->
            if (mapAssetsReady){
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        model.getFeedList.observe(this, Observer { feedEntryList ->
            Log.d("FeedsActivity", "new data in model : ${feedEntryList.size}")

            feedEntryList.forEach {
                mMap.addMarker(MarkerOptions()
                    .icon(descriptorMap[666])
                    .position(LatLngBounds(LatLng(
                        it.bounds!!.minLat!!,
                        it.bounds!!.minLon!!),
                        LatLng(it.bounds!!.maxLat!!,
                            it.bounds!!.maxLon!!)
                ).center)
                    .title("Name:${it.name}").snippet("City:${it.location}"))
            }
            //TODO: rebuild map pins
            // Add a marker in Sydney and move the camera
            //val sydney = LatLng(-34.0, 151.0)

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        })
    }
}
