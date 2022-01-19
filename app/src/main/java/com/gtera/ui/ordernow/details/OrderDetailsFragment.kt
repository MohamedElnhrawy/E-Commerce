package com.gtera.ui.ordernow.details

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.OrderDetailsLayoutBinding

class OrderDetailsFragment :
    BaseFragment<OrderDetailsLayoutBinding, OrderDetailsViewModel>(),
    OrdeDetailsNavigator {
    override val layoutId: Int
        get() = R.layout.order_details_layout

    override val viewModelClass: Class<OrderDetailsViewModel>
        get() = OrderDetailsViewModel::class.java

    override fun setNavigator(viewModel: OrderDetailsViewModel?) {
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
        get() = getString(R.string.str_language_title)
}