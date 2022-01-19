package com.gtera.ui.home.viewmodels

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.TopDeal
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.ListOrientation
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class TotDealsItemViewModel @Inject constructor() : BaseItemViewModel() {

    var isFavoriteCar = ObservableField(false)
    var resourceProvider: ResourceProvider? = null
    var hotDealsItem: TopDeal? = null
    override val layoutId: Int
        get() =
            when {
                Objects.equals(orientation!!.get(),ListOrientation.ORIENTATION_VERTICAL)-> R.layout.top_deal_item_list_vertical_layout
                Objects.equals(orientation!!.get(),ListOrientation.ORIENTATION_GRID)-> R.layout.home_top_deal_item_list_vertical_layout
                else -> R.layout.home_top_deals_item_horizontal_layout
            }

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var resourceProviderObservable: ObservableField<ResourceProvider>? = null

    private var TotDealItem: TopDeal? = null

    var orientation: ObservableField<ListOrientation>? = null
    var imageUrl = ObservableField("")
    var topDealTitle = ObservableField("")
    var topDealprice = ObservableField("")
    var topDealName = ObservableField("")


    constructor(hotDealsItem: TopDeal?, resourceProvider: ResourceProvider,orientation: ListOrientation) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        this.resourceProvider = resourceProvider
        this.orientation = ObservableField(orientation)
        this.hotDealsItem = hotDealsItem
        isFavoriteCar.set(hotDealsItem?.car?.isFavorite)
        setupHotDeal(hotDealsItem)
    }


    fun setupHotDeal(TotDealsItem: TopDeal?) {
        this.TotDealItem = TotDealsItem
        if (!Utilities.isNullString(TotDealsItem?.car?.images?.get(0))) imageUrl.set(
            TotDealsItem?.car?.images?.get(0)!!
        )
        topDealTitle.set(TotDealsItem?.car?.name)
        topDealprice.set(resourceProvider?.getString(R.string.str_egp, TotDealsItem?.price))
        topDealName.set(TotDealItem?.name)

    }

}