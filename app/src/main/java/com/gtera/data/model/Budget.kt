package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Budget(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("from")
    val from: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("to")
    val to: String? = null,

    @field:SerializedName("brands")
    val brands: ArrayList<Brand>? = null
) : Serializable
