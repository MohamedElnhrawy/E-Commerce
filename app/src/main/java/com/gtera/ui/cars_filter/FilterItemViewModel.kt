package com.gtera.ui.cars_filter

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Color
import com.gtera.data.model.FiltrationOption
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.FilterType
import com.example.famousapp.famous.utils.display.ScreenUtils
import javax.inject.Inject

class FilterItemViewModel @Inject constructor() : BaseItemViewModel() {

    private var filterType: FilterType? = FilterType.FILTER_TYPE_DEFAULT

    var itemSize = ObservableField<Int>()
    var filtrationOption: FiltrationOption? = null
    var code = ObservableField("")
    var itemName = ObservableField("")
    var itemValue = ObservableField("")
    var itemIcon: Drawable? = null
    var isSelected = ObservableBoolean(false)

    constructor(color: Color) : this() {
        code.set(color.code)
        this.filterType = FilterType.FILTER_TYPE_HORIZONTAL
    }

    constructor(filtrationOption: FiltrationOption) : this(
        filtrationOption, FilterType.FILTER_TYPE_DEFAULT
    )

    constructor(filtrationOption: FiltrationOption, filterType: FilterType?) : this() {
        this.itemName.set(filtrationOption.name)
            this.code.set(filtrationOption.value)
            this.itemValue.set(filtrationOption.value)

        this.filterType = filterType
    }

    constructor(
        filtrationOption: FiltrationOption,
        filterType: FilterType?,
        context: Context
    ) : this(
        filtrationOption, filterType
    ) {
        this.filtrationOption = filtrationOption
        val screenWidth: Int = ScreenUtils.getScreenWidth(context) * 95 / 100
        var itemWidth = 0
        if (filterType?.equals(FilterType.FILTER_TYPE_HORIZONTAL_COLOR)!!)
            itemWidth = (screenWidth / 8).toInt()
        else
            itemWidth = (screenWidth / 3.6).toInt()
        itemSize.set(itemWidth)
    }

    constructor(
        @DrawableRes icon: Int,
        @StringRes title: Int,
        resourceProvider: ResourceProvider
    ) : this() {
        if (icon != 0)
            itemIcon = resourceProvider.getDrawable(icon)
        itemName.set(resourceProvider.getString(title))
    }

    fun onItemSelection() {
        if (isSelected.get())
            itemSelected()
        else
            itemUnselected()
    }

    fun itemSelected() {


        if (filterType != FilterType.FILTER_TYPE_HORIZONTAL_COLOR)
            code.set("#ffffff")
        isSelected.set(true)
    }

    fun itemUnselected() {
        if (filterType != FilterType.FILTER_TYPE_HORIZONTAL_COLOR)
            code.set("")
        isSelected.set(false)
    }

    override val layoutId: Int
        get() = when (filterType) {
            FilterType.FILTER_TYPE_HORIZONTAL_COLOR -> R.layout.filter_item_color_layout
            FilterType.FILTER_TYPE_GRID -> R.layout.filter_item_grid_layout
            FilterType.FILTER_TYPE_HORIZONTAL -> R.layout.filter_item_horizontal_layout
            else -> R.layout.filter_item_brand_layout
        }

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


}
