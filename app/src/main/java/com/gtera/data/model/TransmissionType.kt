package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TransmissionType(

	@field:SerializedName("cars")
	val cars: List<Any?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null


): Serializable


