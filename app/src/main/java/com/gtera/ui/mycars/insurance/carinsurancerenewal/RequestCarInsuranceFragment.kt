package com.gtera.ui.mycars.insurance.carinsurancerenewal

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.RequestCarInsuranceLayoutBinding

class RequestCarInsuranceFragment :
    BaseFragment<RequestCarInsuranceLayoutBinding, RequestCarInsuranceViewModel>(),
    RequestCarInsuranceNavigator {
    override val layoutId: Int
        get() = R.layout.request_car_insurance_layout

    override val viewModelClass: Class<RequestCarInsuranceViewModel>
        get() = RequestCarInsuranceViewModel::class.java

    override fun setNavigator(viewModel: RequestCarInsuranceViewModel?) {
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
        get() = getString(R.string.str_car_insurance_request_title)
}