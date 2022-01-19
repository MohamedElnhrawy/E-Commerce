package com.gtera.ui.calculateit.result

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Car
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.utils.Utilities
import javax.inject.Inject

class CalculateItCarItemVM @Inject constructor() : BaseItemViewModel() {



    override val layoutId: Int
        get() = R.layout.calculate_it_car_item_layout


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
    var isFavoriteCar = ObservableField(false)
    var monthlyInstallmnet = ObservableField("")
    var insurance = ObservableField("")
    var administrativeExpense = ObservableField("")
    var totalAmountRequired = ObservableField("")
    var lastUpdate = ObservableField("")
    var carCode = ObservableField("")


    lateinit var resourceProviderObservable: ResourceProvider

    var car: Car? = null

    var imageUrl = ObservableField("")
    var carName = ObservableField("")
    var carPrice = ObservableField("")


    constructor(
        car: Car?,
        resourceProvider: ResourceProvider
    ) : this() {
        resourceProviderObservable = resourceProvider
        this.car = car
        isFavoriteCar.set(car?.isFavorite)
        setupCar(car)
    }




    private fun setupCar(car: Car?) {
        this.car = car
        if (!Utilities.isNullString(car?.images?.get(0))) imageUrl.set(
            car?.images?.get(0)
        )
        car?.priceFrom.let { carPrice.set(resourceProviderObservable.getString(R.string.str_price)+car?.priceFrom) }
        carName.set(car?.brand?.name.let { it?:"" } + "  " + car?.name.let { it?:"" } + "  " + car?.manufactureYear.let { it?:"" })
        monthlyInstallmnet.set(resourceProviderObservable.getString(R.string.str_monthly_installment))
        insurance.set(resourceProviderObservable.getString(R.string.str_insurance))
        administrativeExpense.set(resourceProviderObservable.getString(R.string.str_adminstrative_expenses))
        totalAmountRequired.set(resourceProviderObservable.getString(R.string.str_totla_amount_required))
        lastUpdate.set(resourceProviderObservable.getString(R.string.str_last_update))
        carCode.set(resourceProviderObservable.getString(R.string.str_car_code))

    }

}