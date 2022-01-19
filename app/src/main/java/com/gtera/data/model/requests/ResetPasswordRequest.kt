package com.gtera.data.model.requests

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResetPasswordRequest(

    @field:SerializedName("email")
    val email: String? = null



):Serializable

