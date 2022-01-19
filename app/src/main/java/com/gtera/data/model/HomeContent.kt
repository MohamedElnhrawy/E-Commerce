package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class HomeContent(

	@field:SerializedName("data")
	val data: Data? = null
)


data class Data(

	@field:SerializedName("categories")
	val categories: List<Category?>? = null,


	@field:SerializedName("welcome")
	val welcome: String? = null
)






