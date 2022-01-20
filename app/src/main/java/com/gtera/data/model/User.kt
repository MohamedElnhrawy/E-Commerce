package com.gtera.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.michaelrocks.paranoid.Obfuscate
import java.io.Serializable

//@Obfuscate
@Entity(tableName = "users")
class User : Serializable {

    @SerializedName("email")
    @ColumnInfo(name = "email")
    var email: String? = null

    @SerializedName("display_name")
    @ColumnInfo(name = "display_name")
    var displayName: String? = null


    @SerializedName("phone_number")
    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = null

    @PrimaryKey
    @SerializedName("auth_token")
    @ColumnInfo(name = "auth_token")
    @NonNull
    var accessToken: String = ""



    @Ignore
    constructor()

    constructor(
        email: String?,
        displayName: String?,
        phoneNumber: String?,
        accessToken: String

    ) {
        this.email = email
        this.displayName = displayName
        this.phoneNumber = phoneNumber
        this.accessToken = accessToken


    }


    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return other.accessToken == accessToken
        }
        return false
    }

    fun getWelcomedName():String{
        return when {
            displayName != null -> displayName!!
            email != null -> email!!
            phoneNumber != null -> phoneNumber!!
            else -> "Guest"
        }
    }
}