package com.f8full.feedlocator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.f8full.feedlocator.data.FeedRepository
import com.f8full.feedlocator.data.network.FeedEntry

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class FeedActivityViewModel(repo: FeedRepository) : ViewModel() {

    private val currentFeedList : LiveData<List<FeedEntry>> = repo.getLatestFeedList
    private val mapAssetsReady = MutableLiveData<Boolean>()

    val isMapAssetsReady: LiveData<Boolean>
        get() = mapAssetsReady

    fun postMapAssetsReady(toPost:Boolean){
        mapAssetsReady.postValue(toPost)
    }

    val getFeedList: LiveData<List<FeedEntry>>
    get() {return currentFeedList}
}