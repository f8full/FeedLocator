package com.f8full.feedlocator.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
interface BackendAPI {

    @GET("feeds")
    fun feedList(@Query("all") all : Boolean = true, @Query("detailed") detailed : Boolean = true  ):
            Call<FeedListAnswerRoot>
}