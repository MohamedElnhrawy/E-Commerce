package com.gtera.ui.choosebudget.carlist

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.BudgetCarsListLayoutBinding

class BudgetCarListFragment :
    BaseFragment<BudgetCarsListLayoutBinding, BudgetCarListViewModel>(),
    BudgetCarListNavigator {

    override val layoutId: Int
        get() = R.layout.budget_cars_list_layout


    override val viewModelClass: Class<BudgetCarListViewModel>
        get() = BudgetCarListViewModel::class.java

    override fun setNavigator(viewModel: BudgetCarListViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }


    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true


//    override val toolbarTitle: String?
//        get() = viewModel?.modelName?.get()


}