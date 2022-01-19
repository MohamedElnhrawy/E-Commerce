package com.gtera.data.model.requests

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RefreshTokenRequest(

    @field:SerializedName("refresh_token")
    val refreshToken: String? = null



): Serializable