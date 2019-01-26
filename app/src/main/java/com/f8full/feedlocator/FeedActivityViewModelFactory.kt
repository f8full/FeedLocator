package com.f8full.feedlocator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.f8full.feedlocator.data.FeedRepository

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class FeedActivityViewModelFactory(repo: FeedRepository) : ViewModelProvider.NewInstanceFactory() {

    private val repository : FeedRepository = repo

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return FeedActivityViewModel(repository) as T
    }
}