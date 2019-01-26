package com.f8full.feedlocator.utils

import com.f8full.feedlocator.FeedActivityViewModelFactory
import com.f8full.feedlocator.data.FeedRepository
import com.f8full.feedlocator.data.network.TransitFeedNetworkDataSource

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class InjectorUtils {

    companion object {
        fun provideRepository() : FeedRepository{
            val networkDataSource = TransitFeedNetworkDataSource.getInstance()

            return FeedRepository.getInstance(networkDataSource)
        }

        fun provideFeedActivityViewModelFactory() : FeedActivityViewModelFactory{
            val repo = provideRepository()
            return FeedActivityViewModelFactory(repo)
        }
    }
}