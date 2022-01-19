package com.gtera.ui.profile.favorites

import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Favourite
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.FavoriteType
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.base.TabLayoutListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import java.util.*
import javax.inject.Inject

class FavoritesViewModel @Inject constructor() : BaseViewModel<FavoritesNavigator>(),
    ViewHolderInterface {


    init {

        initFavoritesView()
    }

    var isRefreshing = ObservableBoolean(false)
    var hasNewCarFavorite = ObservableBoolean(false)
    var hasUsedCarFavorite = ObservableBoolean(false)
    var hasEmptyFavorite = ObservableBoolean(false)
    var onRefreshListener: OnRefreshListener? = null

    var usedCarsFavoritesOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL
    var newCarsFavoritesOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL
    var tabList = ObservableArrayList<String>()
    var tabSelectedPos = ObservableInt(0)
    var currentTabPos = ObservableInt(0)

    var newCarsFavoritesAdapter: BaseAdapter<FavoriteItemViewModel>? = null
    var usedCarsFavoritesAdapter: BaseAdapter<FavoriteItemViewModel>? = null
    private val newCarsFavorites: ArrayList<Favourite> = ArrayList<Favourite>()
    private val usedCarsFavorites: ArrayList<Favourite> = ArrayList<Favourite>()
    val newCarsFavoritesList: ArrayList<FavoriteItemViewModel> =
        ArrayList<FavoriteItemViewModel>()
    val usedCarsFavoritesList: ArrayList<FavoriteItemViewModel> =
        ArrayList<FavoriteItemViewModel>()

    fun onTabClick(position: Int) {
        if (position == currentTabPos.get()) return
        currentTabPos.set(position)
        tabSelectedPos.set(position)
        getItemsList()
    }

    var tabListener: TabLayoutListener =
        object : TabLayoutListener {
            override fun tabSelected(position: Int) {
                onTabClick(position)
            }
        }

    constructor(activityClass: Class<*>?) : this() {

    }

    override fun onViewCreated() {
        super.onViewCreated()
        setupTab()
        getFavoritesList()
    }

    private fun setupTab() {

        tabList.clear()
        tabList.add(getStringResource(R.string.str_empty_favorite_new_cars))
        tabList.add(getStringResource(R.string.str_empty_favorite_used_cars))

    }

    protected fun getItemsList() {
        when (currentTabPos.get()) {
            0 -> getNewCarsFavoritesList()
            1 -> getUsedCarsFavoritesList()
        }
    }

    private fun getFavoritesList() {

        showLoading(false)
        isRefreshing.set(true)
        appRepository.getFavorites(lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<List<Favourite?>?>?> {
                override fun onSuccess(result: BaseResponse<List<Favourite?>?>?) {

                    hideLoading()
                    isRefreshing.set(false)
                    usedCarsFavorites.clear()
                    newCarsFavorites.clear()
                    result?.data?.forEach {

                        if (it?.itemType.equals(FavoriteType.CAR.toString().toLowerCase())) {

                            if (it?.car != null && it?.car?.used!!)
                                usedCarsFavorites.add(it)
                            else
                                newCarsFavorites.add(it!!)

                        } else {

                            newCarsFavorites.add(it!!)
                        }
                    }

                    getItemsList()
                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    isRefreshing.set(false)
                    showErrorBanner(throwable?.errorMsg)
                }
            })
    }

    private fun getNewCarsFavoritesList() {
        hasNewCarFavorite.set(true)
        newCarsFavoritesList.clear()
        newCarsFavorites.forEach {

            if (it.car != null) {

                newCarsFavoritesList.add(
                    FavoriteItemViewModel(
                        it,
                        resourceProvider,
                        FavoriteType.NEW_CAR
                    )
                )
            } else if (it.offer != null) {

                if (it.offer.hotDeal!!) {

                    newCarsFavoritesList.add(
                        FavoriteItemViewModel(
                            it,
                            resourceProvider,
                            FavoriteType.TOP_DEAL
                        )
                    )
                } else {

                    newCarsFavoritesList.add(
                        FavoriteItemViewModel(
                            it,
                            resourceProvider,
                            FavoriteType.OFFER
                        )
                    )
                }


            }

        }
        newCarsFavoritesAdapter?.updateList(newCarsFavoritesList)
        newCarsFavoritesAdapter?.notifyDataSetChanged()
        if( newCarsFavoritesList.size>0) hasNewCarFavorite.set(true) else hasNewCarFavorite.set(false)
        if(tabSelectedPos.get() == 0 && hasNewCarFavorite.get()){
            hasEmptyFavorite.set(false)
            hasUsedCarFavorite.set(false)
        } else hasEmptyFavorite.set(true)

    }

    private fun getUsedCarsFavoritesList() {

        hasUsedCarFavorite.set(true)
        usedCarsFavoritesList.clear()
        usedCarsFavorites.forEach {
            usedCarsFavoritesList.add(
                FavoriteItemViewModel(
                    it,
                    resourceProvider,
                    FavoriteType.USED_CAR
                )
            )
        }
        usedCarsFavoritesAdapter?.updateList(usedCarsFavoritesList)
        usedCarsFavoritesAdapter?.notifyDataSetChanged()
        if(usedCarsFavoritesList.size>0) hasUsedCarFavorite.set(true) else hasUsedCarFavorite.set(false)
        if(tabSelectedPos.get() == 1 && hasUsedCarFavorite.get()) {
            hasEmptyFavorite.set(false)
            hasNewCarFavorite.set(false)
        }else hasEmptyFavorite.set(true)

    }


    override fun onViewClicked(position: Int, id: Int) {
        when (currentTabPos.get()) {
            0 -> onNewCarsClicked(position, id)
            1 -> onUsedClicked(position, id)
        }
    }

    private fun onUsedClicked(position: Int, id: Int) {
        if (id == R.id.iv_share_icon){
            launchShareIntent(usedCarsFavorites[position].car?.name,usedCarsFavorites[position].car?.shareLink)

        }
    }

    private fun onNewCarsClicked(position: Int, id: Int) {

        if (id == R.id.iv_share_icon){
            launchShareIntent(newCarsFavorites[position].car?.name,newCarsFavorites[position].car?.shareLink)

        }
    }

    protected fun initFavoritesView() {
        newCarsFavoritesAdapter = BaseAdapter(newCarsFavoritesList, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {

                if (id == R.id.iv_favorite_icon) {


                    if (appRepository.isUserLoggedIn()) {
                        newCarsFavoritesList.get(position).favourite?.car?.isFavorite =
                            newCarsFavoritesList.get(position).isFavoriteCar.get()?.not()!!
                        newCarsFavoritesList.get(position).isFavoriteCar.set(
                            newCarsFavoritesList.get(
                                position
                            ).isFavoriteCar.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(
                                if (newCarsFavoritesList[position].favourite?.car
                                    != null
                                ) newCarsFavoritesList[position].favourite?.car?.id!! else newCarsFavoritesList.get(
                                    position
                                ).favourite?.offer?.car?.id!!
                            ),
                            if (newCarsFavoritesList[position].favourite?.car
                                != null
                            ) APPConstants.FAVORITE_CAR_TYPE else APPConstants.FAVORITE_OFFER_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        newCarsFavoritesList[position].isFavoriteCar.get()!!
                    )


                } else if (id == R.id.iv_share_icon) {
                    launchShareIntent(
                        newCarsFavoritesList[position].favourite?.car?.name,
                        newCarsFavoritesList[position].favourite?.car?.shareLink
                    )
                }else{

                    val extras = Bundle()
                    newCarsFavoritesList[position].favourite?.car?.id?.let {
                        extras.putInt(
                            APPConstants.EXTRAS_KEY_CAR_ID,
                            it
                        )
                    }

                    openView(AppScreenRoute.CAR_DETAILS, extras)
                }
            }

        })
        usedCarsFavoritesAdapter = BaseAdapter(usedCarsFavoritesList, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {

                if (id == R.id.iv_favorite_icon) {


                    if (appRepository.isUserLoggedIn()) {
                        usedCarsFavoritesList[position].favourite?.car?.isFavorite =
                            usedCarsFavoritesList[position].isFavoriteCar.get()?.not()!!
                        usedCarsFavoritesList[position].isFavoriteCar.set(
                            usedCarsFavoritesList[position].isFavoriteCar.get()?.not()
                        )
                    }

                    addOrRemoveFavorite(
                        FavoritesRequest(
                            arrayListOf(usedCarsFavoritesList[position].favourite?.car?.id!!),
                            APPConstants.FAVORITE_CAR_TYPE
                        ),
                        getGoToSignInConfirmationNavigator()!!,
                        usedCarsFavoritesList[position].isFavoriteCar.get()!!
                    )


                } else if (id == R.id.iv_share_icon) {
                    launchShareIntent(
                        usedCarsFavoritesList[position].favourite?.car?.name,
                        usedCarsFavoritesList[position].favourite?.car?.shareLink
                    )
                }else{

                    val extras = Bundle()
                    usedCarsFavoritesList[position].favourite?.car?.id?.let {
                        extras.putInt(APPConstants.EXTRAS_KEY_CAR_ID, it)
                        usedCarsFavoritesList[position].favourite?.car?.let {
                            extras.putString(
                                APPConstants.EXTRAS_KEY_CAR_NAME,
                                usedCarsFavoritesList[position].favourite?.car?.name + " " +
                                        usedCarsFavoritesList[position].favourite?.car?.brand.let { "${it?.name}" } + " " +
                                        usedCarsFavoritesList[position].favourite?.car?.manufactureYear
                            )
                        }
                    }

                        openView(AppScreenRoute.USED_CAR_DETAILS, extras)
                }

            }

        })

        onRefreshListener = OnRefreshListener {
            isRefreshing.set(true)
            getFavoritesList()
        }
    }

    fun addToFavorite() {

        openView(AppScreenRoute.MAIN_SCREEN, null)
    }
}