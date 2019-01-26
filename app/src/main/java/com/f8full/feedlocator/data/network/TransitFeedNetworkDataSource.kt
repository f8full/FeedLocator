package com.f8full.feedlocator.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
/**
 *
 * Provides an API for doing all operations with the server data
 */
class TransitFeedNetworkDataSource private constructor() {

    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

    // LiveData storing the latest downloaded weather forecasts
    private val downloadedFeedList: MutableLiveData<List<FeedListAnswerFeedElement>>

    val currentTransitFeedList: LiveData<List<FeedListAnswerFeedElement>>
        get() = downloadedFeedList

    init {
        downloadedFeedList = MutableLiveData<List<FeedListAnswerFeedElement>>()
    }

    /**
     * Schedules a repeating job service which fetches the feed list.
     */
    fun scheduleRecurringFetchFeedListSync() {
        //TODO: Use WorkManager to schedule recurring data refresh
        //https://developer.android.com/topic/libraries/architecture/workmanager/
        //with conditions like being connected to the network and not more than once per 24h (server refresh interval)
        //Repository calls this method
    }

    /**
     * Gets the newest feed list
     */
    internal fun fetchFeedList() {
        Log.d(LOG_TAG, "Fetch feeds started")

        //Network operation on backgroud thread. Posts to model
        coroutineScopeIO.launch {
            try {

                val backendAnswer = RetrofitClient.instance.backendAPI.feedList().execute().body()

                if (backendAnswer?.feeds != null) {
                    Log.d(
                        LOG_TAG, "JSON not null and has " + backendAnswer.feeds!!.size
                                + " values"
                    )

                    // When you are off of the main thread and want to update LiveData, use postValue.
                    // It posts the update to the main thread.
                    downloadedFeedList.postValue(backendAnswer.feeds)

                }
                else{
                    Log.e(LOG_TAG,"Some network error")

                }
            } catch (e: Exception) {
                // Something went wrong, surface error in app UX
                e.printStackTrace()
            }
        }
    }

    companion object {
        private val LOG_TAG = TransitFeedNetworkDataSource::class.java.simpleName

        // For Singleton instantiation
        private val LOCK = Any()
        private var sInstance: TransitFeedNetworkDataSource? = null

        /**
         * Get the singleton for this class
         */
        fun getInstance(): TransitFeedNetworkDataSource {
            Log.d(LOG_TAG, "Getting the network data source")
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = TransitFeedNetworkDataSource()
                    Log.d(LOG_TAG, "Made new network data source")
                }
            }
            return sInstance!!
        }
    }

}