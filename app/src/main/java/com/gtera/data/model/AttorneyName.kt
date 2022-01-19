package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class AttorneyName(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("branch_id")
	val branchId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
