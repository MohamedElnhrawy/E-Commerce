package com.gtera.data.model.requests

import com.gtera.data.model.response.DataStatus
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FavoritesRequest(
    @field:SerializedName("ids")
    var ids: ArrayList<Int>?= null,

    @field:SerializedName("type")
    var type: String? = null


) : Serializable

data class FavoriteResponse(

    @field:SerializedName("data")
    val data: DataStatus? = null
)