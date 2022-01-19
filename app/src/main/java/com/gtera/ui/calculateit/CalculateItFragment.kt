package com.gtera.ui.calculateit

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.CalculateItFragmentBinding

class CalculateItFragment : BaseFragment<CalculateItFragmentBinding, CalculateItVM>(),
    CalculateItNavigator {
    override val layoutId: Int
        get() = R.layout.calculate_it_fragment

    override val viewModelClass: Class<CalculateItVM>
        get() = CalculateItVM::class.java

    override fun setNavigator(viewModel: CalculateItVM?) {
        viewModel!!.setNavigator(this)
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_calculate_it_your_self)
}