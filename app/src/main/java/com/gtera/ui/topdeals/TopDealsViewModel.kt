package com.gtera.ui.topdeals

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.TopDeal
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.CarType
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogNavigator
import com.gtera.ui.home.viewmodels.TotDealsItemViewModel
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import javax.inject.Inject



class TopDealsViewModel @Inject constructor() : BaseViewModel<TopdealsNavigator>() {

    init {
        initTopDeals()

    }
    private var selectedSortPos: Int = 0
    var carType: ObservableField<CarType> = ObservableField(CarType.CAR_LIST)
    var isList = ObservableBoolean(true)
    var gridOrientationClick =
        View.OnClickListener { v: View? ->
            isList.set(false)
            carOrientation.set(ListOrientation.ORIENTATION_GRID)
            carType.set(CarType.CAR_GRID)
        }

    var listOrientationClick =
        View.OnClickListener { v: View? ->
            isList.set(true)
            carOrientation.set(ListOrientation.ORIENTATION_VERTICAL)
            carType.set(CarType.CAR_LIST)
        }

    var topDealsCount = ObservableField("")

    var topDealsAdapter: BaseAdapter<TotDealsItemViewModel>? = null

    protected var topDealsList: ArrayList<TotDealsItemViewModel> =
        ArrayList<TotDealsItemViewModel>()

    var isRefreshing = ObservableBoolean(false)


    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null
    var carsVerticalOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL
    var carsGridOrientation: ListOrientation? = ListOrientation.ORIENTATION_GRID
    var carOrientation= ObservableField<ListOrientation>(ListOrientation.ORIENTATION_VERTICAL)




    override fun onViewCreated() {
        super.onViewCreated()
        getTopDealsList("",APPConstants.DESC)

    }

    private fun getTopDealsList(sortBy: String?,desc: Int?) {

        showLoading(false)
        appRepository.getTopDeals(
            true,
            sortBy,
            desc,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<TopDeal?>?>?> {

                override fun onSuccess(result: BaseResponse<ArrayList<TopDeal?>?>?) {
                    result?.data?.let { addTopDeals(it) }

                    topDealsCount.set(
                        result?.data?.size.toString() + " " + resourceProvider.getString(
                            R.string.str_item
                        )
                    )

                    hideLoading()
                    isRefreshing.set(false)

                }

                override fun onError(throwable: ErrorDetails?) {

                    isRefreshing.set(false)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getTopDealsList(sortBy,desc)
                            }
                        })

                }

            })
    }


    protected fun addTopDeals(List: List<TopDeal?>) {
        if (Utilities.isNullList(List)) return

        this.topDealsList.clear()
        for (hotDeal in List) {
            val topDealsItemViewModel = TotDealsItemViewModel(
                hotDeal, resourceProvider, carOrientation.get()!!
            )
            topDealsItemViewModel.orientation = carOrientation
            this.topDealsList.add(topDealsItemViewModel)
        }
        topDealsAdapter?.updateList(topDealsList)
        topDealsAdapter?.notifyDataSetChanged()
    }


    protected fun initTopDeals() {
        topDealsAdapter = BaseAdapter(topDealsList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {


                if (id == R.id.iv_favorite_icon) {

                    if (appRepository.isUserLoggedIn()) {
                        topDealsList.get(position).hotDealsItem?.car?.isFavorite =
                            topDealsList.get(position).isFavoriteCar.get()?.not()!!
                        topDealsList.get(position).isFavoriteCar.set(
                            topDealsList.get(position).isFavoriteCar.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(topDealsList.get(position).hotDealsItem?.car?.id!!),
                            APPConstants.FAVORITE_OFFER_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        topDealsList.get(position).isFavoriteCar.get()!!
                    )


                } else {


                    val extras = Bundle()
                    topDealsList[position].hotDealsItem?.car?.id?.let {
                        extras.putInt(APPConstants.EXTRAS_KEY_CAR_ID, it)
                        topDealsList[position].hotDealsItem?.let {
                            extras.putString(
                                APPConstants.EXTRAS_KEY_CAR_NAME,
                                topDealsList[position].hotDealsItem?.name + " " +
                                        topDealsList[position].hotDealsItem?.car?.brand?.name + " " +
                                        topDealsList[position].hotDealsItem?.car?.manufactureYear
                            )
                        }
                    }

                    openView(AppScreenRoute.CAR_DETAILS, extras)

                }
            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getTopDealsList("",APPConstants.DESC)
        }


    }

    fun showSortingDialog() {

        val layout = R.layout.bottom_sheet_layout

        val itemsList = arrayListOf(
            getStringResource(R.string.str_car_sorting_latest),
            getStringResource(R.string.str_car_popularity),
            getStringResource(R.string.str_car_high_prices),
            getStringResource(R.string.str_car_low_prices)
        )

        val itemsIcons = arrayListOf(
            R.drawable.ic_latest,
            R.drawable.ic_popularity,
            R.drawable.ic_high_prices,
            R.drawable.ic_low_prices
        )
        showBottomSheetDialog(
            layout,
            getStringResource(R.string.str_car_sorting),
            itemsList,
            itemsIcons,
            selectedSortPos,
            false,

            object : BottomSheetDialogNavigator {
                override fun onItemSelected(itemClicked: String, pos: Int) {
                    selectedSortPos = pos
                    when (itemClicked) {
                        getStringResource(R.string.str_car_sorting_latest)
                        ->  getTopDealsList(APPConstants.CAR_SORT_BY_CREATED_AT, APPConstants.DESC)
                        getStringResource(R.string.str_car_popularity)
                        -> showLongToast("Not Implemented Yet")
                        getStringResource(R.string.str_car_high_prices)
                        -> getTopDealsList(APPConstants.TOP_DEALS_SORT_BY_PRICE, APPConstants.DESC)
                        getStringResource(R.string.str_car_low_prices)
                        ->  getTopDealsList(APPConstants.TOP_DEALS_SORT_BY_PRICE, APPConstants.ASC)
                    }
                }

            },
            resourceProvider
        )

    }

}