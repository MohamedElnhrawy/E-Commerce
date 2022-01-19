package com.gtera.ui.mycars.maintenance

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.MaintenanceListLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView

class MaintenanceListListFragment : BaseFragment<MaintenanceListLayoutBinding, MaintenanceListVM>(),
    MaintenanceListNavigator {
    override val layoutId: Int
        get() = R.layout.maintenance_list_layout
    override val viewModelClass: Class<MaintenanceListVM>
        get() = MaintenanceListVM::class.java

    override fun setNavigator(viewModel: MaintenanceListVM?) {
        viewModel?.setNavigator(this)
    }


    override val toolbarTitle: String?
        get() = getString(R.string.str_maintenance)

    override val isListingView: Boolean
        get() = true


    override fun screenEmptyView(): EmptyView {

        return EmptyView(R.drawable.ic_tools, "",getString(R.string.str_empty_maintenance), R.string.str_add,
            object : ClickListener {
                override fun onViewClicked() {
                    viewModel?.addMaintenance()
                }
            },
            viewModel?.resourceProvider!!
        )
    }
}