package com.gtera.ui.mycars.insurance.endorsement

import android.app.Activity
import android.os.Bundle
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.response.Payment
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import javax.inject.Inject

class EndorsementViewModel @Inject constructor() : BaseViewModel<EndorsementNavigator>(),
    ViewHolderInterface {

    var insuranceID = -1
    override fun onViewCreated() {
        super.onViewCreated()
        if (dataExtras?.containsKey(APPConstants.EXTRAS_KEY_INSURANCE_ID)!!){
            insuranceID = dataExtras?.getInt(APPConstants.EXTRAS_KEY_INSURANCE_ID)!!
        }

    }

    override fun onViewClicked(position: Int, id: Int) {
        TODO("Not yet implemented")
    }


    fun onAcceptAndPayBtnClick() {
        if (insuranceID == -1)
            return

        isLoading.set(true)
         appRepository.RenewalInsurancePay(
            insuranceID,
            lifecycleOwner,
            object : APICommunicatorListener<Payment?> {
                override fun onSuccess(result: Payment?) {
                    isLoading.set(false)
                    val extras = Bundle()
                    extras.putString(APPConstants.EXTRAS_KEY_WEB_URL, result?.data?.url)
                    openViewForResult(AppScreenRoute.RENEWAL_INSURANCE_PAYMENT,APPConstants.REQUEST_CODE_ONLINE_PAYMENT,extras)
                }


                override fun onError(throwable: ErrorDetails?) {
                    isLoading.set(false)

                    showErrorBanner(throwable?.errorMsg)
                }


            })
    }

    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        if (requestCode == APPConstants.REQUEST_CODE_ONLINE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK)
                successPayment()
            else if (resultCode == APPConstants.RESULT_CODE_ERROR && extras != null) {
                val errorMsg = extras.getString(
                    APPConstants.EXTRAS_KEY_PAYMENT_MESSAGE,
                    getStringResource(R.string.something_went_wrong)
                )
                showErrorBanner(errorMsg)
            }

        } else
            super.onViewResult(requestCode, resultCode, extras)
    }

    fun successPayment(){
        isLoading.set(false)
    }
}