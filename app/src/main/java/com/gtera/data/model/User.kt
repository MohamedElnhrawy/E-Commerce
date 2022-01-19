package com.gtera.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.michaelrocks.paranoid.Obfuscate
import java.io.Serializable

@Obfuscate
@Entity(tableName = "users")
class User : Serializable {

    @PrimaryKey
    @SerializedName("email")
    @ColumnInfo(name = "email")
    @NonNull
    var email: String = ""

    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    var lastName: String? = null

    @SerializedName("phone_number")
    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = null

    @SerializedName("profile_picture")
    @ColumnInfo(name = "profile_picture")
    var profilePicture: String? = null

    @SerializedName("birthdate")
    @ColumnInfo(name = "birthdate")
    var birthDate: String? = null

    @Ignore
    @SerializedName("password")
    var password: String? = null

    @Ignore
    @SerializedName("auth_token")
    var accessToken: String? = null

    @ColumnInfo(name = "city_id")
    @SerializedName("city_id")
    var cityId: Int? = null


    @ColumnInfo(name = "city")
    @SerializedName("city")
    var city: City? = null

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    var gender: Int? = null

    @ColumnInfo(name = "governorate_id")
    @SerializedName("governorate_id")
    var governorateId: Int? = null

    @ColumnInfo(name = "governorate")
    @SerializedName("governorate")
    var governorate: Governorate? = null

    @ColumnInfo(name = "addresses")
    @SerializedName("addresses")
    var address: String? = null

    @Ignore
    @SerializedName("timezone")
    var timezone: String? = null
    @Ignore
    @SerializedName("created_at")
    var createdAt: String? = null
    @Ignore
    @SerializedName("locale")
    var locale: String? = null
    @Ignore
    @SerializedName("updated_at")
    var updatedAt: String? = null


    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int? = null

    @ColumnInfo(name = "cars_count")
    @SerializedName("cars_count")
    var carsCount: Int? = null

    @ColumnInfo(name = "notifications_count")
    @SerializedName("notifications_count")
    var notificationsCount: Int? = null

    @ColumnInfo(name = "messages_count")
    @SerializedName("messages_count")
    var messagesCount: Int? = null

    @ColumnInfo(name = "favourites_count")
    @SerializedName("favourites_count")
    var favouritesCount: Int? = null


    @Ignore
    constructor(email: String, password: String?) {
        this.email = email
        this.password = password
    }

    @Ignore
    constructor(email: String, firstName: String?, password: String?) {
        this.email = email
        this.firstName = firstName
        this.password = password
    }

    @Ignore
    constructor(email: String, firstName: String?, lastName: String?, password: String?) {
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.password = password
    }

    constructor()


    @Ignore
    constructor(
        email: String,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        address: String?,
        birthDate: String?,
        gender: Int?
    ) {
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.phoneNumber = phoneNumber
        this.profilePicture = profilePicture
        this.birthDate = birthDate
        this.gender = gender
        this.address = address
    }


    @Ignore
    constructor(
        email: String,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        profilePicture: String?,
        birthDate: String?,
        cityId: Int?,
        gender: Int?,
        governorateId: Int
    ) {
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.phoneNumber = phoneNumber
        this.profilePicture = profilePicture
        this.birthDate = birthDate
        this.cityId = cityId
        this.gender = gender
        this.governorateId = governorateId
    }

    constructor(
        email: String,
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        profilePicture: String?,
        birthDate: String?,
        password: String?,
        accessToken: String?,
        cityId: Int?,
        city: City?,
        gender: Int?,
        governorateId: Int?,
        governorate: Governorate?,
        address: String?,
        timezone: String?,
        createdAt: String?,
        locale: String?,
        updatedAt: String?,
        id: Int?,
        carsCount: Int?,
        notificationsCount: Int?,
        messagesCount: Int?,
        favouritesCount: Int?
    ) {
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.phoneNumber = phoneNumber
        this.profilePicture = profilePicture
        this.birthDate = birthDate
        this.password = password
        this.accessToken = accessToken
        this.cityId = cityId
        this.city = city
        this.gender = gender
        this.governorateId = governorateId
        this.governorate = governorate
        this.address = address
        this.timezone = timezone
        this.createdAt = createdAt
        this.locale = locale
        this.updatedAt = updatedAt
        this.id = id
        this.carsCount = carsCount
        this.notificationsCount = notificationsCount
        this.messagesCount = messagesCount
        this.favouritesCount = favouritesCount
    }


    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return other.email == email &&
                    other.firstName == firstName &&
                    other.phoneNumber == phoneNumber
        }
        return false
    }
}