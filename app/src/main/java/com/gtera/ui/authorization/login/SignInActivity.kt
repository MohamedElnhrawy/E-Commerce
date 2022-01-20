package com.gtera.ui.authorization.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.SignInLayoutBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.gtera.di.providers.ResourceProvider
import com.mukeshsolanki.sociallogin.google.GoogleHelper
import com.mukeshsolanki.sociallogin.google.GoogleListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SignInActivity : BaseActivity<SignInLayoutBinding, SignInViewModel>(), SignInNavigator{

    @Inject
    lateinit var signInViewModel: SignInViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override val viewModelClass: Class<SignInViewModel>
        get() = SignInViewModel::class.java

    override fun getLayoutRes(): Int {
        return R.layout.sign_in_layout
    }

    override fun setNavigator(viewModel: SignInViewModel?) {
        viewModel?.setNavigator(this)
    }

    override val toolbarTitle: String
        get() = resourceProvider.getString(R.string.sign_in)

    override fun getActivity(): Activity {
        return this
    }


    override fun hasBack(): Boolean {
        return false
    }

}
