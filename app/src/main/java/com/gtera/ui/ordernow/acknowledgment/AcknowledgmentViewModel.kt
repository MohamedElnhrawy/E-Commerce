package com.gtera.ui.ordernow.acknowledgment

import android.os.Bundle
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.response.Payment
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import javax.inject.Inject

class AcknowledgmentViewModel @Inject constructor() : BaseViewModel<AcknowledgmentNavigator>(),
    ViewHolderInterface {



    override fun onViewCreated() {
        super.onViewCreated()


    }


    override fun onViewRecreated() {
        super.onViewRecreated()

    }

    override fun onViewClicked(position: Int, id: Int) {
        TODO("Not yet implemented")
    }

    fun onAcceptAndPayBtnClick() {
//        if (insuranceID == -1)
//            return

        isLoading.set(true)
        appRepository.payCarOrder(
            1,
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

}