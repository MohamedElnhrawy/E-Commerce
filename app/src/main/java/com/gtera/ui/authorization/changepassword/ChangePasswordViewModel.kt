package com.gtera.ui.authorization.resetpassword

import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.User
import com.gtera.data.model.requests.ChangePasswordRequest
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor() : BaseViewModel<ChangePasswordNavigator>() {


    var oldPassword = ObservableField("")
    var newPassword = ObservableField("")
    var confirmPassword = ObservableField("")

    var oldPasswordStatus = ObservableBoolean(false)
    var newPasswordStatus = ObservableBoolean(false)
    var confirmPasswordStatus = ObservableBoolean(false)

    var oldPasswordError = ObservableField("")
    var newPasswordError = ObservableField("")
    var confirmPasswordError = ObservableField("")


    fun changePasswordBtnClick() {
        if (validInput()) {
            isLoading.set(true)
            appRepository.changePassword(
                ChangePasswordRequest(oldPassword.get(), newPassword.get(), confirmPassword.get()), lifecycleOwner, object : APICommunicatorListener<User?> {
                    override fun onSuccess(result: User?) {
                        isLoading.set(false)
                        showSuccessBanner(getStringResource(R.string.str_updated_successfully))
                        goBack()
                    }

                    override fun onError(throwable: ErrorDetails?) {
                        showErrorBanner(throwable?.errorMsg)
                        isLoading.set(false)
                    }

                })

        }
    }

    private fun validInput(): Boolean {
        confirmPasswordStatus.set(false)
        newPasswordStatus.set(false)
        oldPasswordStatus.set(false)
        var status = true
        val successMsg = getStringResource(R.string.looks_great)
        if (TextUtils.isEmpty(oldPassword.get()) ||
            Objects.requireNonNull(oldPassword.get())?.let { Utilities.isValidPassword(it).not() }!!
        ) {
            oldPasswordError.set(
                if (TextUtils.isEmpty(oldPassword.get())) resourceProvider.getString(
                    R.string.field_is_required,
                    getStringResource(R.string.str_current_password)
                ) else getStringResource(R.string.password_too_short)
            )
            status = false
        } else if (!TextUtils.isEmpty(oldPasswordError.get())) {
            oldPasswordError.set(
                successMsg
            )
            oldPasswordStatus.set(true)
        }
        if (TextUtils.isEmpty(newPassword.get()) ||
            Objects.requireNonNull(newPassword.get())?.let { Utilities.isValidPassword(it).not() }!!
        ) {
            newPasswordError.set(
                if (TextUtils.isEmpty(newPassword.get())) resourceProvider.getString(
                    R.string.field_is_required,
                    getStringResource(R.string.str_new_password)
                ) else getStringResource(R.string.str_password_too_short)
            )
            status = false
        } else if (!TextUtils.isEmpty(newPasswordError.get())) {
            newPasswordStatus.set(true)
            newPasswordError.set(
                successMsg
            )
        }
        if (TextUtils.isEmpty(confirmPassword.get()) ||
            confirmPassword.get() != newPassword.get()
        ) {
            confirmPasswordError.set(
                if (TextUtils.isEmpty(confirmPassword.get())) resourceProvider.getString(
                    R.string.field_is_required,
                    getStringResource(R.string.str_confirm_password)
                ) else getStringResource(R.string.str_passwords_dont_match)
            )
            status = false
        } else if (!TextUtils.isEmpty(confirmPasswordError.get())) {
            confirmPasswordStatus.set(true)
            confirmPasswordError.set(
               successMsg
            )
        }
        return status
    }


}