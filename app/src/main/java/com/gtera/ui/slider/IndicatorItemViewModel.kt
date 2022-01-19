package com.gtera.ui.slider

import androidx.databinding.ObservableBoolean
import com.gtera.R
import com.gtera.ui.base.BaseItemViewModel

class IndicatorItemViewModel(isSelected: Boolean) :
    BaseItemViewModel() {
    @JvmField
    var isSelected = ObservableBoolean(false)
     override val layoutId: Int
         get() =  R.layout.bottom_indicator_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    init {
        this.isSelected.set(isSelected)
    }
}