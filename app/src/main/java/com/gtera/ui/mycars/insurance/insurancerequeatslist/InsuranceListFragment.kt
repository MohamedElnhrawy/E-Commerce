package com.gtera.ui.mycars.insurance.insurancerequeatslist

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.InsuranceListLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView
import com.gtera.ui.utils.AppScreenRoute.CAR_RENEWAL_INSURANCE

class InsuranceListFragment :
    BaseFragment<InsuranceListLayoutBinding, InsuranceListViewModel>(),
    InsuranceListNavigator {
    override val layoutId: Int
        get() = R.layout.insurance_list_layout

    override val viewModelClass: Class<InsuranceListViewModel>
        get() = InsuranceListViewModel::class.java

    override fun setNavigator(viewModel: InsuranceListViewModel?) {
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

        return EmptyView(
            R.drawable.ic_empty_insurance,
            getString(R.string.str_insurance_empty_list_header),
            getString(R.string.str_insurance_empty_list_message),
            R.string.str_insurance_empty_list_add,
            object : ClickListener {
                override fun onViewClicked() {

                    viewModel?.openView(CAR_RENEWAL_INSURANCE, null)
                }
            },
            viewModel?.resourceProvider!!
        )
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_renewal_insurance)

    override fun shimmerLoadingCount(): Int {
        return 10
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_insurance_item_list_layout
    }
}