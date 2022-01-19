package com.gtera.ui.mycars.maintenance

import com.gtera.base.BaseViewModel
import com.gtera.ui.utils.AppScreenRoute
import javax.inject.Inject

class MaintenanceListVM @Inject constructor() : BaseViewModel<MaintenanceListNavigator>() {

    override fun onViewCreated() {
        super.onViewCreated()
        hasData(0)
    }
    fun addMaintenance(){

        openView(AppScreenRoute.ADD_MAINTENANCE,null)
    }
}