package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("description")
    var description: String? = null,
    @field:SerializedName("image")
    var image: String? = null,

    @field:SerializedName("stock_qty")
    var stock_qty: Long? = null,

    @field:SerializedName("price")
    var price: Double? = null


) : Serializable

