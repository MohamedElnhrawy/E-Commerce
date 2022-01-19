package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class OrderNowViewData(

	@field:SerializedName("attorneyPerson")
	val attorneyPersons: List<AttorneyPersonItem?>? = null,

	@field:SerializedName("branch")
	val branches: List<BranchItem?>? = null
)

data class BranchItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("value")
	val value: Int? = null
)

data class AttorneyPersonItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("value")
	val value: Int? = null
)
