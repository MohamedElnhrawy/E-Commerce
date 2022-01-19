package com.gtera.ui.authorization.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.mukeshsolanki.sociallogin.google.GoogleHelper
import com.mukeshsolanki.sociallogin.google.GoogleListener
import javax.inject.Inject


class SignInActivity : BaseActivity<SignInLayoutBinding, SignInViewModel>(), SignInNavigator,
    GoogleListener, GoogleApiClient.ConnectionCallbacks {
    //for google social login
    private lateinit var mgoogle: GoogleHelper
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleApiClient: GoogleApiClient
    internal var result: GoogleSignInResult? = null

    @Inject
    lateinit var signInViewModel: SignInViewModel
    private var callbackManager: CallbackManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setupFacebookLogin()
        super.onCreate(savedInstanceState)
        setupGoogleLoginHelpers()
    }

    override val viewModelClass: Class<SignInViewModel>
        get() = SignInViewModel::class.java

    override fun getLayoutRes(): Int {
        return R.layout.sign_in_layout
    }

    override fun setNavigator(viewModel: SignInViewModel?) {
        viewModel?.setNavigator(this)
    }

    override val toolbarTitle: String?
        get() = getString(R.string.sign_in)

    override fun getActivity(): Activity? {
        return this
    }


    private fun setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    viewModel?.facebookLogin(loginResult.accessToken)
                    LoginManager.getInstance().logOut()
                }

                override fun onCancel() {}
                override fun onError(exception: FacebookException) {
                    // App code
                    showToast(exception.message, Toast.LENGTH_SHORT)
                }
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // google
        mgoogle.onActivityResult(requestCode, resultCode, data)
        result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

        // facebook
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun hasBack(): Boolean {
        return true;
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }

    fun setupGoogleLoginHelpers() {
        mgoogle = GoogleHelper(this, this, null)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    override fun performGoogleLogin() {
        mgoogle.performSignIn(this)
    }

    override fun onGoogleAuthSignIn(token: String?, userId: String?) {
        token?.let {
            viewModel?.googleLogin(it, mgoogle)
            mgoogle.performSignOut()
        }
    }

    override fun onGoogleAuthSignOut() {
    }

    override fun onGoogleAuthSignInFailed(p0: String?) {
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

}
