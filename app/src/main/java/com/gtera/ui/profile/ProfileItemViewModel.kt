package com.gtera.ui.profile

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class ProfileItemViewModel @Inject constructor() : BaseItemViewModel() {



    var itemTitle = ObservableField("")
    var count = ObservableField(0)
    var itemIcon: Drawable? = null
    var hasArrow = ObservableBoolean(false)
    var hasNotification = ObservableBoolean(false)
    private var titleResource = 0




    constructor(
        @DrawableRes icon: Int,
        @StringRes title: Int,
        hasNotification: Boolean,
        resourceProvider: ResourceProvider,
        count:Int
    ) : this() {
        if (icon != 0) itemIcon = resourceProvider.getDrawable(icon)
        titleResource = title
        itemTitle.set(resourceProvider.getString(title))
        this.count.set(count)
        this.hasNotification.set(hasNotification)



    }

    constructor(@DrawableRes icon: Int, @StringRes title: Int, resourceProvider:ResourceProvider, hasArrow :Boolean,   count:Int) : this() {
        if (icon != 0) itemIcon = resourceProvider.getDrawable(icon)
        titleResource = title
        this.count.set(count)
        itemTitle.set(resourceProvider.getString(title))
        this.hasArrow.set(hasArrow)
    }

    fun getTitleResource(): Int {
        return titleResource
    }


    override val layoutId: Int
        get() =  R.layout.profile_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
}