package com.gtera.ui.search.result

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Car
import com.gtera.data.model.Model
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.CarType
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.cars_filter.CarsFilterActivity
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.CarItemViewModel
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.CAR_SORT_BY_CREATED_AT
import com.gtera.utils.APPConstants.DESC
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class SearchResultViewModel @Inject constructor() : BaseViewModel<SearchResultNavigator>() {

    init {
        initCarsView()
    }


    var model: Model? = null
    var searchTxt = ""
    var brandsIds = ""
    var numOfCarsFound = ObservableField("")
    var isRefreshing = ObservableBoolean(false)
    var isList = ObservableBoolean(true)
    var carType: ObservableField<CarType> = ObservableField(CarType.CAR_LIST)

    var searchListener: SearchActionListener = object : SearchActionListener {
        override fun preformSearch(searchText: String?) {

            getCars(CAR_SORT_BY_CREATED_AT, DESC)
        }
    }

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var carsVerticalOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL
    var carsGridOrientation: ListOrientation? = ListOrientation.ORIENTATION_GRID

    var carsAdapter: BaseAdapter<CarItemViewModel>? = null

    // adapter's lists
    protected var carsList: ArrayList<CarItemViewModel> = ArrayList<CarItemViewModel>()


    override fun onViewCreated() {
        searchTxt = dataExtras?.getString(APPConstants.EXTRAS_KEY_CAR_SEARCH_TEXT)!!
        brandsIds = dataExtras?.getString(APPConstants.EXTRAS_KEY_CAR_SEARCH_BRANDS_IDS)!!
        setToolbarSearchText(searchTxt)
        setToolbarSearchListener(searchListener)
        setToolbarFilterActionField(R.drawable.ic_search_filter, object :ClickListener{
            override fun onViewClicked() {
                val extras: Bundle = Bundle()
                extras.putBoolean(APPConstants.EXTRAS_KEY_IS_NEW_CARS, true)
                openActivityForResult(
                    CarsFilterActivity::class.java,
                    APPConstants.REQUEST_CODE_FILTER, extras
                )
            }

        }, true)
        super.onViewCreated()

        getCars(CAR_SORT_BY_CREATED_AT, DESC)

    }


    private fun getCars(sortBy: String, desc: Int) {
        showLoading(false)
        appRepository.getCars(
            1, 15,
            0,
            1,
            model?.id,
            brandsIds!!,
            searchTxt,
            null,
            sortBy,
            desc,
            lifecycleOwner,
            object :
                APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {
                    hideLoading()
                    numOfCarsFound.set(result?.data?.size.toString() + " " + getStringResource(R.string.str_found))
                    addCars(result?.data!!)
                    hasData(result?.data?.size!!)
                    isRefreshing.set(false)
                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    isRefreshing.set(false)
                    hasData(0)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getCars(CAR_SORT_BY_CREATED_AT, DESC)
                            }
                        })
                }
            })
    }



    private fun getCars(sortBy: String, desc: Int, queryMap:HashMap<String, String>) {

        queryMap.put("sortBy",sortBy)
        queryMap.put("desc",desc.toString())
        queryMap.put("new","1")

        showLoading(false)
        appRepository.getCars(
            queryMap,
            lifecycleOwner,
            object :
                APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {
                    hideLoading()
                    carsList.clear()
                    hasData(result?.data?.size!!)
                    numOfCarsFound.set(result?.data?.size.toString() + " " + getStringResource(R.string.str_found))
                    addCars(result?.data!!)
                    isRefreshing.set(false)
                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    hasData(0)
                    isRefreshing.set(false)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getCars(CAR_SORT_BY_CREATED_AT, DESC, queryMap)
                            }
                        })
                }
            })
    }

    protected fun initCarsView() {
        carsAdapter = BaseAdapter(carsList, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {
                if (id == R.id.iv_favorite_icon) {

                    if (appRepository.isUserLoggedIn()) {
                        carsList.get(position).car?.isFavorite =
                            carsList.get(position).isFavoriteCar.get()?.not()!!
                        carsList.get(position).isFavoriteCar.set(
                            carsList.get(position).isFavoriteCar.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(carsList.get(position).car?.id!!),
                            APPConstants.FAVORITE_CAR_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        carsList.get(position).isFavoriteCar.get()!!
                    )


                } else if (id == R.id.iv_share_icon) {
                    launchShareIntent(
                        carsList[position].car?.name,
                        carsList[position].car?.shareLink
                    )
                } else {
                    val extras = Bundle()
                    carsList[position].car?.id?.let {
                        extras.putInt(APPConstants.EXTRAS_KEY_CAR_ID, it)
                        carsList[position].car?.let {
                            extras.putString(
                                APPConstants.EXTRAS_KEY_CAR_NAME,
                                carsList[position].car?.name + " " +
                                        carsList[position].car?.brand.let { "${it?.name}" } + " " +
                                        carsList[position].car?.manufactureYear
                            )
                        }
                    }

                    openView(AppScreenRoute.CAR_DETAILS, extras)
                }
            }

        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getCars(CAR_SORT_BY_CREATED_AT, DESC)
        }
    }

    protected fun addCars(List: List<Car?>) {
        if (Utilities.isNullList(List)) return

        this.carsList.clear()
        for (model in List) {
            val carItemViewModel = CarItemViewModel(
                model!!, resourceProvider
            )
            carItemViewModel.carType = carType
            this.carsList.add(carItemViewModel)
        }
        carsAdapter?.updateList(carsList)
        carsAdapter?.notifyDataSetChanged()
    }


    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        if(requestCode==APPConstants.REQUEST_CODE_FILTER) {
            var filterQueryMap =
                extras?.getSerializable(APPConstants.FILTER_OPTION_RESULT)

            if(filterQueryMap!= null)
                getCars(CAR_SORT_BY_CREATED_AT, DESC, filterQueryMap as HashMap<String, String>)
        }

    }


}