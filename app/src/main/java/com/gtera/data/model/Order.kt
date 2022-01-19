package com.gtera.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(

	@field:SerializedName("bankAccount")
	val bankAccount: String? = null,

	@field:SerializedName("bank_account_id")
	val bankAccountId: Int? = null,

	@field:SerializedName("delivery_place")
	val deliveryPlace: String? = null,

	@field:SerializedName("color")
	val color: Color? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("power_of_attorney_image")
	val powerOfAttorneyImage: List<String?>? = null,

	@field:SerializedName("power_of_attorney_names")
	val powerOfAttorneyNames: List<String?>? = null,

	@field:SerializedName("branch")
	val branch: Branche? = null,

	@field:SerializedName("payment_type")
	val paymentType: Int? = null,

	@field:SerializedName("color_id")
	val colorId: Int? = null,

	@field:SerializedName("insurance_amount")
	val insuranceAmount: Double? = null,

	@field:SerializedName("need_car_insurance")
	val needCarInsurance: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("total_amount")
	val totalAmount: Double? = null,

	@field:SerializedName("need_car_license")
	val needCarLicense: Boolean? = null,

	@field:SerializedName("branch_id")
	val branchId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("car")
	val car: Car? = null,

	@field:SerializedName("advance_payment")
	val advancePayment: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("car_id")
	val carId: Int? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("notes")
	var notes: String? = null

) : Serializable


