package com.gtera.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import io.michaelrocks.paranoid.Obfuscate
import java.io.Serializable

//@Obfuscate
@Entity(tableName = "cart")
class CartProduct  : Serializable {

    @PrimaryKey
    @field:SerializedName("id")
    var id: Int? = null

    @field:SerializedName("name")
    var name: String? = null

    @field:SerializedName("description")
    var description: String? = null

    @field:SerializedName("image")
    var image: String? = null

    @field:SerializedName("price")
    var price: Double? = null

    @SerializedName(value = "quantity")
     var cartQuantity = 0

    @SerializedName("total_price")
     var totalPrice: Float? = null

    constructor(
        id:Int?,
        name: String?,
        description: String?,
        image: String?,
        price: Double?,
        cartQuantity: Int,
        totalPrice: Float?
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.image = image
        this.price = price
        this.cartQuantity = cartQuantity
        this.totalPrice = totalPrice
    }


    override fun equals(other: Any?): Boolean {
        if (other is CartProduct) {
            return other.id == id
        }
        return false
    }
}