package com.gtera.ui.cardetials.attributes

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Attribute
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.utils.APPConstants
import javax.inject.Inject

class CarAttributesItemViewModel @Inject constructor() :
    BaseItemViewModel() {

    var  attribute: Attribute? = null
    var isSelected = ObservableField(false)
    var isEnabled = ObservableField(false)
    var isValue = ObservableField(false)
    var isInput = ObservableField(false)
    var attributeName = ObservableField("")
    var attributeValue = ObservableField("")

    constructor(
        attribute: Attribute,
        enabled:Boolean
    ) : this() {

        this.attribute=attribute
        this.isEnabled.set(enabled)
        this.attributeName.set(attribute.name.toString())
        this.attributeValue.set(attribute.value)



        if (attribute.type.equals(APPConstants.ATTRIBUTE_VALUE_BOOLEAN)) {
            isValue.set(false)
            attribute.value.let {  isSelected.set(it?.equals("1"))}
        } else if(attribute.type.equals(APPConstants.ATTRIBUTE_VALUE_INT)){
            isValue.set(true)
            if(enabled){
                attributeValue.set("")
            }
        }


    }

    override val layoutId: Int
        get() = R.layout.car_details_attribute_list_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


}