package com.gtera.data.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import java.io.Serializable

@SuppressLint("ParcelCreator")
@Parcelize
data class JobTitle(
    @SerializedName("bank_benfit_rate")
    val bankBenfitRate: Double,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("max_installment_years")
    val maxInstallmentYears: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable,Serializable{

}



@SuppressLint("ParcelCreator")
@Parcelize
data class Name(
    @SerializedName("en")
    val en: String,
    @SerializedName("ar")
    val ar: String
) : Parcelable,Serializable{

}