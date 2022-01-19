package com.gtera.data.model

import androidx.annotation.NonNull
import androidx.room.TypeConverters
import com.gtera.data.local.db.converter.Converter
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@Entity
data class Car(


//    @PrimaryKey
//    @ColumnInfo(name = "id")
    @NonNull
    @field:SerializedName("id")
    val id: Int? = null,
    @TypeConverters(Converter::class)
    @field:SerializedName("images")
    val images: ArrayList<String?>? = null,

    @field:SerializedName("price_from")
    val priceFrom: String? = null,

    @field:SerializedName("video_url")
    val videoUrl: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("price_to")
    val priceTo: String? = null,

    @field:SerializedName("active")
    val active: Boolean? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("used")
    val used: Boolean? = null,

    @field:SerializedName("led_electric_side_mirrors_signals")
    val ledElectricSideMirrorsSignals: String? = null,

    @field:SerializedName("number_of_engine_gears")
    val numberOfEngineGears: String? = null,

    @field:SerializedName("pull_type_id")
    val pullTypeId: Int? = null,


    @field:SerializedName("body_type_id")
    val bodyTypeId: Int? = null,

    @field:SerializedName("fuel_tank_capacity")
    val fuelTankCapacity: String? = null,


    @field:SerializedName("acceleration")
    val acceleration: String? = null,

    @field:SerializedName("motor_cc")
    val motorCc: String? = null,

    @field:SerializedName("mile_age")
    val mileage: String? = null,

    @field:SerializedName("license")
    val license: String? = null,

    @field:SerializedName("number_of_seats")
    val numberOfSeats: String? = null,

    @field:SerializedName("number_of_doors")
    val numberOfDoors: String? = null,

    @field:SerializedName("num_of_cylender")
    val numOfCylender: String? = null,


    @field:SerializedName("model_id")
    val modelId: Int? = null,

    @field:SerializedName("brand_id")
    val brandId: Int? = null,

    @field:SerializedName("max_torque")
    val maxTorque: String? = null,

    @field:SerializedName("height_from_ground")
    val heightFromGround: String? = null,

    @field:SerializedName("catalogue_id")
    val catalogueId: Int? = null,

    @field:SerializedName("transmission_type_id")
    val transmissionTypeId: Int? = null,

    @field:SerializedName("average_fuel_consumbtion")
    val averageFuelConsumbtion: String? = null,


    @field:SerializedName("steering_wheel_type_id")
    val steeringWheelTypeId: Int? = null,

    @field:SerializedName("motor_power_by_hourse")
    val motorPowerByHourse: String? = null,

    @field:SerializedName("number_of_casting")
    val numberOfCasting: String? = null,

    @field:SerializedName("dimensions")
    val dimensions: String? = null,
    @field:SerializedName("motorPowerByHorse")
    val motorPowerByHorse: String? = null,
    @TypeConverters(Converter::class)
    @field:SerializedName("bodyType")
    val bodyType: BodyType ?= null,

    @field:SerializedName("steeringWheelType")
    val steeringWheelType: SteeringWheelType? = null,

    @TypeConverters(Converter::class)
    @field:SerializedName("colors")
    val colors: List<Color?>? = null,


    @field:SerializedName("model")
    val model: Model? = null,

    @field:SerializedName("transmissionType")
    val transmissionType: TransmissionType? = null,

    @TypeConverters(Converter::class)
    @field:SerializedName("brand")
    val brand: Brand? = null,

    @TypeConverters(Converter::class)
    @field:SerializedName("catalogue")
    val catalogue: Catalogue? = null,


    @field:SerializedName("pullType")
    val pullType: PullType? = null,
    @TypeConverters(Converter::class)
    @field:SerializedName("attributeGroups")
    val attributes: List<AttributeGroup>? = null,



    @field:SerializedName("category_id")
    val categoryId: Any? = null,


    @field:SerializedName("electric_injection")
    val electricInjection: Boolean? = null,

    @field:SerializedName("power")
    val power: Boolean? = null,


    @field:SerializedName("monthly_payment")
    val monthlyPayment: String? = null,

    @field:SerializedName("share_link")
    val shareLink: String? = "",


    @field:SerializedName("manufacture_year")
    val manufactureYear: String? = null,

    @field:SerializedName("calculate_it_your_self_price")
    val calculateItYourSelfPrice: String? = null,


    @field:SerializedName("new_year_price")
    val newYearPrice: String? = null,

    @field:SerializedName("user")
    val user: User? = null,

    @field:SerializedName("CarAd")
    val carAd: CarAd? = null,

    var isCheckedForCompare: Boolean,

    @field:SerializedName("favourite")
    var isFavorite: Boolean = false,

    @field:SerializedName("description")
    var description: String

) : Serializable