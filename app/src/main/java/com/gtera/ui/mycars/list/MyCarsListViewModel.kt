package com.gtera.ui.mycars.list

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.base.SelectorType
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.mycars.add.selector.SelectorActivity
import com.gtera.ui.mycars.list.source.MyCarsDataSourceFactory
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.IS_MY_CAR
import javax.inject.Inject

class MyCarsListViewModel @Inject constructor() : BaseViewModel<MyCarsListNavigator>() {


    lateinit var carsDataSourceFactory: MyCarsDataSourceFactory
    var myCarsAdapter: BasePagedListAdapter<MyCarListItemViewModel>? =
        BasePagedListAdapter<MyCarListItemViewModel>(object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {


                if (id == R.id.iv_favorite_icon) {


                    if (appRepository.isUserLoggedIn()) {
                        myCarsList.value?.get(position)?.car?.isFavorite =
                            myCarsList.value?.get(position)?.isFavoriteCar?.get()?.not()!!
                        myCarsList.value?.get(position)?.isFavoriteCar?.set(
                            myCarsList.value?.get(position)?.isFavoriteCar?.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(myCarsList.value?.get(position)?.car?.id!!),
                            APPConstants.FAVORITE_CAR_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        myCarsList.value?.get(position)?.isFavoriteCar?.get()!!
                    )


                } else if (id == R.id.iv_share_icon) {
                    launchShareIntent(
                        myCarsList.value?.get(position)?.car?.name,
                        myCarsList.value?.get(position)?.car?.shareLink
                    )
                } else {

                    val extras = Bundle()
                    myCarsList.value?.get(position)?.car?.id?.let {
                        extras.putInt(APPConstants.EXTRAS_KEY_CAR_ID, it)
                        myCarsList.value?.get(position)!!.car?.let {
                            extras.putString(
                                APPConstants.EXTRAS_KEY_CAR_NAME,
                                myCarsList.value?.get(position)?.car?.name + " " +
                                        myCarsList.value?.get(position)?.car?.brand.let { "${it?.name}" } + " " +
                                        myCarsList.value?.get(position)?.car?.manufactureYear
                            )
                        }
                    }
                    extras.putBoolean(IS_MY_CAR, true)

                    openView(AppScreenRoute.USED_CAR_DETAILS, extras)

                }
            }
        })

    // adapter's lists
    lateinit var myCarsList: LiveData<PagedList<MyCarListItemViewModel>>
    private var list: PagedList<MyCarListItemViewModel>? = null
    var isRefreshing = ObservableBoolean(false)

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var myCarsOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL


    override fun onViewCreated() {
        super.onViewCreated()
        getCars()

        myCarsList.observe(lifecycleOwner!!, Observer {
            list?.clear()
            list?.addAll(it)
            myCarsAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }
            isRefreshing.set(false)

        })

    }

    override fun onViewRecreated() {
        super.onViewRecreated()

    }


    private fun getCars(): LiveData<PagedList<MyCarListItemViewModel>> {

        carsDataSourceFactory = MyCarsDataSourceFactory(
            this,
            appRepository,
            resourceProvider,
            lifecycleOwner
        )

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .setPageSize(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .build()
        myCarsList = LivePagedListBuilder(
            carsDataSourceFactory,
            config
        ).build() as LiveData<PagedList<MyCarListItemViewModel>>
        return myCarsList

    }

    protected fun initMyCarsView() {
        myCarsAdapter = BasePagedListAdapter(object : ViewHolderInterface {

            override fun onViewClicked(position: Int, id: Int) {

            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getCars()
        }
    }


    fun addNewCar(){

        val extras = Bundle()
        extras.putSerializable(APPConstants.SELECTOR_TYPE, SelectorType.BRAND)
        extras.putSerializable(
            APPConstants.SELECTOR_HEADER_TEXT,
            resourceProvider.getString(R.string.str_add_my_car_select_brand)
        )
        extras.putSerializable(
            APPConstants.SELECTOR_OTHER_TEXT,
            resourceProvider.getString(
                R.string.str_add_my_car_other,
               resourceProvider.getString(R.string.str_add_my_car_brand)
            )
        )
        openActivity(SelectorActivity::class.java, extras)
    }

}