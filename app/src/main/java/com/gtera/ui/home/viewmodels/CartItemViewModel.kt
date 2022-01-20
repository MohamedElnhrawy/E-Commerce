package com.gtera.ui.home.viewmodels

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.CartProduct
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.ListOrientation
import javax.inject.Inject

class CartItemViewModel @Inject constructor() : BaseItemViewModel() {


    var totalCoast = ObservableField<String>("")
    var price = ObservableField<String>("")
    var orientation: ListOrientation? = null
    var cartProduct: CartProduct? = null
    var resourceProvider: ResourceProvider? = null

    override val layoutId: Int
        get() = if (orientation?.equals(ListOrientation.ORIENTATION_HORIZONTAL)!!)
            R.layout.cart_item_horizontal_layout
        else R.layout.cart_item_vertical_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }




    constructor(
        cartProduct: CartProduct,
        resourceProvider: ResourceProvider,
        orientation: ListOrientation
    ) : this() {
       this.resourceProvider = resourceProvider
        this.orientation = orientation
        this.cartProduct =  cartProduct
        setup()
    }


    fun setup() {
        price.set(resourceProvider?.getString(R.string.str_price) + " : "  + resourceProvider?.getString(
            R.string.str_egp,
            cartProduct?.price?.toString()
        ))
        totalCoast.set(resourceProvider?.getString(R.string.str_total_cost) + " : " + resourceProvider?.getString(R.string.str_egp,(cartProduct?.cartQuantity)?.times(cartProduct?.price!!).toString()))

    }

}