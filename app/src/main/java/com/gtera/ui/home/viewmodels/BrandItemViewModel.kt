package com.gtera.ui.home.viewmodels

import android.content.Context
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Brand
import com.gtera.ui.base.BaseItemViewModel
import com.example.famousapp.famous.utils.display.ScreenUtils
import javax.inject.Inject

class BrandItemViewModel @Inject constructor() : BaseItemViewModel() {

    var itemSize = ObservableField<Int>()
    var imageUrl = ObservableField("")
    var brandId = ObservableField(0)
    var brandName = ObservableField("")
    var isSelected = ObservableField(false)
    var brand: Brand? = null
    lateinit var context:Context
    override val layoutId: Int
        get() = R.layout.brand_item_grid_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    override fun filter(searchKey: String): Boolean {
        return brand!!.name!!.toLowerCase().contains(searchKey.toLowerCase())
    }

    constructor(brand: Brand, context: Context) : this() {
        this.brand = brand
        this.context = context
        initView(brand,false)
    }

    constructor(brand: Brand, context: Context, horizontalScroll: Boolean) : this() {
        this.brand = brand
        this.context = context
        initView(brand,horizontalScroll)
    }

    fun initView(brand: Brand, horizontalScroll: Boolean) {
        val screenWidth: Int
        val itemWidth: Int
        if (horizontalScroll) {
            screenWidth = ScreenUtils.getScreenWidth(context) * 95 / 100
            itemWidth = ((screenWidth / 3.6).toInt())
        } else {
            screenWidth = ScreenUtils.getScreenWidth(context) * 95 / 100
            itemWidth = (screenWidth / 3)
        }
        imageUrl.set(brand.image)
        brandId.set(brand.id)
        brandName.set(brand.name)
        itemSize.set(itemWidth)
    }
}