package com.gtera.ui.profile.contactus

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Branche
import com.gtera.data.model.response.BaseResponse
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import javax.inject.Inject

class ContactUsVM @Inject constructor(): BaseViewModel<ContactUsNavigator>() {

    var abazaEmail = ObservableField("")
    var abazaPhoneNumber = ObservableField("")
    var name = ObservableField("")
    var email = ObservableField("")
    var message = ObservableField("")
    var branche:Branche? = null
    override fun onViewCreated() {
        super.onViewCreated()
        dataExtras?.let {
           if (it.containsKey(APPConstants.EXTRAS_KEY_BRANCH)){
               branche = it.get(APPConstants.EXTRAS_KEY_BRANCH) as Branche

           }
        }

        abazaEmail.set(resourceProvider.getString(R.string.abaza_email))
        abazaPhoneNumber.set(resourceProvider.getString(R.string.abaza_phone))
    }


    fun sendEmail(){

        if(!Validate())
            showErrorBanner(getStringResource(R.string.str_invalid_information))
        else {
            val body = HashMap<String,String>()
            name.get()?.let { body.put("name", it) }
            email.get()?.let { body.put("email", it) }
            message.get()?.let { body.put("message", it) }
            appRepository.ContactUs(
                body,
                lifecycleOwner,
                object : APICommunicatorListener<BaseResponse<Any>> {
                    override fun onSuccess(result: BaseResponse<Any>) {
                            showSuccessBanner(getStringResource(R.string.banner_success))
                            finish()
                    }


                    override fun onError(throwable: ErrorDetails?) {
                        showErrorBanner(throwable?.errorMsg)
                    }


                })
        }
    }

    fun Validate():Boolean{
        var status = true

        if (TextUtils.isEmpty(name.get()) || !Utilities.isValidPersonName(name.get().toString()))
            status = false

        if (TextUtils.isEmpty(email.get()) || !Utilities.isValidEmail(email.get().toString()))
            status = false

        if (TextUtils.isEmpty(message.get()) || !Utilities.isValidEmail(email.get().toString()))
            status = false

        return status
    }
}