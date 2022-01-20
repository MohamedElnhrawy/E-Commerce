package com.gtera.ui.home

import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.*
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.*
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel<HomeNavigator>() {

    init {
        initHomeViews()
    }


    var welcomeText = ObservableField<String>("")
    var hasCart = ObservableField(false)

    var allCartItemsClick =
        View.OnClickListener { _: View? ->
            openView(AppScreenRoute.CART_SCREEN,null)
        }

    // adapters
    var categoriesAdapter: BaseAdapter<CategoryItemViewModel>? = null
    var cartAdapter: BaseAdapter<CartItemViewModel>? = null

    // adapter's lists
     private var categoriesList: ArrayList<CategoryItemViewModel> = ArrayList()
    var cartList: ArrayList<CartItemViewModel> = ArrayList()

    var isRefreshing = ObservableBoolean(false)

    //scrolling
    var onScrollStartedListener: NestedScrollView.OnScrollChangeListener? = null

    //swipe
    var onRefreshListener: OnRefreshListener? = null


    //Orientation
    var cartOrientation: ListOrientation = ListOrientation.ORIENTATION_HORIZONTAL
    var categoriesOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL


    override fun onViewCreated() {
        super.onViewCreated()
        getHomeContent()
        getLiveCart()


    }

    private fun initCart() {
        cartAdapter = BaseAdapter(cartList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {
                openView(AppScreenRoute.CART_SCREEN,null)
//                val extras = Bundle()
//                extras.putSerializable(APPConstants.EXTRAS_KEY_NEWS, newsList[position].news)
//                openView(AppScreenRoute.HOME_NEWS_DETAILS, extras)
            }
        })
    }

    private fun initCategories() {
        categoriesAdapter = BaseAdapter(categoriesList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {

            }
        })


    }


    private fun getHomeContent() {

        showLoading(false)
        appRepository.getHome(lifecycleOwner, object : APICommunicatorListener<HomeContent?> {

            override fun onSuccess(result: HomeContent?) {
                welcomeText.set(result?.data?.welcome)
                addCategories(result?.data?.categories!!)
                hideLoading()
                isRefreshing.set(false)
                getUserData()
            }

            override fun onError(throwable: ErrorDetails?) {
                hideLoading()
                isRefreshing.set(false)
                showErrorView(
                    throwable?.statusCode!!,
                    throwable.errorMsg,
                    object : ClickListener {
                        override fun onViewClicked() {
                            getHomeContent()
                        }
                    })
            }
        })
    }



    private fun initHomeViews() {
        initCart()
        initCategories()
        onRefreshListener = OnRefreshListener {
            isRefreshing.set(true)
            getHomeContent()
        }
        onScrollStartedListener =
            NestedScrollView.OnScrollChangeListener { _: NestedScrollView?, _: Int, _: Int, _: Int, _: Int ->
                //                navigator.clearSearchViewFocus()
            }
    }

    private fun addCarts(list: List<CartProduct?>) {
//        if (Utilities.isNullList(list)) return
        navigator?.initCartView()
        this.cartList.clear()
      hasCart.set(list.isNotEmpty())
        for (item in list) {
            val cartItemViewModel = item?.let {
                CartItemViewModel(
                    it, resourceProvider, cartOrientation
                )
            }
            cartItemViewModel?.let {
                this.cartList.add(it)
            }
            if (cartList.size == 3)
                break
        }
        cartAdapter?.updateList(cartList)
        cartAdapter?.notifyDataSetChanged()
    }

    private fun addCategories(list: List<Category?>) {
        if (Utilities.isNullList(list)) return

        this.categoriesList.clear()
        for (category in list) {
            val categoryItemViewModel = CategoryItemViewModel(category!!,this,resourceProvider)

            this.categoriesList.add(categoryItemViewModel)
        }
        categoriesAdapter?.updateList(categoriesList)
    }

    private fun getLiveCart(){
        appRepository.getLiveCartList(lifecycleOwner,
            androidx.lifecycle.Observer<List<CartProduct?>?> {
                it?.let { it1 -> addCarts(it1) }
               Log.e("cccccc",it?.size.toString())
            })
    }


    override fun onViewRecreated() {
        super.onViewRecreated()
        if (!Utilities.isNullList(categoriesList))
            navigator?.initCartView()
    }

    fun getUserData(){
        appRepository.getLoggedInUser(lifecycleOwner, object : APICommunicatorListener<User?> {
            override fun onSuccess(result: User?) {
                welcomeText.set(welcomeText.get()+ result?.getWelcomedName())
            }
            override fun onError(throwable: ErrorDetails?) {
            }
        })
    }

}