package com.gtera.ui.authorization.register

import android.app.Activity
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.User
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class SignUpViewModel @Inject constructor() : BaseViewModel<SignUpNavigator>() {


    var lastName = ObservableField("")
    var firstName = ObservableField("")
    var userMail = ObservableField("")
    var userPassword = ObservableField("")

    var lastNameStatus = ObservableBoolean(false)
    var firstNameStatus = ObservableBoolean(false)
    var userMailStatus = ObservableBoolean(false)
    var userPasswordStatus = ObservableBoolean(false)

    var lastNameError = ObservableField("")
    var firstNameError = ObservableField("")
    var userMailError = ObservableField("")
    var userPasswordError = ObservableField("")


    protected fun validInputFields(): Boolean {

        lastNameStatus.set(false)
        firstNameStatus.set(false)
        userMailStatus.set(false)
        userPasswordStatus.set(false)
        val successMsg = getStringResource(R.string.looks_great)
        var isValid = true
        if (TextUtils.isEmpty(firstName.get()) ||
            Objects.requireNonNull(firstName.get())?.let { Utilities.isValidPersonName(it).not() }!!
        ) {
            firstNameError.set(
                resourceProvider.getString(
                    if (TextUtils.isEmpty(firstName.get())) R.string.field_is_required else if (Objects.requireNonNull<String>(
                            firstName.get()
                        ).length < 3
                    ) R.string.user_name_length_short else R.string.start_special_characters,
                    getStringResource(R.string.user_name)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(firstNameError.get())) {
            firstNameStatus.set(true)
            firstNameError.set(
                 successMsg
            )
        }

        if (TextUtils.isEmpty(lastName.get()) ||
            Objects.requireNonNull(lastName.get())?.let { Utilities.isValidPersonName(it).not() }!!
        ) {
            lastNameError.set(
                resourceProvider.getString(
                    if (TextUtils.isEmpty(lastName.get())) R.string.field_is_required else if (Objects.requireNonNull<String>(
                            lastName.get()
                        ).length < 3
                    ) R.string.user_name_length_short else R.string.start_special_characters,
                    getStringResource(R.string.user_name)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(lastNameError.get())) {
            lastNameStatus.set(true)
            lastNameError.set(
                successMsg
            )
        }

        if (TextUtils.isEmpty(userMail.get()) ||
            Objects.requireNonNull(userMail.get())?.let { !Utilities.isValidEmail(it)}!!
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

        if (TextUtils.isEmpty(userPassword.get()) ||
            !Utilities.isValidPassword(Objects.requireNonNull(userPassword.get())!!)
        ) {
            userPasswordError.set(
                resourceProvider.getString(
                    if (TextUtils.isEmpty(
                            userPassword.get()
                        )
                    ) R.string.field_is_required else R.string.password_too_short,
                    getStringResource(R.string.password)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(userPasswordError.get())) {
            userPasswordStatus.set(true)
            userPasswordError.set(
                if (userPasswordError.get() == successMsg
                ) "" else successMsg
            )
        }

        return isValid
    }

    fun signUpBtnClick() {
        if (isLoading.get()) return
        if (validInputFields()) {
            isLoading.set(true)
            val user = User(
                Objects.requireNonNull(userMail.get()!!),
                Objects.requireNonNull(firstName.get()),
                Objects.requireNonNull(lastName.get()),
                userPassword.get()
            )


            showLoading()
            appRepository.signUp(lifecycleOwner,
                user,
                object : APICommunicatorListener<BaseResponse<User?>?> {

                    override fun onSuccess(result: BaseResponse<User?>?) {

                        showSuccessBanner(getStringResource(R.string.str_registered_successfully))
                        successSignup()
                        hideLoading()

                    }

                    override fun onError(throwable: ErrorDetails?) {
                        hideLoading()
                        isLoading.set(false)
                        showErrorBanner(throwable?.errorMsg)


                    }

                })

        }
    }

    protected fun successSignup() {
        isLoading.set(false)

        if (dataExtras?.containsKey(APPConstants.EXTRAS_KEY_FAV)!!) setViewResult(
            AppScreenRoute.SIGN_IN_SUCCESS,
            Activity.RESULT_OK,
            null
        ) else openView(AppScreenRoute.MAIN_SCREEN, null)
    }

    fun onSignInBtnClick() {
        openView(AppScreenRoute.SIGN_IN_SCREEN, null)
    }

    fun openSignInView(){
        openView(AppScreenRoute.SIGN_IN_SCREEN, null)
    }


}