package com.gtera.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.data.model.Promotion
import com.gtera.databinding.CartLayoutBinding
import com.gtera.utils.Utilities
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.inject.Inject


class CartActivity : BaseActivity<CartLayoutBinding, CartViewModel>(), CartNavigator {


    @Inject
    lateinit var cartViewModel: CartViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleDynamicLinks(intent)


    }

    var promotionObj = ObservableField<Promotion?>()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let { newIntent ->
            handleDynamicLinks(newIntent)
        }
    }

    override val viewModelClass: Class<CartViewModel>
        get() = CartViewModel::class.java

    override fun getLayoutRes(): Int {
        return R.layout.cart_layout
    }


    override fun setNavigator(viewModel: CartViewModel?) {
        viewModel?.setNavigator(this)
    }


    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_home_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 1
    }

    override fun hasToolbar(): Boolean {
        return true
    }

    override fun hasBack(): Boolean {
        return true
    }

    override fun hasSearch(): Boolean {
        return false
    }

    override val toolbarTitle: String
        get() = getString(R.string.cart)


    override val isListingView: Boolean
        get() = true

    private fun handleDynamicLinks(intent: Intent) {

        if (intent.data != null) {
            var promotion = Promotion(
                intent.data!!.getQueryParameter(Promotion.CODE),
                intent.data!!.getQueryParameter(Promotion.AMOUNT)?.toDouble(),
                intent.data!!.getQueryParameter(Promotion.EXCLUDE),
                  intent.data!!.getQueryParameter(Promotion.MIN)?.toInt(),
                intent.data!!.getQueryParameter(Promotion.MAX)?.toInt(),
                Utilities.stringToData(intent.data!!.getQueryParameter(Promotion.START)),
                Utilities.stringToData(intent.data!!.getQueryParameter(Promotion.END)))

            promotionObj.set(promotion)
        }

    }

    override fun getPromotion(): Promotion? {
        return promotionObj.get()
    }


}
