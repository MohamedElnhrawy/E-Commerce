package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class Slider(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("item_id")
    val itemId: Int? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("item_type")
    val itemType: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null
)