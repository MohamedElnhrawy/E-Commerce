package com.gtera.ui.mycars.carcompare.list

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Car
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.utils.Utilities
import javax.inject.Inject

class CarCompareItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = R.layout.car_compare_item_list_layout



    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var resourceProviderObservable: ObservableField<ResourceProvider>? = null
    var isFavoriteCar = ObservableField(false)
    var car: Car? = null

    var imageUrl = ObservableField("")
    var carName = ObservableField("")
    var isSelected = ObservableField(false)


    constructor(car: Car?, resourceProvider: ResourceProvider, selectedBefore: Boolean) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        this.car = car
        this.isSelected.set(car?.isCheckedForCompare)
        isFavoriteCar.set(car?.isFavorite)
        setupCar(car)
    }


    fun setupCar(car: Car?) {
        this.car = car
        if (!Utilities.isNullString(car?.images?.get(0))) imageUrl.set(
            car?.images?.get(0)
        )
        carName.set(car?.brand?.name.let {if (it!= null) it+ " | " else "" } + car?.name .let {if (it!= null) it+ " | " else "" } + car?.manufactureYear.let {if (it!= null) it else "" })

    }

}