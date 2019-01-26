package com.f8full.feedlocator.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.f8full.feedlocator.data.network.TransitFeedNetworkDataSource

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class FeedRepository private constructor(transitFeedNetworkDataSource: TransitFeedNetworkDataSource){

    private val transitFeedNetworkDataSource : TransitFeedNetworkDataSource

    init {

        this.transitFeedNetworkDataSource = transitFeedNetworkDataSource

        val networkData = this.transitFeedNetworkDataSource.currentTransitFeedList

        networkData.observeForever {
            Log.d(LOG_TAG, "network data list size : ${it.size}")
        }

        //TODO: use service in place of direct call
        this.transitFeedNetworkDataSource.fetchFeedList()
    }

    fun getCurrentFeedList(): LiveData<List<FeedEntry>>? {
        return null
    }

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