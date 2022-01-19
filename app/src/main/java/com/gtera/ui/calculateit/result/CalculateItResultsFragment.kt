package com.gtera.ui.calculateit.result

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.CalculateItResultsFragmentBinding

class CalculateItResultsFragment : BaseFragment<CalculateItResultsFragmentBinding,CalculateItResultsVM>(),
    CalculateItResultsNavigator {
    override val layoutId: Int
        get() = R.layout.calculate_it_results_fragment

    override val viewModelClass: Class<CalculateItResultsVM>
        get() = CalculateItResultsVM::class.java

    override fun setNavigator(viewModel: CalculateItResultsVM?) {
        viewModel!!.setNavigator(this)
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_calculate_it_your_self)

    override val isListingView: Boolean
        get() = true
}