package com.gtera.ui.ordernow.list

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
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.ordernow.list.datasource.MyOrdersDataSourceFactory
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.ORDER_NOW_CAR_ORDER
import javax.inject.Inject

class OrderListViewModel @Inject constructor() : BaseViewModel<OrderListNavigator>() {

    lateinit var ordersDataSourceFactory: MyOrdersDataSourceFactory
    var myOrdersAdapter: BasePagedListAdapter<OrderListItemViewModel>? =
        BasePagedListAdapter<OrderListItemViewModel>(object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {


                if (id == R.id.iv_favorite_icon) {


                    if (appRepository.isUserLoggedIn()) {
                        myOrdersList.value?.get(position)?.car?.isFavorite =
                            myOrdersList.value?.get(position)?.isFavoriteCar?.get()?.not()!!
                        myOrdersList.value?.get(position)?.isFavoriteCar?.set(
                            myOrdersList.value?.get(position)?.isFavoriteCar?.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(myOrdersList.value?.get(position)?.car?.id!!),
                            APPConstants.FAVORITE_CAR_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        myOrdersList.value?.get(position)?.isFavoriteCar?.get()!!
                    )


                } else if (id == R.id.iv_share_icon) {
                    launchShareIntent(
                        myOrdersList.value?.get(position)?.car?.name,
                        myOrdersList.value?.get(position)?.car?.shareLink
                    )
                } else {

                    val extras = Bundle()
                    extras.putSerializable(ORDER_NOW_CAR_ORDER,  myOrdersList.value?.get(position)?.order)
                    openView(AppScreenRoute.ORDER_DETAILS, extras)

                }
            }
        })

    // adapter's lists
    lateinit var myOrdersList: LiveData<PagedList<OrderListItemViewModel>>
    private var list: PagedList<OrderListItemViewModel>? = null
    var isRefreshing = ObservableBoolean(false)

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var myOrdersOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL


    override fun onViewCreated() {
        super.onViewCreated()
        getCars()

        myOrdersList.observe(lifecycleOwner!!, Observer {
            list?.clear()
            list?.addAll(it)
            myOrdersAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }
            isRefreshing.set(false)

        })

    }

    override fun onViewRecreated() {
        super.onViewRecreated()

    }


    private fun getCars(): LiveData<PagedList<OrderListItemViewModel>> {

        ordersDataSourceFactory = MyOrdersDataSourceFactory(
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
        myOrdersList = LivePagedListBuilder(
            ordersDataSourceFactory,
            config
        ).build() as LiveData<PagedList<OrderListItemViewModel>>
        return myOrdersList

    }

    protected fun initMyOrdersView() {
        myOrdersAdapter = BasePagedListAdapter(object : ViewHolderInterface {

            override fun onViewClicked(position: Int, id: Int) {

            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getCars()
        }
    }


}