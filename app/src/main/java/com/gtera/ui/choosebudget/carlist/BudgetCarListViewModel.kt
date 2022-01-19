package com.gtera.ui.choosebudget.carlist

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.model.Budget
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.ui.base.CarType
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.CarItemViewModel
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.CAR_SORT_BY_CREATED_AT
import com.gtera.utils.APPConstants.DESC
import javax.inject.Inject

class BudgetCarListViewModel @Inject constructor() : BaseViewModel<BudgetCarListNavigator>() {

    init {
        initCarsView()
    }


    var budget: Budget? = null
    var selectedBudgetId: Int? = null
    var selectedBrandsId: String? = null
    var budgetFromTo = ObservableField("")
    var numOfCarsFound = ObservableField("")
    var isNewCar: Boolean? = null
    var selectedBrand: Int? = null
    lateinit var carsDataSourceFactory: CarsDataSourceFactory
    var carsAdapter: BasePagedListAdapter<CarItemViewModel>? = null

    // adapter's lists
    lateinit var carsList: LiveData<PagedList<CarItemViewModel>>
    private var list: PagedList<CarItemViewModel>? = null
    var isRefreshing = ObservableBoolean(false)

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null
    var budgetCarsOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL


    override fun onViewCreated() {
        super.onViewCreated()
        budget = dataExtras?.getSerializable(APPConstants.EXTRAS_KEY_BUDGET) as Budget
        selectedBudgetId = budget?.id
        selectedBrandsId = dataExtras?.getString(APPConstants.EXTRAS_KEY_BUDGET_BRAND_ID)
        budgetFromTo.set(
            resourceProvider.getString(
                R.string.str_from,
                resourceProvider.getString(R.string.str_egp, budget?.from)
            )
                    + " " + resourceProvider.getString(
                R.string.str_to, resourceProvider.getString(
                    R.string.str_egp, budget?.to
                )
            )
        )

        getCars()

        carsList.observe(lifecycleOwner!!, Observer {
            list?.clear()
            list?.addAll(it)
            carsAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }
            isRefreshing.set(false)

        })

    }

    private fun getCars(): LiveData<PagedList<CarItemViewModel>> {

        carsDataSourceFactory = CarsDataSourceFactory(
            this,
            selectedBrandsId,
            null,
            selectedBudgetId,
            CAR_SORT_BY_CREATED_AT,
            DESC,
            true,
            CarType.CAR_BUDGET_LIST,
            appRepository,
            resourceProvider,
            lifecycleOwner
        )

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .setPageSize(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .build()
        carsList = LivePagedListBuilder(
            carsDataSourceFactory,
            config
        ).build() as LiveData<PagedList<CarItemViewModel>>
        return carsList

    }


    protected fun initCarsView() {
        carsAdapter = BasePagedListAdapter(object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {

                if (id == R.id.iv_favorite_icon) {


                    if (appRepository.isUserLoggedIn()) {
                        carsList.value?.get(position)?.car?.isFavorite =
                            carsList.value?.get(position)?.isFavoriteCar?.get()?.not()!!
                        carsList.value?.get(position)?.isFavoriteCar?.set(
                            carsList.value?.get(position)?.isFavoriteCar?.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(carsList.value?.get(position)?.car?.id!!),
                            APPConstants.FAVORITE_CAR_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        carsList.value?.get(position)?.isFavoriteCar?.get()!!
                    )


                } else if (id == R.id.iv_share_icon) {
                    launchShareIntent(
                        carsList.value?.get(position)?.car?.name,
                        carsList.value?.get(position)?.car?.shareLink
                    )
                } else {

                    val extras = Bundle()
                    carsList.value?.get(position)?.car?.id?.let {
                        extras.putInt(
                            APPConstants.EXTRAS_KEY_CAR_ID,
                            it
                        )
                    }

                    openView(AppScreenRoute.CAR_DETAILS, extras)
                }
            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getCars()
        }
    }


}