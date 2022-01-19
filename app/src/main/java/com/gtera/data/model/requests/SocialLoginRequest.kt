package com.gtera.data.model.requests

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SocialLoginRequest(

    @field:SerializedName("auth_code")
    var authCode: String? = null,

    @field:SerializedName("access_token")
    var accessToken: String? = null,

    @field:SerializedName("type")
    var type: String? = null

) : Serializable




