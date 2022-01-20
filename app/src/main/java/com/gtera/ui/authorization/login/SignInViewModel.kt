package com.gtera.ui.authorization.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.model.User
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SignInViewModel @Inject constructor() : BaseViewModel<SignInNavigator>() {

    // create instance of firebase auth
    lateinit var auth: FirebaseAuth

    // we will use this to match the sent otp from firebase
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    var user = ObservableField("")
    var userPassword = ObservableField("")

    var userError = ObservableField("")
    var userPasswordError = ObservableField("")

    var userStatus = ObservableBoolean(false)
    var showPassword = ObservableBoolean(false)
    var userPasswordStatus = ObservableBoolean(false)


    override fun onViewCreated() {
        super.onViewCreated()
        setupFirebaseClient()
    }

    init {
        user.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                showPassword.set(
                    !user.get().isNullOrEmpty() && Utilities.isValidEmail(user.get()!!)
                )
            }

        })
    }

    fun signInBtnClick() {

        if (isLoading.get()) return

        if (validInput()) {

            isLoading.set(true)
            if (!showPassword.get())
                sendVerificationCode("+2" + user.get()!!)
            else
                createUserWithEmailAndPassword()
        }
    }

    private fun validInput(): Boolean {
        userStatus.set(false)
        userPasswordStatus.set(false)
        var isValid = true
        val successMsg = resourceProvider.getString(R.string.looks_great)
        if (showPassword.get()) {

            if (TextUtils.isEmpty(user.get()) ||
                Objects.requireNonNull(user.get())?.let { !Utilities.isValidEmail(it) }!!
            ) {
                userError.set(
                    resourceProvider.getString(
                        if (TextUtils.isEmpty(user.get())) R.string.field_is_required else R.string.enter_valid_email,
                        getStringResource(R.string.email)
                    )
                )
                isValid = false
            } else if (!TextUtils.isEmpty(userError.get())) {
                userStatus.set(true)
                userError.set(
                    successMsg
                )
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


        } else {


            if (TextUtils.isEmpty(user.get()) || user.get()?.length!! < 11 ||
                Objects.requireNonNull(user.get())
                    ?.let { Utilities.isValidPhoneNumber(it).not() }!!
            ) {
                userStatus.set(false)
                userError.set(
                    resourceProvider.getString(
                        if (TextUtils.isEmpty(user.get())) R.string.field_is_required else R.string.enter_valid_phone,
                        resourceProvider.getString(R.string.str_phone)
                    )
                )
                isValid = false
            } else if (!TextUtils.isEmpty(userError.get())) {
                userStatus.set(true)
                userError.set(successMsg)

            }
        }

        return isValid
    }

    private fun setupFirebaseClient() {
        auth = FirebaseAuth.getInstance()

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential, auth)
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG", "onVerificationFailed  $e")
                showErrorBanner("onVerificationFailed  $e")
                isLoading.set(false)

            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG", "onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                isLoading.set(false)
                // Start a new activity using intent
                // also send the storedVerificationId using intent
                // we will use this id to send the otp back to firebase
                val extras = Bundle()
                extras.putString(APPConstants.EXTRAS_KEY_VERIFICATION_ID, storedVerificationId)
                openView(AppScreenRoute.OTP_SCREEN, extras)
//                val intent = Intent(applicationContext,OtpActivity::class.java)
//                intent.putExtra("storedVerificationId",storedVerificationId)
//                startActivity(intent)
//                finish()
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = navigator?.getActivity()?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(it) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
        }
        options?.let { PhoneAuthProvider.verifyPhoneNumber(it) }
        Log.d("GFG", "Auth started")
    }

    private fun createUserWithEmailAndPassword(){
        auth.createUserWithEmailAndPassword(user.get().toString().trim(), userPassword.get().toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoading.set(false)
                    val fbUser = task.result?.user
                    val user = fbUser?.let { it1 ->
                        User(
                            fbUser.email, fbUser.displayName, fbUser.phoneNumber,
                            it1.uid
                        )
                    }
                    user?.let { it1 ->
                        appRepository.insertUserLocally(it1)
                        openView(AppScreenRoute.MAIN_SCREEN, null)
                        sendEmailVerification(task.result.user)
                    }

                } else {
                    Log.e("error",task.exception.toString())
                    if ((task.exception as FirebaseAuthException).errorCode == "ERROR_EMAIL_ALREADY_IN_USE"){
                        // login direct
                        signInWithEmailAndPassword()
                    }else{
                        isLoading.set(false)
                        showErrorBanner(task.exception?.message)
                    }
                }

            }
    }

    private fun sendEmailVerification(fbUser:FirebaseUser?) {
        fbUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showSuccessBanner(getStringResource(R.string.email_verification_sent))
                }
            }
        }
    }

    private fun signInWithEmailAndPassword(){
        auth.signInWithEmailAndPassword(user.get().toString().trim(), userPassword.get().toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isLoading.set(false)
                    val fbUser = task.result?.user
                    val user = fbUser?.let { it1 ->
                        User(
                            fbUser.email, fbUser.displayName, fbUser.phoneNumber,
                            it1.uid
                        )
                    }
                    user?.let { it1 ->
                        appRepository.insertUserLocally(it1)
                        openView(AppScreenRoute.MAIN_SCREEN, null)
                    }

                } else {
                        isLoading.set(false)
                        showErrorBanner(task.exception?.message)
                    }
                }

            }


}