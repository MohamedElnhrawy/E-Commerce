package com.gtera.ui.topdeals

import android.view.View
import androidx.databinding.ViewStubProxy
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.TopdealsLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView

class TopDealsFragment :
    BaseFragment<TopdealsLayoutBinding, TopDealsViewModel>(),
    TopdealsNavigator {
    override val layoutId: Int
        get() = R.layout.topdeals_layout

    override val viewModelClass: Class<TopDealsViewModel>
        get() = TopDealsViewModel::class.java

    override fun setNavigator(viewModel: TopDealsViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }


    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true


    override fun screenEmptyView(): EmptyView {

        return EmptyView(R.drawable.ic_my_cars_empty,
            getString(R.string.oops),
            R.string.str_my_car_empty_txt,
            object : ClickListener {
                override fun onViewClicked() {

                }
            },
            viewModel?.resourceProvider!!
        )
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_home_listing_top_deals_title)


    override val loadingView: ViewStubProxy
        get() = viewDataBinding.loadingView

    override val emptyView: ViewStubProxy
        get() = viewDataBinding.emptyView
    override val dataView: View
        get() = viewDataBinding.swrefDealsList

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_cars_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 6
    }

}