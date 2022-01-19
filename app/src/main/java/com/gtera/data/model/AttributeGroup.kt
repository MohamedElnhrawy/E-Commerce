package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttributeGroup(


    @field:SerializedName("attributes")
    var attributes: List<Attribute>? = null,

    @field:SerializedName("name")
    var name: String? = null
): Serializable


data class Attribute(


    @field:SerializedName("id")
    var id: Int? = 0,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("value")
    var value: String? = null,

    @field:SerializedName("type")
    val type: String? = null



): Serializable
