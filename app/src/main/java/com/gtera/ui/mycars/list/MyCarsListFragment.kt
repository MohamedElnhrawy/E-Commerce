package com.gtera.ui.mycars.list

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.MyCarsListLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView

class MyCarsListFragment :
    BaseFragment<MyCarsListLayoutBinding, MyCarsListViewModel>(),
    MyCarsListNavigator {
    override val layoutId: Int
        get() = R.layout.my_cars_list_layout

    override val viewModelClass: Class<MyCarsListViewModel>
        get() = MyCarsListViewModel::class.java

    override fun setNavigator(viewModel: MyCarsListViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override val toolbarElevation: Boolean
        get() = false

    override fun shimmerLoadingCount(): Int {
        return 8
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_mycars_list_layout
    }

    override val isListingView: Boolean
        get() = true

    override val toolbarTitle: String?
        get() = getString(R.string.str_add_my_car)

    override fun screenEmptyView(): EmptyView {

        return EmptyView(
            R.drawable.ic_my_car_empty,
            getString(R.string.str_add_my_car_empty_header),
            getString(R.string.str_add_my_car_empty_message),
            R.string.str_add_my_car_add,
            object : ClickListener {
                override fun onViewClicked() {


                viewModel?.addNewCar()
                }
            },
            viewModel?.resourceProvider!!
        )
    }
}