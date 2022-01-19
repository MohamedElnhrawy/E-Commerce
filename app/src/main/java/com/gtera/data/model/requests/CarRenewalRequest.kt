package com.gtera.data.model.requests

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CarRenewalRequest(

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("phone_number")
    var phoneNumber: String? = null,

    @field:SerializedName("chassie_no")
    var chassieNo: String? = null,


    @field:SerializedName("license_front")
    var licenseFront: String? = null,


    @field:SerializedName("license_back")
    var licenseBack: String? = null,

    @field:SerializedName("status")
    var status: Int? = null,

    @field:SerializedName("price")
    var price: Double? = null,

    @field:SerializedName("notes")
    var notes: String? = null,

    @field:SerializedName("user_id")
    var userId: Int? = null,

    @field:SerializedName("branch_id")
    var branchId: Int? = null


) : Serializable
