package com.gtera.ui.authorization.resetpasswordconfirmation

import androidx.databinding.ObservableField
import com.gtera.base.BaseViewModel
import com.gtera.utils.APPConstants
import javax.inject.Inject

class ResetPasswordConfirmationViewModel @Inject constructor() : BaseViewModel<ResetPasswordConfirmationNavigator>() {


    var userEmail = ObservableField("")


    override fun onViewCreated() {
        super.onViewCreated()
        userEmail.set(dataExtras?.getString(APPConstants.EXTRAS_KEY_RESET_PASSWORD_MAIL))
    }

    fun backToLogin() {
        goBack()
    }



}