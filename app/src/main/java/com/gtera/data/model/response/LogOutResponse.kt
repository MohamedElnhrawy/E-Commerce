package com.gtera.data.model.response

import com.google.gson.annotations.SerializedName

data class LogOutResponse(

	@field:SerializedName("data")
	val data: DataStatus? = null
)


