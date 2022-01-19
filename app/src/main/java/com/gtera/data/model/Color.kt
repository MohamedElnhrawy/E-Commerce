package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Color(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null

) : Serializable


