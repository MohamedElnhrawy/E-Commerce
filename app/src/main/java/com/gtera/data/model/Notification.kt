package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class Notification(

	@field:SerializedName("data")
	val data: NotificationData? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("read_at")
	val readAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Any? = null,

	@field:SerializedName("type")
	val type: String? = null
)

data class NotificationData(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("action")
	val action: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
