package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class New(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("images")
    val image: ArrayList<String>? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("tags")
    val tags: List<String?>? = null,

    @field:SerializedName("share_link")
    val shareLink: String? = null
): Serializable