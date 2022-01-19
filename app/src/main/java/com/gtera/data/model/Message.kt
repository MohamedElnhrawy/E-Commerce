package com.gtera.data.model


import com.google.gson.annotations.SerializedName

data class Message(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("messages")
	val messages: List<MessagesItem?>? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class MessagesItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("from_id")
	var fromId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("conversation_id")
	var conversationId: Int? = null,

	@field:SerializedName("to_id")
	var toId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	var id: Int? = null,

	@field:SerializedName("message")
	var message: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("user_id")
	var userId: Int? = null


)

data class ConversationsItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

