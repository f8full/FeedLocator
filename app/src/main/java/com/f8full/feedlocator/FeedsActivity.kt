package com.f8full.feedlocator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.f8full.feedlocator.utils.InjectorUtils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by F8Full on 2019-01-19. Copyright (c) -- All rights reserved
 */
class FeedsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeds)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        val modelFactory = InjectorUtils.provideFeedActivityViewModelFactory()

        val model = ViewModelProviders.of(this, modelFactory).get(FeedActivityViewModel::class.java)

        model.getFeedList.observe(this, Observer { feedEntryList ->
            Log.d("FeedsActivity", "new data in model : ${feedEntryList.size}")

            //TODO: rebuild map pins
            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        })
    }
}
