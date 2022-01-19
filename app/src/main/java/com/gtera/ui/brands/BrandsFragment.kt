package com.gtera.ui.brands

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.BrandsLayoutBinding
import com.gtera.utils.Utilities

class BrandsFragment :
    BaseFragment<BrandsLayoutBinding, BrandsViewModel>(),
    BrandsNavigator {
    override val layoutId: Int
        get() = R.layout.brands_layout

    override val viewModelClass: Class<BrandsViewModel>
        get() = BrandsViewModel::class.java

    override fun setNavigator(viewModel: BrandsViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hideKeyboard() {
        Utilities.hideSoftKeyboard(baseActivity!!)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_brands_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 1
    }

    override val toolbarElevation: Boolean
        get() = false


    override val toolbarTitle: String?
        get() = getString(R.string.str_brands_title)

    override val isListingView: Boolean
        get() = true
}