package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FiltrationOption(

    @field:SerializedName("name")
    var name: String? = null,
    @field:SerializedName("id")
    var id: Int,
    @field:SerializedName("value")
    var value: String? = null,
    @field:SerializedName("attributes")
    var attributes: ArrayList<Attribute>? = null



):Serializable