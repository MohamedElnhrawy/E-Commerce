package com.gtera.ui.authorization.resetpassword

import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.requests.ResetPasswordRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.data.model.response.ResetPassword
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class ResetPasswordViewModel @Inject constructor() : BaseViewModel<ResetPasswordNavigator>() {


    var userMail = ObservableField("")


    var userMailStatus = ObservableBoolean(false)


    var userMailError = ObservableField("")


    protected fun validInputFields(): Boolean {


        userMailStatus.set(false)
        val successMsg = getStringResource(R.string.looks_great)
        var isValid = true

        if (TextUtils.isEmpty(userMail.get()) ||
            Objects.requireNonNull(userMail.get())?.let { !Utilities.isValidEmail(it) }!!
        ) {
            userMailError.set(
                resourceProvider.getString(
                    if (TextUtils.isEmpty(userMail.get())) R.string.field_is_required else R.string.enter_valid_email,
                    getStringResource(R.string.email)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(userMailError.get())) {
            userMailStatus.set(true)
            userMailError.set(
                successMsg
            )
        }


        return isValid
    }

    fun resetPasswordBtnClick() {
        if (isLoading.get()) return
        if (validInputFields()) {
            isLoading.set(true)

            val resetRequest = ResetPasswordRequest(
                Objects.requireNonNull(userMail.get())

            )


            showLoading()
            appRepository.sendResetPassword(resetRequest, lifecycleOwner,
                object : APICommunicatorListener<BaseResponse<ResetPassword?>?> {

                    override fun onSuccess(result: BaseResponse<ResetPassword?>?) {
                        hideLoading()
                        isLoading.set(false)
                        if (result?.message != null) showErrorBanner(result?.message) else successReset()

                    }

                    override fun onError(throwable: ErrorDetails?) {
                        hideLoading()
                        isLoading.set(false)
                        showErrorBanner(throwable?.errorMsg)


                    }

                })

        }
    }

    protected fun successReset() {
        isLoading.set(false)

        val extras = Bundle()
        extras.putString(APPConstants.EXTRAS_KEY_RESET_PASSWORD_MAIL, userMail.get())
        openView(AppScreenRoute.RESET_PASSWORD_CONFIRMATION, extras)

    }



}