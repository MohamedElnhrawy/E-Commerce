package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SteeringWheelType(

	@field:SerializedName("cars")
	val cars: List<Car?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
): Serializable
