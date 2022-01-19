package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Model(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("brand_id")
	val brandId: Int? = null,

	@field:SerializedName("share_link")
	val shareLink: String? = null
) : Serializable
