package com.gtera.ui.cardetials.attributes

import android.view.View
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Attribute
import com.gtera.data.model.AttributeGroup
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.utils.Utilities
import javax.inject.Inject

class CarAttributesItemListViewModel @Inject constructor() :
    BaseItemViewModel() {


    var isexpanded = ObservableField(false)
    var attributeName = ObservableField("")
    var isEnabled: Boolean = false
    var selectedIds = ArrayList<Int>()


    var expandCollapseClick =
        View.OnClickListener { v: View? ->
            isexpanded.set(isexpanded.get()?.not())
        }


    var attributesGroup: AttributeGroup? = null

    var attributesListAdapter: BaseAdapter<CarAttributesItemViewModel>? = null

    // adapter's lists
    var attributesList: ArrayList<CarAttributesItemViewModel> =
        java.util.ArrayList<CarAttributesItemViewModel>()

    constructor(
        attributes: AttributeGroup,
        isEnabled: Boolean
    ) : this() {
        initAttributeList()
        this.isEnabled = isEnabled
        this.attributesGroup = attributes
        this.attributeName.set(attributes.name)
        attributesGroup?.attributes?.let { addGroupAttributes(it) }


    }

    override val layoutId: Int
        get() = R.layout.car_details_attribute_list_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


    protected fun initAttributeList() {

        attributesListAdapter = BaseAdapter(attributesList, object : ViewHolderInterface {

            override fun onViewClicked(position: Int, id: Int) {

                if (id == R.id.cb_selction) {
                    if (selectedIds.contains(attributesList[position].attribute?.id)) {
                        attributesList[position].isSelected.set(false)
                        selectedIds.removeAll({ it == attributesList[position].attribute?.id })

                    } else {

                        selectedIds.add(attributesList[position].attribute?.id!!)
                        attributesList[position].isSelected.set(true)
                    }

                }
            }
        })
    }

    protected fun addGroupAttributes(List: List<Attribute?>) {
        if (Utilities.isNullList(List)) return


        for (attribute in List) {
            val carAttributesItemViewModel = CarAttributesItemViewModel(
                attribute!!, isEnabled
            )
            this.attributesList.add(carAttributesItemViewModel)
        }
//        attributesListAdapter?.updateList(attributesList)
        attributesListAdapter?.notifyDataSetChanged()
    }


}

