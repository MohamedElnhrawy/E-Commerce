package com.gtera.ui.dialog.bottomsheet

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class BottomSheetDialogItemViewModel @Inject constructor() :
    BaseItemViewModel() {

    var isSelected = ObservableField(false)
    var itemText = ObservableField("")
    var itemIcon: Drawable? = null

    constructor(
        @DrawableRes icon: Int,
        itemText: String,
        selected: Boolean,
        resourceProvider: ResourceProvider
    ) : this() {
        this.itemText.set(itemText)
        if (icon != 0) itemIcon = resourceProvider.getDrawable(icon)
        this.isSelected.set(selected)
    }

    override val layoutId: Int
        get() = R.layout.bottom_sheet_list_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


}