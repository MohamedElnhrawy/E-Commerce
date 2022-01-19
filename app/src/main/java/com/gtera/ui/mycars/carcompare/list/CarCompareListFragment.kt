package com.gtera.ui.mycars.carcompare.list

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.CarsCompareListLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView
import com.gtera.ui.utils.AppScreenRoute

class CarCompareListFragment :
    BaseFragment<CarsCompareListLayoutBinding, CarCompareListViewModel>(),
    CarCompareListNavigator {

    override val layoutId: Int
        get() = R.layout.cars_compare_list_layout


    override val viewModelClass: Class<CarCompareListViewModel>
        get() = CarCompareListViewModel::class.java

    override fun setNavigator(viewModel: CarCompareListViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }
    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_compare_list_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 6
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

                    ( viewModel as CarCompareListViewModel).openView(AppScreenRoute.MAIN_SCREEN, null)
                }
            },
            viewModel?.resourceProvider!!
        )
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_compare)


    override fun onResume() {
        super.onResume()
        viewModel?.invalidateList()
    }

}