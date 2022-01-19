package com.gtera.ui.mycars

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class MyCarItemViewModel @Inject constructor() : BaseItemViewModel() {

    var itemTitle = ObservableField("")
    var itemDesc = ObservableField("")
    var UserCarCount = ObservableField("")
    var itemIcon: Drawable? = null
    private var titleResource = 0


    init {

    }

    constructor(
        @DrawableRes icon: Int,
        @StringRes title: Int,
        @StringRes description: Int,
        count: String,
        resourceProvider: ResourceProvider
    ) : this() {
        if (icon != 0) itemIcon = resourceProvider.getDrawable(icon)
        titleResource = title
        itemTitle.set(resourceProvider.getString(title))
        itemDesc.set(resourceProvider.getString(description))
        UserCarCount.set(count)



    }

    constructor(
        @DrawableRes icon: Int,
        @StringRes title: Int,
        count: String,
        resourceProvider: ResourceProvider
    ) : this() {
        if (icon != 0) itemIcon = resourceProvider.getDrawable(icon)
        titleResource = title
        itemTitle.set(resourceProvider.getString(title))
        UserCarCount.set(count)

    }

    fun getTitleResource(): Int {
        return titleResource
    }


    override val layoutId: Int
        get() =  R.layout.my_car_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
}