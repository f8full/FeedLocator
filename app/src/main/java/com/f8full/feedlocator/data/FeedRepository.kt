package com.f8full.feedlocator.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.f8full.feedlocator.data.network.FeedEntry
import com.f8full.feedlocator.data.network.TransitFeedNetworkDataSource

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class FeedRepository private constructor(private val transitFeedNetworkDataSource: TransitFeedNetworkDataSource){

    private val latestFeedList: MutableLiveData<List<FeedEntry>> = MutableLiveData()

    init {

        val networkData = this.transitFeedNetworkDataSource.currentTransitFeedList

        networkData.observeForever {
            Log.d(LOG_TAG, "network data list size : ${it.size}")

            //TODO: here is where we'd update Room database through use of DAO
            //here we simply cache in memory
            latestFeedList.value = it
        }

        //TODO: use service in place of direct call
        this.transitFeedNetworkDataSource.fetchFeedList()
    }

    val getLatestFeedList: LiveData<List<FeedEntry>>
    get() {return latestFeedList} //TODO: with Room, returned value is LiveData exposed by DAO

    companion object {
        private val LOG_TAG = FeedRepository::class.java.simpleName

        // For Singleton instantiation
        private val LOCK = Any()
        private var sInstance: FeedRepository? = null

        /**
         * Get the singleton for this class
         */
        fun getInstance(transitFeedNetworkDataSource: TransitFeedNetworkDataSource): FeedRepository {
            Log.d(LOG_TAG, "Getting the network data source")
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = FeedRepository(transitFeedNetworkDataSource)
                    Log.d(LOG_TAG, "Made new network data source")
                }
            }
            return sInstance!!
        }
    }
}