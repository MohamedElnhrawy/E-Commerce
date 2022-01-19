package com.gtera.ui.choosebudget

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.ChooseYourBudgetLayoutBinding

class ChooseYourBudgetFragment :
    BaseFragment<ChooseYourBudgetLayoutBinding, ChooseYourBudgetViewModel>(),
    ChooseYourBudgetNavigator {
    override val layoutId: Int
        get() = R.layout.choose_your_budget_layout

    override val viewModelClass: Class<ChooseYourBudgetViewModel>
         get() = ChooseYourBudgetViewModel::class.java

    override fun setNavigator(viewModel: ChooseYourBudgetViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_choose_your_budget_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 15
    }


    override val toolbarTitle: String?
        get() = getString(R.string.str_choose_your_budget)



}