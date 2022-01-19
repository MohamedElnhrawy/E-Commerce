package com.gtera.ui.mycars.insurance.insurancedetails

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.InsuranceDetailsLayoutBinding

class InsuranceDetailsFragment :
    BaseFragment<InsuranceDetailsLayoutBinding, InsuranceDetailsViewModel>(),
    InsuranceDetailsNavigator {
    override val layoutId: Int
        get() = R.layout.insurance_details_layout

    override val viewModelClass: Class<InsuranceDetailsViewModel>
        get() = InsuranceDetailsViewModel::class.java

    override fun setNavigator(viewModel: InsuranceDetailsViewModel?) {
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
        get() = getString(R.string.str_renewal_insurance)
}