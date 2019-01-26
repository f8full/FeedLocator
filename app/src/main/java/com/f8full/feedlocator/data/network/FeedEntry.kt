package com.f8full.feedlocator.data.network

import com.google.gson.annotations.SerializedName

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class FeedEntry {

    var id: Int? = null
    var name: String? = null
    var location: String? = null
    var bounds : FeedElementBounds? = null
    @SerializedName("country_code")
    var countryCode : String? = null
}