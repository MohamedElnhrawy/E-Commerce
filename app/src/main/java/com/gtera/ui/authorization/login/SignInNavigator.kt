package com.gtera.ui.authorization.login

import android.app.Activity

interface SignInNavigator {

    fun getActivity(): Activity?
    fun performGoogleLogin()
}