package com.gtera.ui.ordernow.list

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Car
import com.gtera.data.model.Order
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.utils.Utilities
import javax.inject.Inject

class OrderListItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = R.layout.orders_item_list_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var resourceProviderObservable: ObservableField<ResourceProvider>? = null
    var isFavoriteCar = ObservableField(false)

    var orderCarImageUrl = ObservableField("")
    var orderCarPrice = ObservableField("")
    var orderCarName = ObservableField("")
    var orderCarId = ObservableField(0)
    var orderStatus = ObservableField(0)
    var car: Car? = null
    var order: Order? = null


    constructor( order: Order?, resourceProvider: ResourceProvider) : this() {
        setupMyOrder( order, resourceProvider)
    }


    fun setupMyOrder( order: Order?, resourceProvider: ResourceProvider) {
        this.order = order
        this.car = order?.car
        this.isFavoriteCar.set(car?.isFavorite!!)
        if (!Utilities.isNullList(order?.car?.images))
            orderCarImageUrl.set(order?.car?.images?.get(0))
        orderCarName.set(order?.car?.name)
        orderCarId.set(this.car?.id)
        orderCarPrice.set(resourceProvider.getString(R.string.str_egp, order?.car?.priceFrom.toString()))


    }

}