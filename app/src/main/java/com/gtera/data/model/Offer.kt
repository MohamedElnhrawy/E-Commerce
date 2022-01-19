package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class Offer(

    @field:SerializedName("end_date")
    val endDate: String? = null,

    @field:SerializedName("active")
    val active: Boolean? = null,

    @field:SerializedName("discount")
    val discount: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("advance")
    val advance: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("car")
    val car: Car? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("hot_deal")
    val hotDeal: Boolean? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("start_date")
    val startDate: String? = null,

    @field:SerializedName("monthly_payment")
    val monthlyPayment: String? = null,

    @field:SerializedName("carId")
    val carId: Int? = null,

    @field:SerializedName("favourite")
    var isFavorite: Boolean = false,

    @field:SerializedName("discount_type")
    var discountType: String? = null

)