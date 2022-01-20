package com.gtera.ui.cart

import android.os.Handler
import androidx.databinding.*
import androidx.databinding.Observable
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.model.CartProduct
import com.gtera.data.model.Promotion
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.CartItemViewModel
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class CartViewModel @Inject constructor() : BaseViewModel<CartNavigator>() {

    init {
        initCart()
    }

    // adapters
    var cartAdapter: BaseAdapter<CartItemViewModel>? = null

    // adapter's lists
    var cartList: ArrayList<CartItemViewModel> = ArrayList()
    var cartProductList= ObservableArrayList <CartProduct?>()

    //Orientation
    var cartOrientation: ListOrientation = ListOrientation.ORIENTATION_VERTICAL

    var totalCoastField = ObservableField("")
    var totalCartItems = ObservableInt(0)
    var totalCoast = ObservableDouble(0.0)
    var hasCart = ObservableBoolean(false)
    var showPromotion = ObservableBoolean(false)
    var promotion = ObservableField<Promotion>()

    override fun onViewCreated() {
        super.onViewCreated()
        getLiveCart()

    }

    private fun initCart() {
        cartAdapter = BaseAdapter(cartList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {
//                val extras = Bundle()
//                extras.putSerializable(APPConstants.EXTRAS_KEY_NEWS, newsList[position].news)
//                openView(AppScreenRoute.HOME_NEWS_DETAILS, extras)
            }
        })

    }


    private fun addCarts(list: List<CartProduct?>) {
        if (Utilities.isNullList(list)) return

        this.cartList.clear()

        var coast = 0.0
        var count = 0
        for (item in list) {
            val cartItemViewModel = item?.let {
                CartItemViewModel(
                    it, resourceProvider, cartOrientation
                )
            }
            cartItemViewModel?.let { this.cartList.add(it) }
            (item?.cartQuantity)?.let {
                count += it
                it.times(item.price!!).let {
                    coast += it
                }
            }

        }
        cartAdapter?.updateList(cartList)
        cartAdapter?.notifyDataSetChanged()
        totalCartItems.set(count)
        totalCoastField.set(
            resourceProvider.getString(R.string.str_Order_now) + " | " + resourceProvider.getString(
                R.string.str_egp, coast.toString()
            )
        )
        totalCoast.set(coast)

        promotion.set(navigator?.getPromotion())
        promotion.get()?.let {
            showDynamicLinkOffer()
        }
    }


    private fun getLiveCart() {

        appRepository.getLiveCartList(lifecycleOwner,
            androidx.lifecycle.Observer<List<CartProduct?>?> {
                hideLoading()
                it?.let { it1 ->
                    hasData(it1.size)
                    hasCart.set(it1.isNotEmpty())

                }
                it?.let { it1 ->
                    cartProductList.clear()
                    cartProductList.addAll(it1)
                    addCarts(it1)

                }
            })

    }


    fun checkoutClicked() {
        showLoading(true)
        val handler = Handler()
        appRepository.clearCart()
        showSuccessBanner(getStringResource(R.string.order_have_placed))
        handler.postDelayed(Runnable {
            goBack()
        }, 800)
    }

    private fun showDynamicLinkOffer() {
        val current = Utilities.getCurrentDate()
        if (promotion.get()!!.Amount == null){
            showErrorBanner(getStringFromResources(R.string.invalid_promotion_amount))
        } else if (!promotion.get()!!.isValidDatePromotion(current)) {
            showErrorBanner(getStringFromResources(R.string.invalid_promotion))
        } else if (!promotion.get()!!.isValidCartItemsCount(totalCartItems.get())) {
            showErrorBanner(getStringFromResources(R.string.excedded_items))
        } else if (!isPromotionLowerThanFifPercent(promotion.get()!!.Amount!!)) {
            showErrorBanner(getStringFromResources(R.string.increase_cart_items))
        } else if (isExcludedCategoryExists(promotion.get()!!.exclude) != null) {
            showErrorBanner(
                getStringFromResources(R.string.excluded_category_exists) + " \"${
                    isExcludedCategoryExists(
                        promotion.get()!!.exclude
                    )
                }\" "
            )
        } else {
            // apply promotion
            showPromotion.set(true)
            totalCoast.set(totalCoast.get() - promotion.get()!!.Amount!!)
            totalCoastField.set(
                resourceProvider.getString(R.string.str_Order_now) + " | " + resourceProvider.getString(
                    R.string.str_egp, totalCoast.get().toString()
                )
            )
        }
    }


    fun isPromotionLowerThanFifPercent(promotionAmount: Double): Boolean {
        val costPercent = totalCoast.get() * .5
        val costWithPromotion = totalCoast.get() - promotionAmount
        return costPercent  <= costWithPromotion
    }

    fun isExcludedCategoryExists(excludedCategory: String?): String? {
        for (item in cartProductList) {
            if (item != null && excludedCategory != null) {
                if (item.categoryName.equals(excludedCategory))
                    return excludedCategory
            }
        }
        return null
    }
}