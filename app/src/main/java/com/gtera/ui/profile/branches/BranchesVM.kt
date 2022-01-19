package com.gtera.ui.profile.branches

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableInt
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.interfaces.GpsListener
import com.gtera.data.interfaces.LocationListener
import com.gtera.data.model.Branche
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.TabLayoutListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.profile.branches.branchinfo.BranchInfoNavigator
import com.gtera.ui.profile.branches.branchinfo.BranchInfoVM
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.REQUEST_CODE_LOCATION_PERMISSION
import javax.inject.Inject

class BranchesVM @Inject constructor() : BaseViewModel<BranchesNavigator>(), ViewHolderInterface,
    GpsListener {

    var locationListener = object : LocationListener {
        override fun currentLocation(location: Location) {
            navigator!!.drawUserLocation(location)
        }

    }

    override fun gpsStatus(status: Boolean) {
        if (status)
            if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)!! && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!!) {
                navigator!!.getLastLocation(locationListener)

            }
    }

    override fun onViewClicked(position: Int, id: Int) {
        launchMapIntent(branches[position].latitude,branches[position].longitude)
        //TODO:CHECK ACTION
//        tabListener.tabSelected(0)
//        navigator?.selectMarker(branches[position])
    }

    var currentTabPos = ObservableInt(0)
    var tabSelectedPos = 0


    var branchesList = ArrayList<BranchItemVM>()
    var branches = ArrayList<Branche>()
    var branchesAdapter = BaseAdapter<BranchItemVM>(branchesList, this)

    var tabList = ObservableArrayList<String>()
    var tabListener: TabLayoutListener = object : TabLayoutListener {
        override fun tabSelected(position: Int) {
            if (position == currentTabPos.get())
                return

            currentTabPos.set(position)
            tabSelectedPos = position

        }
    }

    override fun onViewCreated() {
        super.onViewCreated()
        initTabList()

        currentTabPos.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                when (currentTabPos.get()) {
                    0 -> {
//                        navigator?.drawBranches(branches)
                    }
                    1 -> {

                    }
                }
            }

        })
    }

    fun initTabList() {
        tabList.add(getStringResource(R.string.str_map))
        tabList.add(getStringResource(R.string.str_list))
    }

    fun getBranches() {

        appRepository.getBranches(
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<List<Branche?>?>?> {
                override fun onSuccess(result: BaseResponse<List<Branche?>?>?) {

                    result?.data?.forEach { branch ->
                        branch?.let {
                            branches.add(it)
                            BranchItemVM(it, resourceProvider)
                        }?.let {
                            branchesList.add(it)
                        }
                    }
                    navigator!!.drawBranches(branches)

                    branchesAdapter.notifyDataSetChanged()

                    getUserLocation()
                }

                override fun onError(throwable: ErrorDetails?) {

                }


            })
    }

    private fun getUserLocation() {
        if (isLocationEnabled(navigator!!.getActivity())!! && hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)!!
            && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!!
        )
            navigator!!.getLastLocation(locationListener)
        else {
            navigator!!.openGps(this)
            requestPermissionsSafely(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_CODE_LOCATION_PERMISSION
            )
        }
    }

    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        super.onViewResult(requestCode, resultCode, extras)
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestResultsReady(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray?
    ) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults!!.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)!!) {
                    //                    currentLocation = startRequestLocation();
                    if (isLocationEnabled(navigator!!.getActivity())!!)
                        navigator!!.getLastLocation(locationListener)

                }
            }
        }
    }

    fun showBranchInfo(title:String?){
        for (item in branches){
            if (item.name.equals(title)) {
                navigator?.showBranchInfo(BranchInfoVM(item, object : BranchInfoNavigator {
                    override fun performCall(phone: String?) {
                        launchCallIntent(phone)
                    }

                    override fun contactUs(branche: Branche?) {
                        navigator?.dismissDialog()
                        val extras = Bundle()
                        extras.putSerializable(APPConstants.EXTRAS_KEY_BRANCH,branche)
                        openView(AppScreenRoute.CONTACT_US,extras)
                    }

                }, resourceProvider))
                return
            }
        }

    }
}