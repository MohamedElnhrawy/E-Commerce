package com.gtera.ui.cardetials

import android.content.Context
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.CarsDetailsLayoutBinding
import com.gtera.utils.APPConstants

class CarDetailsFragment :
    BaseFragment<CarsDetailsLayoutBinding, CarDetailsViewModel>(),
    CarDetailsNavigator {


    override fun context(): Context? {
        return baseActivity
    }

    override val layoutId: Int
        get() = R.layout.cars_details_layout

    override val viewModelClass: Class<CarDetailsViewModel>
         get() = CarDetailsViewModel::class.java

    override fun setNavigator(viewModel: CarDetailsViewModel?) {
        viewModel!!.setNavigator(this)

    }

    override fun hasBack(): Boolean {
        return true
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true


//    override fun screenEmptyView(): EmptyView {
//
//            return EmptyView(R.mipmap.empty_result, getString(R.string.oops), R.string.empty_default_txt,
//                object : ClickListener {
//                    override fun onViewClicked() {
//
//                    }
//                })
//    }

    override val toolbarTitle: String?
        get() = intentExtras?.getString(APPConstants.EXTRAS_KEY_CAR_NAME, "")




}