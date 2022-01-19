package com.gtera.ui.ordernow

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.ui.base.BaseItemViewModel

class PowerAttorneyItem(powerAttorneyName: String) : BaseItemViewModel() {

    var powerAttorneyName = ObservableField(powerAttorneyName)
    override val layoutId: Int
        get() = R.layout.attorny_item_layout


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

}
