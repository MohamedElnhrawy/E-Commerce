package com.gtera.ui.mycars.carcompare.result

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.CarsCompareDetailsLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView

class CarCompareDetailsFragment :
    BaseFragment<CarsCompareDetailsLayoutBinding, CarCompareDetailsViewModel>(),
    CarCompareDetailsNavigator {

    override val layoutId: Int
        get() = R.layout.cars_compare_details_layout


    override val viewModelClass: Class<CarCompareDetailsViewModel>
        get() = CarCompareDetailsViewModel::class.java

    override fun setNavigator(viewModel: CarCompareDetailsViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }


    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true


    override fun screenEmptyView(): EmptyView {

        return EmptyView(R.drawable.ic_empty_compare,
            getString(R.string.str_car_compare_empty_header),
            getString(R.string.str_car_compare_empty_message),
            R.string.str_car_compare_Add,
            object : ClickListener {
                override fun onViewClicked() {

                }
            },
            viewModel?.resourceProvider!!
        )
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_compare)



}