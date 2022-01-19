package com.gtera.data.model.requests

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderNowRequest(

	@field:SerializedName("car_id")
	var carId: Int? = null,

	@field:SerializedName("color_id")
	var colorId: Int? = null,

	@field:SerializedName("branch_id")
	var branch_id: Int? = null,

	@field:SerializedName("delivery_place")
	var deliveryPlace: String? = null,

	@field:SerializedName("payment_type")
	var paymentType: Int? = null,

	@field:SerializedName("advance_payment")
	var advancePayment: Double? = null,

	@field:SerializedName("need_car_license")
	var needCarLicense: Boolean? = null,

	@field:SerializedName("power_of_attorney_image")
	var powerOfAttorneyImages: List<String?>? = null,

	@field:SerializedName("attorneyPersons")
	var attorneyPersons: List<String?>? = null,

	@field:SerializedName("need_car_insurance")
	var needCarInsurance: Boolean? = null

) : Serializable





