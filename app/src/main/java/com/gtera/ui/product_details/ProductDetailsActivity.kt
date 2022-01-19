package com.gtera.ui.product_details

import android.app.Activity
import android.os.Bundle
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.data.model.Product
import com.gtera.databinding.ProductDetailsLayoutBinding
import com.gtera.utils.APPConstants
import javax.inject.Inject


class ProductDetailsActivity : BaseActivity<ProductDetailsLayoutBinding, ProductDetailsViewModel>(), ProductDetailsNavigator{


    @Inject
    lateinit var productDetailsViewModel: ProductDetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override val viewModelClass: Class<ProductDetailsViewModel>
        get() = ProductDetailsViewModel::class.java

    override fun getLayoutRes(): Int {
        return R.layout.product_details_layout
    }

    override fun setNavigator(viewModel: ProductDetailsViewModel?) {
        viewModel?.setNavigator(this)
    }

    override fun getActivity(): Activity {
        return this
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
        get() = (intentExtras.getSerializable(APPConstants.EXTRAS_KEY_PRODUCT) as Product).name.toString()


    override val isListingView: Boolean
        get() = false




}
