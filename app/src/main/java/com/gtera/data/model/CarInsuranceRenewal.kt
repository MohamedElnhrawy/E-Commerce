package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CarInsuranceRenewal(

    @field:SerializedName("notes")
    val notes: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("license_front")
    val licenseFront: String? = null,

    @field:SerializedName("chassie_no")
    val chassieNo: String? = null,

    @field:SerializedName("license_back")
    val licenseBack: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("branch_id")
    val branchId: Int? = null,

    @field:SerializedName("branch")
    val branch: Branche? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("status")
    val status: String? = null

) : Serializable
