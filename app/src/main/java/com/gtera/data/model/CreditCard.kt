package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreditCard(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("amount")
    val amount: String? = null


) : Serializable


