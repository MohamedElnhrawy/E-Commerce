package com.gtera.data.helper.interceptors

import android.content.Context
import com.gtera.data.local.preferences.PreferencesHelper
import com.gtera.data.security.SecureData.encryptRequest_CFB
import com.gtera.utils.Utilities.bodyToString
import io.michaelrocks.paranoid.Obfuscate
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*
import javax.inject.Inject

@Obfuscate
class EncryptionInterceptor : Interceptor {

    @Inject
    lateinit var preferencesHelper : PreferencesHelper
    @Inject
    lateinit var context : Context

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.body() != null) request = encryptRequest_CFB(
            request,
            bodyToString(Objects.requireNonNull(request.body()!!,preferencesHelper.accessToken!!)),
            0
        , preferencesHelper.accessToken!!, context)
        return chain.proceed(request)
    }
}