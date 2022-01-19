package com.gtera.ui.product_details

import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.CartProduct
import com.gtera.utils.APPConstants
import com.gtera.data.model.Product
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject


class ProductDetailsViewModel @Inject constructor() : BaseViewModel<ProductDetailsNavigator>() {

    var productQty = ObservableInt(1)
    var totalPrice = ObservableField("")
    var productPrice = ObservableField("")
     var product=  ObservableField<Product>()
    override fun onViewCreated() {
        product.set(dataExtras?.getSerializable(APPConstants.EXTRAS_KEY_PRODUCT) as Product)
        productPrice.set(
            resourceProvider.getString(
                R.string.str_egp,
                product.get()?.price?.toString()
            )
        )
        super.onViewCreated()

    }

  init {

  }

    fun decreaseBtnClick(){
        if (productQty.get()!!.toInt() > 1){
            productQty.set(productQty.get().minus(1))
            calculateTotalPrice()
        }
    }
    fun increaseBtnClick(){
        productQty.set(productQty.get().plus(1))
        calculateTotalPrice()

    }

    fun addToBasket(){
        val handler = Handler()
        showLoading(true)
        val cartItem = CartProduct(product.get()?.id,product.get()?.name,product.get()?.description,product.get()?.image,product.get()?.price,productQty.get(),(productQty.get() * product.get()?.price!!).toFloat())
        appRepository.addProductToCart(cartItem)
        hideLoading()
        showSuccessBanner(getStringResource(R.string.added_to_basket_success))
        handler.postDelayed(Runnable {
            goBack()
        }, 600)
    }

    fun calculateTotalPrice(){
        totalPrice.set(
            if (productQty.get() > 1) " | " + resourceProvider.getString(
                R.string.str_egp,
                (productQty.get() * product.get()?.price!!).toString()
            ) else "")
    }

}