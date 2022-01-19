package com.gtera.ui.usedcardetails

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.CarsDetailsLayoutBinding
import com.gtera.utils.APPConstants

class UsedCarDetailsFragment :
    BaseFragment<CarsDetailsLayoutBinding, UsedCarDetailsViewModel>(),
    UsedCarDetailsNavigator {
    override val layoutId: Int
        get() = R.layout.used_car_details_layout

    override val viewModelClass: Class<UsedCarDetailsViewModel>
         get() = UsedCarDetailsViewModel::class.java

    override fun setNavigator(viewModel: UsedCarDetailsViewModel?) {
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
        get() = intentExtras?.getString(APPConstants.EXTRAS_KEY_CAR_NAME, "")




}