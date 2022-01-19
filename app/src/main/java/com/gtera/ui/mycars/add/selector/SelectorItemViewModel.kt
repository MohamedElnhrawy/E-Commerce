package com.gtera.ui.mycars.add.selector

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.SelectorOption
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.SelectorType
import com.example.famousapp.famous.utils.display.ScreenUtils
import java.io.Serializable
import javax.inject.Inject

class SelectorItemViewModel @Inject constructor() : BaseItemViewModel(), Serializable {


    var selectorOption: SelectorOption? = null
    var selectorType: SelectorType? = null
    var itemSize = ObservableField<Int>()
    var itemName = ObservableField("")
    var itemValue = ObservableField("")
    var itemIcon: Drawable? = null
    var isSelected = ObservableBoolean(false)


    constructor(selectorOption: SelectorOption, selectorType: SelectorType? , context:Context ) : this() {

        this.selectorType = selectorType
        this.itemName.set(selectorOption.name)
        this.itemValue.set(selectorOption.value)
        this.selectorOption = selectorOption
        val screenWidth: Int = ScreenUtils.getScreenWidth(context) * 95 / 100
        var itemWidth = 0
        if (selectorType?.equals(SelectorType.MODEL)!!)
            itemWidth = (screenWidth / 3.6).toInt()
        else
            itemWidth = (screenWidth / 5.5).toInt()
        itemSize.set(itemWidth)
    }



    override val layoutId: Int
        get() = when (selectorType) {
            SelectorType.BRAND, SelectorType.MODEL -> R.layout.selector_first_type_layout
            SelectorType.YEAR -> R.layout.selector_second_type_layout
            else -> R.layout.selector_first_type_layout
        }

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


}
