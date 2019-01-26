package com.f8full.feedlocator

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.f8full.feedlocator.data.FeedEntry
import com.f8full.feedlocator.data.FeedRepository

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class FeedActivityViewModel(repo: FeedRepository) : ViewModel() {

    private val feedRepository : FeedRepository = repo
    private val currentFeedList : LiveData<List<FeedEntry>>?

    init {
        currentFeedList = repo.getCurrentFeedList()
    }
}