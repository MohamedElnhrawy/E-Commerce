package com.gtera.data.model.requests

import com.gtera.data.model.Attribute
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddNewCarRequest(

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("images")
    var images: ArrayList<String?>? = null,

    @field:SerializedName("motor_cc")
    var motor_cc: String? = null,

    @field:SerializedName("motor_power_by_horse")
    var motor_power_by_horse: String? = null,

    @field:SerializedName("number_of_engine_gears")
    var number_of_engine_gears: String? = null,

    @field:SerializedName("number_of_casting")
    var number_of_casting: String? = null,

    @field:SerializedName("number_of_doors")
    var number_of_doors: String? = null,

    @field:SerializedName("number_of_air_bags")
    var number_of_air_bags: String? = null,

    @field:SerializedName("number_of_seats")
    var number_of_seats: String? = null,

    @field:SerializedName("fuel_tank_capacity")
    var fuel_tank_capacity: String? = null,

    @field:SerializedName("average_fuel_consumbtion")
    var average_fuel_consumbtion: String? = null,

    @field:SerializedName("dimensions")
    var dimensions: String? = null,

    @field:SerializedName("height_from_ground")
    var height_from_ground: String? = null,

    @field:SerializedName("num_of_cylender")
    var num_of_cylender: String? = null,

    @field:SerializedName("acceleration")
    var acceleration: String? = null,

    @field:SerializedName("max_torque")
    var max_torque: String? = null,

    @field:SerializedName("max_speed")
    var max_speed: String? = null,

    @field:SerializedName("power")
    var power: Boolean? = null,

    @field:SerializedName("electric_injection")
    var electric_injection: Boolean? = null,

    @field:SerializedName("manufacture_year")
    var manufacture_year: Int? = null,

    @field:SerializedName("category_id")
    var category_id: Int? = null,

    @field:SerializedName("body_type_id")
    var body_type_id: Int? = null,

    @field:SerializedName("transmission_type_id")
    var transmission_type_id: Int? = null,

    @field:SerializedName("steering_wheel_type_id")
    var steering_wheel_type_id: Int? = null,

    @field:SerializedName("pull_type_id")
    var pull_type_id: Int? = null,

    @field:SerializedName("colors")
    var colors: ArrayList<Int>? = null,

    @field:SerializedName("brand_id")
    var brand_id: Int? = null,

    @field:SerializedName("model_id")
    var model_id: Int? = null,

    @field:SerializedName("attributes")
    var attributes: ArrayList<Attribute>? = null

) : Serializable

