package com.gtera.ui.mycars.list

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Car
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.utils.Utilities
import javax.inject.Inject

class MyCarListItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = R.layout.my_car_item_list_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var resourceProviderObservable: ObservableField<ResourceProvider>? = null
    var isFavoriteCar = ObservableField(false)
    var car: Car? = null

    var imageUrl = ObservableField("")
    var carName = ObservableField("")
    var carId = ObservableField(0)


    constructor(car: Car?, resourceProvider: ResourceProvider) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        setupMyCar(car)
    }


    fun setupMyCar(car: Car?) {
        this.car = car
        this.isFavoriteCar.set(car?.isFavorite!!)
        if (!Utilities.isNullList(car?.images))
            imageUrl.set(car?.images?.get(0))
        carName.set(car?.name)
        carId.set(this.car?.id)


    }

}