package com.gtera.ui.home.viewmodels

import android.os.Bundle
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.data.model.Category
import com.gtera.data.model.Product
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.HomeViewModel
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.util.ArrayList
import javax.inject.Inject

class CategoryItemViewModel @Inject constructor() : BaseItemViewModel() {

    var viewModel: HomeViewModel? = null
    var productsAdapter: BaseAdapter<ProductItemViewModel>? = null
    var productsList: ArrayList<ProductItemViewModel> = ArrayList<ProductItemViewModel>()
    var productsOrientation: ListOrientation = ListOrientation.ORIENTATION_HORIZONTAL


    lateinit var resourceProvider:ResourceProvider
    var categoryId = ObservableField(0)
    var categoryName = ObservableField("")
    var category: Category? = null
    override val layoutId: Int
        get() = R.layout.home_category_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }



    constructor(category: Category,viewModel:HomeViewModel,resourceProvider: ResourceProvider) : this() {
        this.category = category
        this.viewModel = viewModel
        this.resourceProvider = resourceProvider
        initProducts()
        initView()
    }


    fun initView() {
        categoryId.set(category?.id)
        categoryName.set(category?.name)
        addProducts(category?.products!!)
    }

    private fun addProducts(list: List<Product?>) {
        if (Utilities.isNullList(list)) return
        this.productsList.clear()
        for (product in list) {
            val productItemViewModel = ProductItemViewModel(product!!,productsOrientation,resourceProvider)

            this.productsList.add(productItemViewModel)
        }
        productsAdapter?.notifyDataSetChanged()
    }

    private fun initProducts() {
        productsAdapter = BaseAdapter(productsList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {
                val extras = Bundle()
                extras.putSerializable(
                    APPConstants.EXTRAS_KEY_PRODUCT,
                    productsList[position].product
                )
                viewModel?.openView(AppScreenRoute.PRODUCT_DETAILS_SCREEN,extras)
            }
        })


    }
    open  fun onMoreClick(){
        val extras = Bundle()
        extras.putSerializable(APPConstants.EXTRAS_KEY_CATEGORY,category)
        viewModel?.openView(AppScreenRoute.SEARCH_VIEW,extras)
    }
}