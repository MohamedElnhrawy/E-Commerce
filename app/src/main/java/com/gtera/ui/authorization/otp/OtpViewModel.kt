package com.gtera.ui.authorization.otp

import androidx.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.gtera.base.BaseViewModel
import com.gtera.data.model.User
import com.gtera.ui.base.TextChangeListener
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import javax.inject.Inject


class OtpViewModel @Inject constructor() : BaseViewModel<OtpNavigator>() {


    var textChangeListener: TextChangeListener = object : TextChangeListener{
        override fun afterChange(text: String?) {
            otpField.set(text)
        }

    }

    // create instance of firebase auth
    lateinit var auth: FirebaseAuth

//    // we will use this to match the sent otp from firebase
//    lateinit var storedVerificationId:String
    val otpField = ObservableField("")
    val storedVerificationId = ObservableField("")





    override fun onViewCreated() {
        super.onViewCreated()
        auth=FirebaseAuth.getInstance()
        storedVerificationId.set(dataExtras?.getString(APPConstants.EXTRAS_KEY_VERIFICATION_ID))
    }

    fun checkOtp() {
        if (isLoading.get()) return

        isLoading.set(true)

        val credential : PhoneAuthCredential? = otpField.get()?.let {
            storedVerificationId.get()?.let { it1 ->
                PhoneAuthProvider.getCredential(
                    it1, it
                )
            }
        }
        credential?.let {
            signInWithPhoneAuthCredential(it,auth)
        }

    }

}