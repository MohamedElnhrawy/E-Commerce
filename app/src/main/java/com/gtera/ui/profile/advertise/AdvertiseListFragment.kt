package com.gtera.ui.profile.advertise

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.AdvertiseListLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView
import com.gtera.ui.ordernow.list.OrderListViewModel
import com.gtera.ui.utils.AppScreenRoute

class AdvertiseListFragment :
    BaseFragment<AdvertiseListLayoutBinding, AdvertiseListViewModel>(),
    AdvertiseListNavigator {
    override val layoutId: Int
        get() = R.layout.advertise_list_layout

    override val viewModelClass: Class<AdvertiseListViewModel>
        get() = AdvertiseListViewModel::class.java

    override fun setNavigator(viewModel: AdvertiseListViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override fun screenEmptyView(): EmptyView {

        return EmptyView(
            R.drawable.ic_empty_advertise,
            getString(R.string.str_advertising_empty_header),
            getString(R.string.str_advertising_empty_body),
            R.string.str_add,
            object : ClickListener {
                override fun onViewClicked() {

                    (viewModel as OrderListViewModel).openView(
                        AppScreenRoute.MAIN_SCREEN,
                        null
                    )
                }
            },
            viewModel?.resourceProvider!!
        )
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true

    override val toolbarTitle: String?
        get() = getString(R.string.str_advertising_title)
}