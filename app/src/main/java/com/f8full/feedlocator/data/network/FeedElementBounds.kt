package com.f8full.feedlocator.data.network

import com.google.gson.annotations.SerializedName

/**
 * Created by F8Full on 2019-01-20. Copyright (c) -- All rights reserved
 */
class FeedElementBounds {

    @SerializedName("min_lat")
    var minLat : Double? = null
    @SerializedName("max_lat")
    var maxLat : Double? = null
    @SerializedName("min_lon")
    var minLon : Double? = null
    @SerializedName("max_lon")
    var maxLon : Double? = null
}