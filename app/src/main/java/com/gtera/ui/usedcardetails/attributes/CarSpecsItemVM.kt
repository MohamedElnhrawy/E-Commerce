package com.gtera.ui.usedcardetails.attributes

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Color
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class CarSpecsItemVM @Inject constructor() : BaseItemViewModel() {


    var isColor = ObservableBoolean(false)
    var showColorBullet = ObservableBoolean(false)
    var itemTitle = ObservableField("")
    var color = ObservableField<String>("")
    var itemValue = ObservableField("")
    var doorsNumber = ObservableField<String>("")


    constructor(title: String, value: String) : this() {
        this.itemTitle.set(title)
        this.itemValue.set(value)

    }
    constructor(title: String, value: Int) : this() {
        this.itemTitle.set(title)
        this.doorsNumber.set(value.toString())

    }

    constructor(title: String, value: Color?,isColor:Boolean, showColorBullet:Boolean) : this() {
        this.itemTitle.set(title)
        this.itemValue.set(value?.name)
        this.isColor.set(isColor)
        this.showColorBullet.set(showColorBullet)
        value?.code?.let {
            this.color.set(it)
        }


    }

    override val layoutId: Int
        get() = if (isColor.get())
             R.layout.car_spec_color_item_layout
    else
            R.layout.car_spec_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
}