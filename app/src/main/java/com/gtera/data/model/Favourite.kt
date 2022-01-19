package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class Favourite(

	@field:SerializedName("offer")
	val offer: Offer? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("item_id")
	val itemId: Int? = null,

	@field:SerializedName("car")
	val car: Car? = null,

	@field:SerializedName("item_type")
	val itemType: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
