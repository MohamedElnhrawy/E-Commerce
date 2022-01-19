package com.gtera.ui.mycars.carcompare.result

import android.content.Context
import android.text.TextUtils
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.CarCompareItemType
import com.example.famousapp.famous.utils.display.ScreenUtils
import java.util.*
import javax.inject.Inject

class CarCompareDetailsItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = if (type?.equals(CarCompareItemType.IMAGE)!!) R.layout.car_compare_details_item_list_image_row_layout
        else if (type?.equals(CarCompareItemType.NAME)!!) R.layout.car_compare_details_item_list_header_row_layout
        else R.layout.car_compare_details_item_list_text_row_layout


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var carImageUrl = ObservableField("")
    var carName = ObservableField("")
    var attributeName = ObservableField("")
    var attributeValue = ObservableField("")
    var type: CarCompareItemType? = null

    var itemSize = ObservableField<Int>()

    constructor(
        carImage: String,
        carName: String,
        type: CarCompareItemType,
        context: Context,
        compareTwo: Boolean
    ) : this() {
        setItemSize(context, compareTwo)

        this.carImageUrl.set(carImage)
        this.carName.set(carName)
        this.type = type
    }

    constructor(
        attributeNameValue: String,
        type: CarCompareItemType,
        context: Context,
        compareTwo: Boolean
    ) : this(attributeNameValue, type) {
        setItemSize(context, compareTwo)
    }

    constructor(attributeNameValue: String, type: CarCompareItemType) : this() {
        this.type = type
        if (Objects.equals(type, CarCompareItemType.NAME)) {
            this.attributeName.set(attributeNameValue)
        } else {
            if (TextUtils.isEmpty(attributeNameValue))
                this.attributeValue.set("--")
            else
                this.attributeValue.set(attributeNameValue)
        }
    }

    private fun setItemSize(context: Context, compareTwo: Boolean) {
        val screenWidth: Int = ScreenUtils.getScreenWidth(context) * 95 / 100
        val itemWidth = if (compareTwo)
            screenWidth / 2
        else
            (screenWidth / 2.2).toInt()
        itemSize.set(itemWidth)
    }

}