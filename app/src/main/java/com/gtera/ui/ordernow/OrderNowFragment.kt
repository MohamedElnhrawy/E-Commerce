package com.gtera.ui.ordernow

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.OrderNowLayoutBindingImpl
import com.gtera.utils.APPConstants

class OrderNowFragment :
    BaseFragment<OrderNowLayoutBindingImpl, OrderNowViewModel>(),
    OrderNowNavigator {
    override val layoutId: Int
        get() = R.layout.order_now_layout

    override val viewModelClass: Class<OrderNowViewModel>
        get() = OrderNowViewModel::class.java

    override fun setNavigator(viewModel: OrderNowViewModel?) {
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

        get() = intentExtras?.getString(APPConstants.ORDER_NOW_CAR_NAME)
}