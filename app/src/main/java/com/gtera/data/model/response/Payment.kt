package com.gtera.data.model.response

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("data")
    val data: Info?
)

data class Info(
    @SerializedName("url")
    val url:String?
)