package com.gtera.ui.home.viewmodels

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Car
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.CarType
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class CarItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = when {
            Objects.equals(carType!!.get(), CarType.CAR_LIST) -> R.layout.car_item_list_layout
            Objects.equals(carType!!.get(), CarType.CAR_GRID) -> R.layout.car_item_grid_layout
            Objects.equals(carType!!.get(), CarType.CAR_SEARCH_RELATED) -> R.layout.car_search_item_layout
            else -> R.layout.top_deals_item_horizontal_layout
        }

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
    var isFavoriteCar = ObservableField(false)
    var carType: ObservableField<CarType>? = null
    var resourceProviderObservable: ObservableField<ResourceProvider>? = null

    var car: Car? = null

    var imageUrl = ObservableField("")
    var carName = ObservableField("")
    var carPrice = ObservableField("")


    constructor(
        car: Car?,
        resourceProvider: ResourceProvider
    ) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        this.car = car
        isFavoriteCar.set(car?.isFavorite)
        setupCar(car)
    }

    constructor(
        car: Car?,
        resourceProvider: ResourceProvider,
        carType: CarType
    ) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        this.car = car
        this.carType = ObservableField(carType)
        isFavoriteCar.set(car?.isFavorite)
        setupCar(car)
    }


    private fun setupCar(car: Car?) {
        this.car = car
        if (!Utilities.isNullString(car?.images?.get(0))) imageUrl.set(
            car?.images?.get(0)
        )
        car?.priceFrom.let { carPrice.set(car?.priceFrom) }
        carName.set(car?.brand?.name .let {if (it!= null) it+ " | " else "" } + car?.name.let {if (it!= null) it+ " | " else "" } + car?.manufactureYear.let {if (it!= null) it else "" })

    }

}