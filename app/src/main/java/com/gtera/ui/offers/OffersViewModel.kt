package com.gtera.ui.offers

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
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.CartItemViewModel
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import javax.inject.Inject

class OffersViewModel @Inject constructor() : BaseViewModel<OffersNavigator>() {

    init {
        initOffers()

    }

    lateinit var offersDataSourceFactory: OffersDataSourceFactory

    var cartAdapter: BasePagedListAdapter<CartItemViewModel>? = null


    // adapter's lists
    lateinit var cartList: LiveData<PagedList<CartItemViewModel>>
    private var list: PagedList<CartItemViewModel>? = null

    var OffersCount = ObservableField("")
//    protected var offersList: ArrayList<OffersItemViewModel> =
//        ArrayList<OffersItemViewModel>()

    var isRefreshing = ObservableBoolean(false)


    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null


    var offersOrientation: ListOrientation = ListOrientation.ORIENTATION_VERTICAL


    override fun onViewCreated() {

        setToolbarFilterActionField(R.drawable.ic_search_filter, null, true)
        super.onViewCreated()
        initDataSource()

    }


    protected fun initOffers() {
        cartAdapter = BasePagedListAdapter( object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {

                if (id == R.id.iv_favorite_icon) {

                    if (appRepository.isUserLoggedIn()) {
                        cartList.value!![position]?.offerItem?.car?.isFavorite =
                            cartList.value!![position]?.isFavoriteCar?.get()?.not()!!
                        cartList.value!![position]?.isFavoriteCar?.set(
                            cartList.value!![position]?.isFavoriteCar?.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(cartList.value!![position]?.offerItem?.car?.id!!),
                            APPConstants.FAVORITE_OFFER_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        cartList.value!![position]?.isFavoriteCar?.get()!!
                    )


                } else {

                    val extras = Bundle()
                    cartList.value!![position]?.offerItem?.let { offer ->
                        extras.putInt(APPConstants.EXTRAS_KEY_OFFER_ID, offer.id!!)
                        extras.putString(APPConstants.EXTRAS_KEY_CAR_TYPE, APPConstants.SLIDER_TYPE_OFFER)

                        cartList.value!![position]?.offerItem?.let {
                            extras.putString(
                                APPConstants.EXTRAS_KEY_CAR_NAME,
                                it.name + " " +
                                        it.car?.brand?.name + " " +
                                        it.car?.manufactureYear
                            )
                        }
                    }

                    openView(AppScreenRoute.CAR_DETAILS, extras)
                }


            }
        })

    onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefreshing.set(true)
        isLoading.set(true)
        offersDataSourceFactory.brandsLiveDataSource.value?.invalidate()
    }


    }

    private fun initDataSource() {

        offersDataSourceFactory = OffersDataSourceFactory(
            this,
           offersOrientation
        )
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .setPageSize(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .build()

        cartList = LivePagedListBuilder(
            offersDataSourceFactory,
            config
        ).build() as LiveData<PagedList<CartItemViewModel>>

        cartList.observe(lifecycleOwner!!, Observer {
            list?.clear()
            list?.addAll(it)
            cartAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }
            isRefreshing.set(false)
        })

    }


}