package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Brand(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("brand_id")
    val brandId: Int? = null,

    @field:SerializedName("image")
    var image: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("active")
    val active: Boolean? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null

) : Serializable

