package com.gtera.ui.ordernow.acknowledgment

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.OrderNowAcknowledgmentLayoutBinding

class AcknowledgmentFragment :
    BaseFragment<OrderNowAcknowledgmentLayoutBinding, AcknowledgmentViewModel>(),
    AcknowledgmentNavigator {
    override val layoutId: Int
        get() = R.layout.order_now_acknowledgment_layout

    override val viewModelClass: Class<AcknowledgmentViewModel>
        get() = AcknowledgmentViewModel::class.java

    override fun setNavigator(viewModel: AcknowledgmentViewModel?) {
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
        get() = getString(R.string.str_car_insurance_request_endorsement)
}