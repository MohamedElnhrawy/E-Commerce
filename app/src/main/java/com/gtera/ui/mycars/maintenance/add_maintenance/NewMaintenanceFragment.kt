package com.gtera.ui.mycars.maintenance.add_maintenance

import android.content.Context
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.NewMaintenanceLayoutBinding

class NewMaintenanceFragment : BaseFragment<NewMaintenanceLayoutBinding, NewMaintenanceVM>(),
    NewMaintenainceNavigator {
    override fun Context(): Context {
        return this.Context()
    }

    override val layoutId: Int
        get() = R.layout.new_maintenance_layout
    override val viewModelClass: Class<NewMaintenanceVM>
        get() = NewMaintenanceVM::class.java

    override fun setNavigator(viewModel: NewMaintenanceVM?) {
        viewModel?.setNavigator(this)
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_maintenance)


}