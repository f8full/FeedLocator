package com.f8full.feedlocator.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class RetrofitClient private constructor() {

    var backendAPI : BackendAPI


    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        backendAPI = retrofit.create(BackendAPI::class.java)

    }

    companion object {

        private var mInstance: RetrofitClient? = null
        private const val BASE_URL = "https://api.transitapp.com/v3/"

        val instance: RetrofitClient
            get() {
                if (mInstance == null){
                    mInstance = RetrofitClient()
                }

                return mInstance!!
            }
    }
}