package com.gtera.ui.cars_filter

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.CarsFilterLayoutBinding
import com.gtera.utils.Utilities

class CarsFilterFragment :
    BaseFragment<CarsFilterLayoutBinding, CarsFilterViewModel>(),
    CarsFilterNavigator {
    override val layoutId: Int
        get() = R.layout.cars_filter_layout

    override val viewModelClass: Class<CarsFilterViewModel>
        get() = CarsFilterViewModel::class.java

    override fun setNavigator(viewModel: CarsFilterViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hideKeyboard() {
        Utilities.hideSoftKeyboard(baseActivity!!)
    }

    override fun hasBack(): Boolean {
        return true
    }


    override fun shimmerLoadingCount(): Int {
        return 6
    }

    override val toolbarElevation: Boolean
        get() = false


    override val toolbarTitle: String?
        get() = getString(R.string.str_filter_title)

    override val isListingView: Boolean
        get() = true
}