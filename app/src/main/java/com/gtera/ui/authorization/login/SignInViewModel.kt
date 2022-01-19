package com.gtera.ui.authorization.login

import android.app.Activity
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.User
import com.gtera.data.model.requests.SocialLoginRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.SOCIAL_MEDIA_FACEBOOK_TYPE
import com.gtera.utils.APPConstants.SOCIAL_MEDIA_GOOGLE_TYPE
import com.gtera.utils.Utilities
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.mukeshsolanki.sociallogin.google.GoogleHelper
import java.util.*
import javax.inject.Inject


class SignInViewModel @Inject constructor() : BaseViewModel<SignInNavigator>() {


    var userMail = ObservableField("")
    var userPassword = ObservableField("")

    var userMailError = ObservableField("")
    var userPasswordError = ObservableField("")

    var userMailStatus = ObservableBoolean(false)
    var userPasswordStatus = ObservableBoolean(false)
    private lateinit var googleSignInClient: GoogleSignInClient


//    constructor(activityClass: Class<*>?): super(activityClass) {
//
//    }

    override fun onViewCreated() {
        super.onViewCreated()
    }

    fun forgetPasswordClick() {
        openView(AppScreenRoute.RESET_PASSWORD, null)
    }

    fun signInBtnClick() {

        if (isLoading.get()) return

        if (validInput()) {

            isLoading.set(true)

            appRepository.signIn(lifecycleOwner,
                User(Objects.requireNonNull(userMail.get()!!), userPassword.get()),
                object : APICommunicatorListener<BaseResponse<User?>?> {

                    override fun onSuccess(result: BaseResponse<User?>?) {

                        successLogin()


                    }

                    override fun onError(throwable: ErrorDetails?) {

                        isLoading.set(false)
                        showErrorBanner(throwable?.errorMsg)


                    }

                })
        }
    }

    protected fun validInput(): Boolean {
        userMailStatus.set(false)
        userPasswordStatus.set(false)
        var isValid = true
        val successMsg = resourceProvider.getString(R.string.looks_great)
        if (TextUtils.isEmpty(userMail.get()) ||
            Objects.requireNonNull(userMail.get())?.let { Utilities.isValidEmail(it).not() }!!
        ) {
            userMailStatus.set(false)
            userMailError.set(
                resourceProvider.getString(
                    if (TextUtils.isEmpty(userMail.get())) R.string.field_is_required else R.string.enter_valid_email,
                    resourceProvider.getString(R.string.email)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(userMailError.get())) {
            userMailStatus.set(true)
            userMailError.set(successMsg)

        }
        if (TextUtils.isEmpty(userPassword.get()) ||
            !Objects.requireNonNull(userPassword.get())?.let { Utilities.isValidPassword(it) }!!
        ) {
            userPasswordStatus.set(false)
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
            userPasswordError.set(successMsg)


        }
        return isValid
    }

    fun onFacebookLoginButtonClicked() {
        LoginManager.getInstance().logInWithReadPermissions(
            navigator?.getActivity(),
            Arrays.asList("email", "public_profile")
        )
    }

    fun onGoogleLoginButtonClicked() {
        navigator?.performGoogleLogin()
    }


    fun facebookLogin(accessToken: AccessToken) {
        showLoading()
        var socialLoginRequest = SocialLoginRequest()
        socialLoginRequest.accessToken = accessToken.token
        socialLoginRequest.type = SOCIAL_MEDIA_FACEBOOK_TYPE
        appRepository.signInWithSocialAccount(
            lifecycleOwner,
            socialLoginRequest,
            object : APICommunicatorListener<BaseResponse<User?>?> {

                override fun onSuccess(result: BaseResponse<User?>?) {
                    successLogin()

                    showSuccessBanner(getStringResource(R.string.log_in_success))
                }

                override fun onError(throwable: ErrorDetails?) {
                    showErrorBanner(throwable?.errorMsg)
                }


            })

    }


    fun googleLogin(
        accessToken: String,
        mgoogle: GoogleHelper
    ) {

        showLoading()
        var socialLoginRequest = SocialLoginRequest()
        socialLoginRequest.accessToken = accessToken
        socialLoginRequest.type = SOCIAL_MEDIA_GOOGLE_TYPE

        appRepository.signInWithSocialAccount(
            lifecycleOwner,
            socialLoginRequest,
            object : APICommunicatorListener<BaseResponse<User?>?> {

                override fun onSuccess(result: BaseResponse<User?>?) {
                    mgoogle.performSignOut()
                    successLogin()

                    showSuccessBanner(getStringResource(R.string.log_in_success))
                }

                override fun onError(throwable: ErrorDetails?) {
                    showErrorBanner(throwable?.errorMsg)
                }


            })

    }

    fun onSignUpBtnClick() {
        openView(AppScreenRoute.SIGN_UP_SCREEN, null)
    }

    protected fun successLogin() {
        isLoading.set(false)
        hideLoading()
        hideLoadingDialog()
        if (dataExtras?.containsKey(APPConstants.EXTRAS_KEY_FAV)!!)
            setViewResult(
                AppScreenRoute.SIGN_IN_SUCCESS,
                Activity.RESULT_OK,
                null
            ) else openView(AppScreenRoute.MAIN_SCREEN, null)
    }


}