package com.gtera.ui.profile.branches

import android.app.Activity
import android.location.Location
import com.gtera.data.interfaces.GpsListener
import com.gtera.data.interfaces.LocationListener
import com.gtera.data.model.Branche
import com.gtera.ui.profile.branches.branchinfo.BranchInfoVM

interface BranchesNavigator {
     fun getActivity(): Activity?

    fun getLastLocation(listener: LocationListener)

    fun openGps(listener: GpsListener)

    fun drawBranches(branches: ArrayList<Branche>)
    fun drawUserLocation(location: Location)

   fun showBranchInfo(viewModel: BranchInfoVM)

    fun dismissDialog()
    fun selectBranch(branche: Branche)
}