package com.f8full.feedlocator

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
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
import android.content.res.TypedArray
import android.graphics.Color
import android.view.View


/**
 * Created by F8Full on 2019-01-19. Copyright (c) -- All rights reserved
 */
class FeedsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

    //country code per precomputed tinted asset
    private var descriptorMap: HashMap<String, BitmapDescriptor> = HashMap()

    private val model:FeedActivityViewModel
    get() {
        //TODO: FeedActivityViewModelFactory provokes download (repo injection)
        val modelFactory = InjectorUtils.provideFeedActivityViewModelFactory()
        return ViewModelProviders.of(this, modelFactory).get(FeedActivityViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeds)

        //launch bitmapDescriptors building
        //TODO: Observe on readyness and get map fragment and get map
        coroutineScopeIO.launch {

            //First let's map RGB colors from resources to country code
            // from https://stackoverflow.com/a/40137782/8958408
            val countriesArrayOfArrays = resources.obtainTypedArray(R.array.countries_array_of_arrays)
            val size = countriesArrayOfArrays.length()
            val colorMap = HashMap<String, Int>(size) //color Int per country code, all from resource files
            var countryTypedArray: TypedArray? = null
            for (i in 0 until size) {
                val id = countriesArrayOfArrays.getResourceId(i, -1)
                if (id == -1) {
                    throw IllegalStateException("R.array.countries_array_of_arrays is not valid")
                }
                countryTypedArray = resources.obtainTypedArray(id)

                colorMap[countryTypedArray!!.getString(0)!!] =
                        countryTypedArray.getColor(1, Color.MAGENTA)

            }
            countryTypedArray?.recycle()
            countriesArrayOfArrays.recycle()

            //Now we have tint color map, let's build BitmapDescriptor map
            //Could be optimized by doing it directly when reading resources.
            MapsInitializer.initialize(this@FeedsActivity)

            val basePinBitmap = BitmapFactory.decodeStream(assets.open("pin.png"))
            val btdrawable = BitmapDrawable(resources, basePinBitmap)
            btdrawable.setBounds(0,0,btdrawable.intrinsicWidth, btdrawable.intrinsicHeight)

            val bm = Bitmap.createBitmap(btdrawable.intrinsicWidth, btdrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bm)

            //for each country code, grab color and tint drawable
            colorMap.forEach {
                btdrawable.setTint(it.value)
                btdrawable.draw(canvas)

                //save result for later use
                descriptorMap[it.key] = BitmapDescriptorFactory.fromBitmap(bm)
            }

            //We're done creating assets, let's signal
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

                val options = MarkerOptions()
                    .icon(descriptorMap["__"]) //other country 'country code'
                    .position(LatLngBounds(LatLng(
                        it.bounds!!.minLat!!,
                        it.bounds!!.minLon!!),
                        LatLng(it.bounds!!.maxLat!!,
                            it.bounds!!.maxLon!!)
                    ).center)
                    .title("Name:${it.name}").snippet("City:${it.location}")

                if (descriptorMap.containsKey(it.countryCode)){
                    options.icon(descriptorMap[it.countryCode])
                }
                mMap.addMarker(options)
            }

            findViewById<View>(R.id.progressBar).visibility = View.GONE
        })
    }
}
