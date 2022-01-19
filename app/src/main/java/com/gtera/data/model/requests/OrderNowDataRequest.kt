package com.gtera.data.model.requests

import com.google.gson.annotations.SerializedName

data class OrderNowDataRequest(

	@field:SerializedName("models")
	val models: List<String?>? = null
)
