package com.gtera.data.helper.interceptors

import android.content.Context
import com.gtera.data.local.preferences.PreferencesHelper
import com.gtera.data.security.SecureData
import io.michaelrocks.paranoid.Obfuscate
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*
import javax.inject.Inject

@Obfuscate
class DecryptionInterceptor : Interceptor {

    @Inject
    lateinit var preferencesHelper :PreferencesHelper

    @Inject
    lateinit var context :Context
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(chain.request())
        return if (response.code() == 200 && response.body() != null && Objects.requireNonNull(
                response.body()
            )
                ?.contentType() != null && Objects.requireNonNull(
                Objects.requireNonNull(response.body())?.contentType()
            ).toString() == "text/html; charset=UTF-8"
        ) SecureData.decryptResponse_CFB(response, request.method(), 0, preferencesHelper.accessToken!!, context) else response
    }
}