package com.gtera.data.model.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
	val data: Data? = null
)

data class Data(
	val status: Status? = null
)

data class Status(
	@field:SerializedName("access_token")
	val accessToken: String? = null,
	@field:SerializedName("refresh_token")
	val refreshToken: String? = null,
	@field:SerializedName("expires_in")
	val expiresIn: Int? = null
)

