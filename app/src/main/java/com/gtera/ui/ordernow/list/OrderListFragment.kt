package com.gtera.ui.ordernow.list

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.OrderListLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView
import com.gtera.ui.utils.AppScreenRoute

class OrderListFragment :
    BaseFragment<OrderListLayoutBinding, OrderListViewModel>(),
    OrderListNavigator {
    override val layoutId: Int
        get() = R.layout.order_list_layout

    override val viewModelClass: Class<OrderListViewModel>
        get() = OrderListViewModel::class.java

    override fun setNavigator(viewModel: OrderListViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_orders_list_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 10
    }

    override fun screenEmptyView(): EmptyView {

        return EmptyView(
            R.drawable.ic_empty_ordes,
            getString(R.string.str_order_requests_empty_header),
            getString(R.string.str_order_requests_empty_body),
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
        get() = getString(R.string.str_order_requests_empty_header)
}