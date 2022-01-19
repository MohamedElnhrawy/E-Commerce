package com.gtera.ui.profile.info

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class ProfileInfoItemViewModel @Inject constructor() : BaseItemViewModel() {


    var itemTitle = ObservableField("")
    var itemValue = ObservableField("")


    constructor(
        title: String, value: String
    ) : this() {
        this.itemTitle.set(title)
        this.itemValue.set(value)

    }

    override val layoutId: Int
        get() = R.layout.profile_info_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
}