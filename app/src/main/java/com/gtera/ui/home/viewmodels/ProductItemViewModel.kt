package com.gtera.ui.home.viewmodels

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.data.model.Category
import com.gtera.data.model.Product
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.ListOrientation
import javax.inject.Inject

class ProductItemViewModel @Inject constructor() : BaseItemViewModel() {

    var productId = ObservableField(0)
    var productName = ObservableField("")
    var productImage = ObservableField("")
    var productPrice = ObservableField("")
    var productDescription = ObservableField("")
    lateinit var productsOrientation: ListOrientation
    var product: Product? = null
    var resourceProvider: ResourceProvider? = null
    override val layoutId: Int
        get() = if (productsOrientation == ListOrientation.ORIENTATION_HORIZONTAL || productsOrientation == ListOrientation.ORIENTATION_GRID) R.layout.product_item_horizontal_layout else R.layout.product_item_base_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }



    constructor(product: Product,orientation: ListOrientation,resourceProvider:ResourceProvider) : this() {
        this.product = product
        this.resourceProvider = resourceProvider
        this.productsOrientation=orientation
        initView()
    }


    fun initView() {
        productId.set(product?.id)
        productName.set(product?.name)
        productPrice.set(resourceProvider?.getString(
            R.string.str_egp,
            product?.price?.toString()
        ))
        productImage.set(product?.image)
        productDescription.set(product?.description)
    }


}