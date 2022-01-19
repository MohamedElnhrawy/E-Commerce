package com.gtera.ui.offers

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.OffersLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView

class OffersFragment :
    BaseFragment<OffersLayoutBinding, OffersViewModel>(),
    OffersNavigator {
    override val layoutId: Int
        get() = R.layout.offers_layout

    override val viewModelClass: Class<OffersViewModel>
         get() = OffersViewModel::class.java

    override fun setNavigator(viewModel: OffersViewModel?) {
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

            return EmptyView(R.drawable.ic_my_cars_empty, getString(R.string.oops), R.string.str_my_car_empty_txt,
                object : ClickListener {
                    override fun onViewClicked() {

                    }
                }, viewModel?.resourceProvider!!)
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_home_listing_offers_title)


    override fun shimmerLoadingCount(): Int {
        return 10
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_offers_list_layout
    }
}