package com.gtera.data.helper.interceptors

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import com.gtera.data.local.preferences.PreferencesHelper
import com.gtera.data.model.requests.RefreshTokenRequest
import com.gtera.data.remote.ApiInterface
import com.gtera.data.remote.Endpoints.URL_SEARCH_RELATED
import com.gtera.di.providers.ResourceProvider
import com.gtera.utils.BASE_URL
import com.gtera.utils.Utilities.getDeviceId
import com.gtera.utils.Utilities.isXlargeScreen
import com.jaredrummler.android.device.DeviceName
import com.jaredrummler.android.device.DeviceName.DeviceInfo
import io.michaelrocks.paranoid.Obfuscate
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*

@Obfuscate
class HeaderInterceptor  constructor(
    var resourceProvider: ResourceProvider,
    var context: Context,
    var preferencesHelper: PreferencesHelper
) : Interceptor {
    private val deviceInfo: DeviceInfo? = null

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = addHeaders(
            request,
            getDefaultHeaders(0)
        )
        val initialResponse = chain.proceed(request)

        when {
            initialResponse.code() == 403 || initialResponse.code() == 401 && !request.url().toString().contains(
                URL_SEARCH_RELATED
            ) -> {

                val resfreshTokenResponse =
                    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build()
                        .create(ApiInterface::class.java)
                        .refreshToken(
                            RefreshTokenRequest(preferencesHelper.refreshToken)
                        )?.execute()



                return when {
                    resfreshTokenResponse == null || resfreshTokenResponse.code() != 200 -> {
                        //go to login page
                        preferencesHelper.deleteAccessToken()
                        preferencesHelper.deleteRefreshToken()
                        return initialResponse
                    }

                    else -> {

                        resfreshTokenResponse.body()?.data?.status?.accessToken?.let {
                            preferencesHelper.accessToken = it
                        }
                        resfreshTokenResponse.body()?.data?.status?.refreshToken.let{
                            preferencesHelper.refreshToken = it
                        }
                        request = updateHeaders(
                            request,
                            getDefaultHeaders(0)
                        )
                        request.headers()

                        return chain.proceed(request)
                    }
                }
            }
            else -> return initialResponse
        }
    }



    fun getDefaultHeaders(time_stamp: Long): HashMap<String, String?> {
        val headers =
            HashMap<String, String?>()
        putConnectionType(headers)
        putMobileCodes(headers)
        putAppVersion(headers)
        val model =
            if (deviceInfo != null) deviceInfo.manufacturer + " " + deviceInfo.marketName else DeviceName.getDeviceName()
        headers["Accept-Language"] = preferencesHelper.appLanguage
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        headers["deviceId"] = getDeviceId(context)
        headers["deviceOs"] = "Android"
        headers["deviceType"] = if (isXlargeScreen(context)) "Tablet" else "Mobile"
        headers["deviceName"] = model
        val token: String? = preferencesHelper.accessToken
        if (!token.equals(null)) headers["Authorization"] = "Bearer " + token
        headers["service-time"] = time_stamp.toString()
        return headers
    }

    private fun putMobileCodes(headers: HashMap<String, String?>) {
        val tel =
            context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkOperator =
            tel?.networkOperator
        if (networkOperator != null && networkOperator.length > 0) {
            val mcc = networkOperator.substring(0, 3)
            headers["mcc"] = mcc
            val mnc = networkOperator.substring(3)
            headers["mnc"] = mnc
        }
    }

    private fun putConnectionType(headers: HashMap<String, String?>) {
        val con =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var info: NetworkInfo? = null
        if (con != null) {
            info = con.activeNetworkInfo
        }
        var connectionType = -1
        var connectionValue: String? = null
        if (info != null) connectionType = info.type
        if (connectionType == ConnectivityManager.TYPE_WIFI) connectionValue =
            "wifi" else if (connectionType == ConnectivityManager.TYPE_MOBILE) {
            val networkType = info!!.subtype
            when (networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> connectionValue =
                    "2g"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> connectionValue =
                    "3g"
                TelephonyManager.NETWORK_TYPE_LTE -> connectionValue = "4g"
                else -> {
                }
            }
        }
        if (connectionValue != null) headers["connectionType"] = connectionValue
    }

    private fun putAppVersion(headers: HashMap<String, String?>) {
        try {
            val packageInfo: PackageInfo =
                context.applicationContext.packageManager
                    ?.getPackageInfo( context.applicationContext.packageName, 0)!!
            val versionName = packageInfo.versionName
            if (versionName != null) headers["applicationVersion"] = versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun addHeaders(
        original: Request,
        defaultHeaders: HashMap<String, String?>
    ): Request {
        var headers = original.headers()
        headers = headers.newBuilder()
            .addAll(Headers.of(defaultHeaders))
            .build()
        val secureHeaders = headers
        val requestBuilder = original.newBuilder()
        requestBuilder.headers(secureHeaders)
        return requestBuilder.build()
    }

    private fun updateHeaders(
        original: Request,
        defaultHeaders: HashMap<String, String?>
    ): Request {
        var headers = original.headers()
        headers = headers.newBuilder().removeAll("Authorization").build()
        headers =headers.newBuilder().add("Authorization", defaultHeaders.get("Authorization")!!).build()
        headers = headers.newBuilder()
            .build()
        val secureHeaders = headers
        val requestBuilder = original.newBuilder()
        requestBuilder.headers(secureHeaders)
        return requestBuilder.build()
    }
}