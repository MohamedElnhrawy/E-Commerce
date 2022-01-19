package com.gtera.data.model

import com.google.gson.annotations.SerializedName

data class TopDeal(

    @field:SerializedName("end_date")
    val endDate: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("hot_deal")
    val hotDeal: Boolean? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("active")
    val active: Boolean? = null,

    @field:SerializedName("discount")
    val discount: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("car")
    val car: Car? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("start_date")
    val startDate: String? = null,

    @field:SerializedName("advance")
    val advance: String? = null,

    @field:SerializedName("monthly_payment")
    val monthlyPayment: String? = null,

    @field:SerializedName("carId")
    val carId: Int? = null,

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("images")
    val images: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("tags")
    val tags: List<String?>? = null,

    @field:SerializedName("favourite")
    var isFavorite: Boolean = false


)