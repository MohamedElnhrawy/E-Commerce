package com.gtera.data.model.response

import com.google.gson.annotations.SerializedName

data class DeleteCompareResponse(

	@field:SerializedName("data")
	val data: DataStatus? = null
)

data class DataStatus(

	@field:SerializedName("status")
	val status: String? = null
)


