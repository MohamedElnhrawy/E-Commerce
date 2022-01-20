package com.gtera.ui.search

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Category
import com.gtera.data.model.Product
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.ProductItemViewModel
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import javax.inject.Inject

class SearchViewModel @Inject constructor() : BaseViewModel<SearchNavigator>() {


    init {
        initSearchView()
    }

    var isRefreshing = ObservableBoolean(false)
    var category: Category? = null
    var searchTextField = ObservableField<String>("")

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null
    var searchListener: SearchActionListener = object : SearchActionListener {
        override fun preformSearch(searchText: String?) {
            searchTextField.set(searchText)
            if (!searchTextField.get().isNullOrEmpty())
                getProducts(searchTextField.get()!!)
        }
    }

    var productsAdapter: BaseAdapter<ProductItemViewModel>? = null
    var productsList: ArrayList<ProductItemViewModel> = ArrayList()
    var productsOrientation: ListOrientation = ListOrientation.ORIENTATION_GRID
    var spanCount = ObservableInt(2)


    override fun onViewCreated() {
        super.onViewCreated()
        category = dataExtras?.getSerializable(APPConstants.EXTRAS_KEY_CATEGORY) as Category?

        category?.let {
            it.name?.let { it1 -> setToolbarSearchText(it1) }
            it.products?.let {
                addProducts(it)
            }
        }
        setToolbarSearchListener(searchListener)

    }

    private fun addProducts(list: List<Product?>?) {
        isRefreshing.set(false)
        if (list != null)
            hasData(list.size)
        else
            hasData(0)
        if (Utilities.isNullList(list)) return
        this.productsList.clear()
        if (list != null) {
            for (product in list) {
                val productItemViewModel =
                    ProductItemViewModel(product!!, productsOrientation, resourceProvider)

                this.productsList.add(productItemViewModel)
            }
        }
        productsAdapter?.updateList(productsList)
    }

    private fun initSearchView() {
        productsAdapter = BaseAdapter(productsList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {
                val extras = Bundle()
                extras.putSerializable(
                    APPConstants.EXTRAS_KEY_PRODUCT,
                    productsList[position].product
                )
                openView(AppScreenRoute.PRODUCT_DETAILS_SCREEN, extras)
            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            if (category != null && searchTextField.get().isNullOrEmpty())
                category!!.products?.let {
                    addProducts(it)
                }
            else {
                isRefreshing.set(false)
                if (!searchTextField.get().isNullOrEmpty())
                    getProducts(searchTextField.get()!!)
            }
        }
    }


    override val hasSearch: Boolean
        get() = true


    fun getProducts(searchText: String) {
        appRepository.searchProducts(searchText,
            lifecycleOwner,
            object :
                APICommunicatorListener<BaseResponse<ArrayList<Product?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Product?>?>?) {
                    hideLoading()
                    isRefreshing.set(false)
                    addProducts(filterList(result?.data).toList())
                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    isRefreshing.set(false)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getProducts(searchText)
                            }
                        })
                }
            })
    }

    fun filterList(list: ArrayList<Product?>?): ArrayList<Product?> {
        val filteredList: ArrayList<Product?> = ArrayList()
        if (list != null) {
            for (item in list) {
                if (searchTextField.get()
                        .let { item?.name!!.toLowerCase().contains(it!!.toLowerCase()) }
                ) {
                    filteredList.add(item)
                }
            }
        }
        return filteredList
    }


}