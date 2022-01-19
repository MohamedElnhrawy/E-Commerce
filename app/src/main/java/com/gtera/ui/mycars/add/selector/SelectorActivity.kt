package com.gtera.ui.mycars.add.selector

import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.SelectorLayoutBinding
import com.gtera.utils.Utilities

class SelectorActivity :
    BaseActivity<SelectorLayoutBinding, SelectorViewModel>(),
    SelectorNavigator {

    override val viewModelClass: Class<SelectorViewModel>
        get() = SelectorViewModel::class.java

    override fun setNavigator(viewModel: SelectorViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true

    override val toolbarTitle: String?
        get() = getString(R.string.str_add_my_car_header)

    override fun hideKeyboard() {
        Utilities.hideSoftKeyboard(this)
    }

    override fun getLayoutRes(): Int {
        return  R.layout.selector_layout
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_selector_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 1
    }

}