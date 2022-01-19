package com.gtera.ui.mycars.insurance.insurancedetails

import android.os.Bundle
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.model.CarInsuranceRenewal
import com.gtera.ui.base.InsuranceStatus
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute.CAR_RENEWAL_INSURANCE_ENDORSEMENT
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.EXTRAS_KEY_INSURANCE_REQUEST
import javax.inject.Inject

class InsuranceDetailsViewModel @Inject constructor() : BaseViewModel<InsuranceDetailsNavigator>(),
    ViewHolderInterface {

    var insuranceStatus = ObservableField("")
    var insuranceChassisNo = ObservableField("")
    var insuranceOwnerName = ObservableField("")
    var insuranceOwnerMobile = ObservableField("")
    var insuranceBranch = ObservableField("")
    var insurancePrice = ObservableField("")
    var insuranceNotes = ObservableField("")
    var headerMessage = ObservableField("")

    var insuranceItem: CarInsuranceRenewal? = null
    override fun onViewCreated() {

        super.onViewCreated()
        insuranceItem =
            dataExtras?.getSerializable(EXTRAS_KEY_INSURANCE_REQUEST) as CarInsuranceRenewal?

        insuranceStatus.set(insuranceItem?.status)
        this.insuranceChassisNo.set(
            resourceProvider.getString(
                R.string.str_car_insurance_chassis_no,
                insuranceItem?.chassieNo
            )
        )
        this.insurancePrice.set(
            resourceProvider.getString(
                R.string.str_car_insurance_price,
                (resourceProvider.getString(R.string.str_egp, insuranceItem?.price))
            )
        )
        this.insuranceOwnerName.set(
            resourceProvider.getString(
                R.string.str_car_insurance_details_name,
                insuranceItem?.name
            )
        )
        this.insuranceOwnerMobile.set(
            resourceProvider.getString(
                R.string.str_car_insurance_details_mobile,
                insuranceItem?.phoneNumber
            )
        )
        this.insuranceBranch.set(
            resourceProvider.getString(
                R.string.str_car_insurance_details_branch,
                insuranceItem?.branch?.name.let { it }
            )
        )
        this.insuranceNotes.set(insuranceItem?.notes)

        this.headerMessage.set(
            resourceProvider.getString(
                R.string.str_insurance_details_header_message, when (insuranceItem?.status) {
                    InsuranceStatus.CONFIRMED.status -> resourceProvider.getString(R.string.str_car_insurance_request_confirmed)
                    InsuranceStatus.PENDING.status -> resourceProvider.getString(R.string.str_car_insurance_request_pending)
                    InsuranceStatus.PAID.status -> resourceProvider.getString(R.string.str_car_insurance_request_paid)
                    else -> resourceProvider.getString(R.string.str_car_insurance_request_cancelled)
                }
            )
        )


    }

    override fun onViewRecreated() {
        super.onViewRecreated()

    }

    override fun onViewClicked(position: Int, id: Int) {
        TODO("Not yet implemented")
    }


    fun onPayBtnClick() {
        val extra = Bundle()
        insuranceItem?.id?.let { extra.putInt(APPConstants.EXTRAS_KEY_INSURANCE_ID, it) }
        openView(CAR_RENEWAL_INSURANCE_ENDORSEMENT, extra)
    }

}