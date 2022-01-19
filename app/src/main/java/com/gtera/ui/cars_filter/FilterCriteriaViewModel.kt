package com.gtera.ui.cars_filter

import android.content.Context
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import com.gtera.R
import com.gtera.data.model.AttributeGroup
import com.gtera.data.model.FiltrationCriteria
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.FilterType
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.base.SpinnerInterface
import com.gtera.ui.cardetials.attributes.CarAttributesItemListViewModel
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FilterCriteriaViewModel @Inject constructor() : BaseItemViewModel() {

    var filterCriteriaViewType: FilterType? = null
    var filterCriteria: FiltrationCriteria? = null
    var criteriaName = ObservableField("")
    var criteriaFrom = ObservableField("")
    var criteriaTo = ObservableField("")
    var viewOrientation: ListOrientation? = null
    var criteriaList: ArrayList<FilterItemViewModel> = ArrayList()
    var criteriaListAdapter: BaseAdapter<FilterItemViewModel>? = null
    var criteriaAttributesAdapter: BaseAdapter<CarAttributesItemListViewModel>? = null
    var attributesGroupList: ArrayList<CarAttributesItemListViewModel> =
       ArrayList<CarAttributesItemListViewModel>()

    var isexpanded = ObservableField(false)
    var isHorizontalGrid = ObservableField(false)
    var spanCount = ObservableField(0)
    var expandCollapseClick =
        View.OnClickListener { v: View? ->
            isexpanded.set(isexpanded.get()?.not())
        }


    var resourceProvider: ResourceProvider? = null

    var rangMin = ObservableField("")
    var rangMax = ObservableField("")

    var minMaxStart = ObservableField("")
    var minMaxEnd = ObservableField("")

    var initMinRang: ObservableFloat = ObservableFloat()
    var initMaxRang: ObservableFloat = ObservableFloat()

    private var minRangValue: Float = 0f
    private var maxRangValue: Float = 500f

    var dropDownNames = ObservableArrayList<String>()
    var dropDownSelectedIndex = ObservableInt(0)

    var dropDownListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < dropDownNames.size && dropDownSelectedIndex.get() != position)
                onDropDownItemSelected(position)
        }
    }

    var selectedListItems = ArrayList<Int>()
    var rangesItems = ArrayList<String>()

    constructor(
        filterCriteria: FiltrationCriteria?,
        listener: ViewHolderInterface,
        context: Context,
        resourceProvider: ResourceProvider
    ) : this(
    ) {
        this.filterCriteria = filterCriteria
        this.resourceProvider = resourceProvider
        criteriaName.set(filterCriteria!!.name)
        filterCriteriaViewType = getFilterViewType(filterCriteria.viewType)

        viewOrientation = when (filterCriteriaViewType) {
            FilterType.FILTER_TYPE_GRID -> ListOrientation.ORIENTATION_GRID
            FilterType.FILTER_TYPE_HORIZONTAL -> ListOrientation.ORIENTATION_HORIZONTAL
            FilterType.FILTER_TYPE_ATTRIBUTES -> ListOrientation.ORIENTATION_VERTICAL
            else -> null
        }

        if (Objects.equals(filterCriteria.key, APPConstants.FILTER_OPTION_COLORS)) {
            spanCount.set(1)
            isHorizontalGrid.set(true)
            viewOrientation = ListOrientation.ORIENTATION_GRID
            filterCriteriaViewType = FilterType.FILTER_TYPE_HORIZONTAL_COLOR
        }

        if (filterCriteriaViewType == FilterType.FILTER_TYPE_RANG || filterCriteriaViewType == FilterType.FILTER_TYPE_MIN_MAX) {

            if (!Utilities.isNullList(filterCriteria.items) && filterCriteria.items!!.size == 2) {


                minMaxStart.set(filterCriteria.items!![0].value!!)
                minMaxEnd.set(filterCriteria.items!![1].value!!)

                var minPrice: Float = filterCriteria.items!![0].value!!.toFloat()
                var maxPrice: Float = filterCriteria.items!![1].value!!.toFloat()


                criteriaFrom.set(filterCriteria.items!![0].name!!)
                criteriaTo.set(filterCriteria.items!![1].name!!)


                if (minPrice > maxPrice) {
                    val temp = minPrice
                    minPrice = maxPrice
                    maxPrice = temp
                }
                initMinRang.set(minPrice)
                initMaxRang.set(maxPrice)
                setRangValues(minPrice, maxPrice)

            }

        } else if (filterCriteriaViewType == FilterType.FILTER_TYPE_DROWN_DOWN) {

            for (item in filterCriteria.items!!) {

                dropDownNames.add(item.name)
            }
            selectedListItems.clear()
            selectedListItems.add(filterCriteria.items?.get(0)?.id!!)

        } else if (filterCriteriaViewType == FilterType.FILTER_TYPE_ATTRIBUTES) {

            for (filterOption in filterCriteria.items!!) {
                var attributeGroup = AttributeGroup()
                attributeGroup.name = filterOption.name
                attributeGroup.attributes= filterOption.attributes
                attributesGroupList.add(

                    CarAttributesItemListViewModel(attributeGroup,
                        true
                    )
                )
            }

            criteriaAttributesAdapter =
                BaseAdapter(attributesGroupList, object : ViewHolderInterface {


                    override fun onViewClicked(position: Int, id: Int) {

                        handleListSelection(position)

                    }
                })

        } else {

            if (filterCriteriaViewType == FilterType.FILTER_TYPE_GRID) {
                isHorizontalGrid.set(true)
                        when(filterCriteria.items?.size!!){
                            0,1,2,3 ->  spanCount.set(1)
                            4,5,6->  spanCount.set(2)
                            else -> spanCount.set(3)

                        }

            }


            for (filterOption in filterCriteria.items!!) {
                criteriaList.add(FilterItemViewModel(filterOption, filterCriteriaViewType, context))
            }

            criteriaListAdapter = BaseAdapter(criteriaList, object : ViewHolderInterface {
                override fun onViewClicked(position: Int, id: Int) {

                    criteriaList[position].onItemSelection()


                    // for selection
                    handleListSelection(position)
                }
            })

        }
    }

    private fun handleListSelection(position: Int) {

        when (filterCriteriaViewType) {
            FilterType.FILTER_TYPE_HORIZONTAL_COLOR,
            FilterType.FILTER_TYPE_HORIZONTAL,
            FilterType.FILTER_TYPE_GRID -> {

                if (selectedListItems.contains(criteriaList[position].filtrationOption?.id)) {

                    selectedListItems.removeAll({ it == criteriaList[position].filtrationOption?.id })
                    criteriaList[position].isSelected.set(false)
                } else {
                    criteriaList[position].filtrationOption?.id?.let { selectedListItems.add(it) }
                    criteriaList[position].isSelected.set(true)
                }


            }
            FilterType.FILTER_TYPE_ATTRIBUTES -> {

                if (selectedListItems.containsAll(attributesGroupList[position].selectedIds)) {

                    selectedListItems.removeAll(attributesGroupList[position].selectedIds)

                } else {
                    attributesGroupList[position].selectedIds.let { selectedListItems.addAll(it) }

                }
            }
            FilterType.FILTER_TYPE_DROWN_DOWN -> {

                selectedListItems.clear()
                criteriaList[position].filtrationOption?.id?.let { selectedListItems.add(it) }
            }
        }
    }

    fun onSeekBarChange(minValue: Number, maxValue: Number) {
        setRangValues(minValue.toFloat(), maxValue.toFloat())
    }

    private fun setRangValues(minValue: Float, maxValue: Float) {
        minRangValue = minValue
        maxRangValue = maxValue

        rangMin.set(minRangValue.toString())
        rangMax.set(maxRangValue.toString())
    }

    private fun getFilterViewType(key: String): FilterType? {
        return when (key) {
            FilterType.FILTER_TYPE_RANG.type -> FilterType.FILTER_TYPE_RANG
            FilterType.FILTER_TYPE_HORIZONTAL.type -> FilterType.FILTER_TYPE_HORIZONTAL
            FilterType.FILTER_TYPE_GRID.type -> FilterType.FILTER_TYPE_GRID
            FilterType.FILTER_TYPE_ATTRIBUTES.type -> FilterType.FILTER_TYPE_ATTRIBUTES
            FilterType.FILTER_TYPE_MIN_MAX.type -> FilterType.FILTER_TYPE_MIN_MAX
            else -> FilterType.FILTER_TYPE_DROWN_DOWN
        }
    }

    override val layoutId: Int
        get() = when (filterCriteriaViewType) {
            FilterType.FILTER_TYPE_GRID, FilterType.FILTER_TYPE_HORIZONTAL -> R.layout.filter_critaria_list_layout
            FilterType.FILTER_TYPE_HORIZONTAL_COLOR -> R.layout.filter_critaria_list_layout
            FilterType.FILTER_TYPE_ATTRIBUTES -> R.layout.filter_critaria_attributes_list_layout
            FilterType.FILTER_TYPE_MIN_MAX -> R.layout.filter_critaria_min_max_layout
            FilterType.FILTER_TYPE_RANG -> R.layout.filter_critaria_rang_layout
            else -> R.layout.filter_critaria_dropdown_layout
        }

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


    protected fun onDropDownItemSelected(position: Int) {
        dropDownSelectedIndex.set(position)
        selectedListItems.clear()
        selectedListItems.add(filterCriteria?.items?.get(position)?.id!!)

    }
}
