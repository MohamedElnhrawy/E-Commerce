package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class FAQ(

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("answer")
	val answer: String? = null
)
