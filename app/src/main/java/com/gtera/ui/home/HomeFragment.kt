package com.gtera.ui.home

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.HomeLayoutBinding

class HomeFragment :
    BaseFragment<HomeLayoutBinding, HomeViewModel>(),
    HomeNavigator {
    override val layoutId: Int
        get() = R.layout.home_layout

    override val viewModelClass: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    override fun setNavigator(viewModel: HomeViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return false
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_home_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 1
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true


}