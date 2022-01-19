package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("items")
    var products: List<Product?>? = null

) : Serializable

