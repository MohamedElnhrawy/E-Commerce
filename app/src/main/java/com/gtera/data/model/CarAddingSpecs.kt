package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class CarAddingSpecs(

	@field:SerializedName("bodyTypes")
	val bodyTypes: List<SpecsItem?>? = null,

	@field:SerializedName("steeringWheelTypes")
	val steeringWheelTypes: List<SpecsItem?>? = null,

	@field:SerializedName("transmissionTypes")
	val transmissionTypes: List<SpecsItem?>? = null,

	@field:SerializedName("pullTypes")
	val pullTypes: List<SpecsItem?>? = null,

	@field:SerializedName("attributes")
	val attributes: List<AttributeGroup?>? = null,

	@field:SerializedName("categories")
	val categories: List<SpecsItem?>? = null,

	@field:SerializedName("colors")
	val colors: List<SpecsItem?>? = null
)

data class SpecsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("value")
	val value: String? = null
)



