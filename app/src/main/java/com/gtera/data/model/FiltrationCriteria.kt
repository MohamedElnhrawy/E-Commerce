package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FiltrationCriteria(

    @field:SerializedName("name")
    var name: String? = null,
    @field:SerializedName("key")
    var key: String,
    @field:SerializedName("type")
    var viewType: String = "",
    @field:SerializedName("items")
    var items: List<FiltrationOption>?

) : Serializable