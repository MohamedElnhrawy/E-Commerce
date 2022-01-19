package com.gtera.ui.choosebudget.viewmodels

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Brand
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class BudgetBrandItemViewModel @Inject constructor() :
    BaseItemViewModel() {

    var brand: Brand? = null
    var isSelected = ObservableField(false)
    var brandImage = ObservableField("")
    var brandName = ObservableField("")
    var brandCount = ObservableField("")

    constructor(
        brand: Brand
    ) : this() {

        this.brand = brand
        this.brandName.set(brand.name)
        this.brandCount.set("")
        this.brandImage.set(brand.image)

    }

    override val layoutId: Int
        get() = R.layout.budget_brand_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


}