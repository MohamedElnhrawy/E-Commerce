package com.gtera.ui.mycars.insurance.insurancerequeatslist

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.CarInsuranceRenewal
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.InsuranceStatus
import javax.inject.Inject

class CarInsuranceListItemViewModel @Inject constructor() : BaseItemViewModel() {


    var insuranceStatus = ObservableField("")
    var insuranceChassisNo = ObservableField("")
    var insuranceOwnerName = ObservableField("")
    var insurancePrice = ObservableField("")
    var insuranceItem: CarInsuranceRenewal? = null


    constructor(
        insuranceItem: CarInsuranceRenewal,
        resourceProvider: ResourceProvider
    ) : this() {

        this.insuranceItem = insuranceItem
        this.insuranceChassisNo.set(
            resourceProvider.getString(
                R.string.str_car_insurance_chassis_no,
                insuranceItem.chassieNo
            )
        )
        this.insurancePrice.set(resourceProvider.getString(R.string.str_egp, insuranceItem.price))
        this.insuranceOwnerName.set(insuranceItem.name)

        this.insuranceStatus.set(
            when (insuranceItem.status) {
                InsuranceStatus.CONFIRMED.status -> resourceProvider.getString(R.string.str_car_insurance_request_confirmed)
                InsuranceStatus.PENDING.status -> resourceProvider.getString(R.string.str_car_insurance_request_pending)
                InsuranceStatus.PAID.status -> resourceProvider.getString(R.string.str_car_insurance_request_paid)
                else -> resourceProvider.getString(R.string.str_car_insurance_request_cancelled)
            }
        )

    }


    override val layoutId: Int
        get() = R.layout.insurance_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
}