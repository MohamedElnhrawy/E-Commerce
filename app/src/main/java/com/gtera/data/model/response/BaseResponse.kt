package com.gtera.data.model.response

import com.gtera.data.model.MetaData
import com.google.gson.annotations.SerializedName
import java.io.Serializable


open class BaseResponse<T>(

    @field:SerializedName("data")
    var data: T? = null,
    @field:SerializedName("meta")
    val meta: MetaData? = null,
    @field:SerializedName("message")
    val message: String? = null

) : Serializable
