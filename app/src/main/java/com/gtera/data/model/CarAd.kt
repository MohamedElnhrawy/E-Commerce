package com.gtera.data.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CarAd(
    @SerializedName("active")
    val active: Boolean,

    @SerializedName("car_id")
    val carId: Int,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("expiration_date")
    val expirationDate: String,

    @SerializedName("governorate_id")
    val governorateId: Int,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("initiation_date")
    val initiationDate: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("show_email")
    val showEmail: Boolean,
    @SerializedName("show_mobile")
    val showMobile: Boolean,
    @SerializedName("status")
    val status: String,
    @SerializedName("status_note")
    val statusNote: String,
    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("viewing_days")
    val viewingDays: List<Int>,
    @SerializedName("viewing_times")
    val viewingTimes: List<String>
) : Parcelable