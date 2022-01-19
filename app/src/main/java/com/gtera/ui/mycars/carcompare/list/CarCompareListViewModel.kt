package com.gtera.ui.mycars.carcompare.list

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Car
import com.gtera.data.model.requests.AddRemoveToCompareRequest
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.data.model.response.DeleteCompareResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import javax.inject.Inject

class CarCompareListViewModel @Inject constructor() : BaseViewModel<CarCompareListNavigator>() {

    init {
        initCarsView()
    }

    var isRefreshing = ObservableBoolean(false)
    var selectedCarsToCompare = ArrayList<Car>()
    var isList = ObservableBoolean(true)

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var compareDetailsOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL

    var compareDetailsAdapter: BaseAdapter<CarCompareItemViewModel>? = null

    // adapter's lists
    protected var compareDetailsList: ArrayList<CarCompareItemViewModel> =
        ArrayList<CarCompareItemViewModel>()


    override fun onViewCreated() {
        super.onViewCreated()

        getCarsToCompare()

    }

    private fun getCarsToCompare() {

        showLoading(false)
        appRepository.getCarCompare(
            null,
            null,
            lifecycleOwner,
            object :
                APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {
                    hideLoading()
                    hasData(result?.data?.size!!)
                    addCars(result.data!!)
                    isRefreshing.set(false)
                }

                override fun onError(throwable: ErrorDetails?) {
                    isRefreshing.set(false)
                    hideLoading()
                    showErrorBanner(throwable?.errorMsg)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getCarsToCompare()
                            }
                        })
                }

            })
    }


    protected fun initCarsView() {

        compareDetailsAdapter = BaseAdapter(compareDetailsList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {

                if (id == R.id.tv_car_add_date) {

                    if (!compareDetailsList[position].isSelected.get()!!) {

                        compareDetailsList[position].car?.isCheckedForCompare = true
                        selectedCarsToCompare.add(compareDetailsList[position].car!!)

                    } else {
                        compareDetailsList[position].car?.isCheckedForCompare = false
                        selectedCarsToCompare.remove(compareDetailsList[position].car!!)
                    }
                    compareDetailsList[position].isSelected.let { it.set(it.get()?.not()) }

                } else if (id == R.id.iv_swipe_item_remove) {

                    var ids = ArrayList<Int>()
                    ids.add(compareDetailsList[position].car?.id!!)
                    var removeCompareRequest = AddRemoveToCompareRequest()
                    removeCompareRequest.ids = ids
                    appRepository.removeCarCompare(
                        removeCompareRequest,
                        lifecycleOwner,
                        object : APICommunicatorListener<DeleteCompareResponse?> {
                            override fun onSuccess(result: DeleteCompareResponse?) {

                                selectedCarsToCompare.remove(compareDetailsList[position].car!!)
                                compareDetailsList.removeAt(position)
                                compareDetailsAdapter?.updateList(compareDetailsList)
                                compareDetailsAdapter?.notifyDataSetChanged()

                                showSuccessBanner(getStringResource(R.string.log_removed_from_compare))
                            }

                            override fun onError(throwable: ErrorDetails?) {
                                showErrorBanner(getStringResource(R.string.str_empty_view_message))
                            }
                        })

                } else if (id == R.id.iv_favorite_icon) {


                    if (appRepository.isUserLoggedIn()) {
                        compareDetailsList[position].car?.isFavorite =
                            compareDetailsList[position].isFavoriteCar.get()?.not()!!
                        compareDetailsList[position].isFavoriteCar.set(
                            compareDetailsList[position].isFavoriteCar.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(compareDetailsList.get(position).car?.id!!),
                            APPConstants.FAVORITE_CAR_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        compareDetailsList.get(position).isFavoriteCar.get()!!
                    )


                } else if (id == R.id.iv_share_icon) {
                    launchShareIntent(
                        compareDetailsList[position].car?.name,
                        compareDetailsList[position].car?.shareLink
                    )
                }else {



                    val extras = Bundle()
                    compareDetailsList[position].car?.id?.let {
                        extras.putInt(APPConstants.EXTRAS_KEY_CAR_ID, it)
                        compareDetailsList[position].car?.let {
                            extras.putString(
                                APPConstants.EXTRAS_KEY_CAR_NAME,
                                compareDetailsList[position].car?.name + " " +
                                        compareDetailsList[position].car?.brand.let { if (it != null) it.name.let {  it?: "" } else "" } + " " +
                                        compareDetailsList[position].car?.manufactureYear
                            )
                        }
                    }

                    if (!compareDetailsList[position].car?.used!!)
                        openView(AppScreenRoute.CAR_DETAILS, extras)
                    else
                        openView(AppScreenRoute.USED_CAR_DETAILS, extras)
                }

            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getCarsToCompare()
        }
    }

    protected fun addCars(List: List<Car?>) {
        if (Utilities.isNullList(List)) return

        this.compareDetailsList.clear()
        for (car in List) {

            val carCompareItemViewModel =
                CarCompareItemViewModel(
                    car!!, resourceProvider, selectedCarsToCompare.contains(car)
                )
            this.compareDetailsList.add(carCompareItemViewModel)
        }
        compareDetailsAdapter?.updateList(compareDetailsList)
        compareDetailsAdapter?.notifyDataSetChanged()
    }


    fun compareCars() {

        if (selectedCarsToCompare.size < 2) {
            showErrorBanner(getStringResource(R.string.str_car_compare_number_validation_message))
            return
        }

        var extra = Bundle()
        extra.putSerializable(APPConstants.EXTRAS_KEY_CAR_COMPARE_LIST, selectedCarsToCompare)

        openView(AppScreenRoute.CAR_COMPARE_DETAILS_SCREEN, extra)
    }


    fun invalidateList() {

        for (selectedCar in compareDetailsList) {
            selectedCar.isSelected.set(selectedCar.car?.isCheckedForCompare)
        }
    }

}