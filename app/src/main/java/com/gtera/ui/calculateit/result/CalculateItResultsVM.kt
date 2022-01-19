package com.gtera.ui.calculateit.result

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.model.Car
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import javax.inject.Inject

class CalculateItResultsVM @Inject constructor() : BaseViewModel<CalculateItResultsNavigator>(),
    ViewHolderInterface {

    var budgetNote = ObservableField("")
    var cars = ArrayList<Car>()
    var list = ArrayList<CalculateItCarItemVM>()
    var carsVerticalOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL
    var isRefreshing = ObservableBoolean(false)

    var carsAdapter = BaseAdapter<CalculateItCarItemVM>(list,this)
    val onRefreshListener  = SwipeRefreshLayout.OnRefreshListener {
        isRefreshing.set(true)
    }


    override fun onViewCreated() {
        super.onViewCreated()

        if (dataExtras?.containsKey(APPConstants.EXTRAS_KEY_CARS)!!)
             cars = dataExtras!!.getSerializable(APPConstants.EXTRAS_KEY_CARS) as ArrayList<Car>

        list.clear()
        if (cars.size>0){
            cars.forEach { car ->
                list.add(CalculateItCarItemVM(car,resourceProvider))
            }
        }
        hasData(cars.size)
        carsAdapter.notifyDataSetChanged()

    }

    override fun onViewClicked(position: Int, id: Int) {

        if (id == R.id.iv_favorite_icon) {

            if (appRepository.isUserLoggedIn()) {
                list.get(position).car?.isFavorite =
                    list.get(position).isFavoriteCar.get()?.not()!!
                list.get(position).isFavoriteCar.set(
                    list.get(position).isFavoriteCar.get()?.not()
                )
            }

            addOrRemoveFavorite(
                FavoritesRequest(
                    arrayListOf(list.get(position).car?.id!!),
                    APPConstants.FAVORITE_CAR_TYPE
                ),
                getGoToSignInConfirmationNavigator()!!,
                list.get(position).isFavoriteCar.get()!!
            )


        } else if (id == R.id.iv_share_icon){

            launchShareIntent(list[position].car?.name,list[position].car?.shareLink)

        }else {

            val extras = Bundle()
            list[position].car?.id?.let {
                extras.putInt(APPConstants.EXTRAS_KEY_CAR_ID, it)
                list[position].car?.let {
                    extras.putString(
                        APPConstants.EXTRAS_KEY_CAR_NAME,
                        list[position].car?.name + " " +
                                list[position].car?.brand.let { "${it?.name}" } + " " +
                                list[position].car?.manufactureYear
                    )
                }
            }

            if (!list[position].car?.used!!)
                openView(AppScreenRoute.CAR_DETAILS, extras)
            else
                openView(AppScreenRoute.USED_CAR_DETAILS, extras)

        }
    }
}