package com.gtera.ui.mycars.insurance.endorsement

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.LanguageLayoutBinding

class EndorsementFragment :
    BaseFragment<LanguageLayoutBinding, EndorsementViewModel>(),
    EndorsementNavigator {
    override val layoutId: Int
        get() = R.layout.endorsement_layout

    override val viewModelClass: Class<EndorsementViewModel>
        get() = EndorsementViewModel::class.java

    override fun setNavigator(viewModel: EndorsementViewModel?) {
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