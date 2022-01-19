package com.gtera.data.helper.interceptors

import com.gtera.R
import com.gtera.di.providers.ResourceProvider
import com.gtera.utils.Logger.Companion.instance
import com.gtera.utils.Utilities.bodyToString
import com.gtera.utils.Utilities.readJsonFromAssetFile
import okhttp3.*
import java.io.IOException
import java.util.*

class LoggerInterceptor constructor(var resourceProvider: ResourceProvider) : Interceptor {
    var response: Response? = null
    /*
    updated by amra @ 28-03-2020
    adding mocking(reading from local files) lines
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers()

        //get Cached response form local cashed json files in asset folder if enable_mocking is true in configs file
        //just for testing or server down, error purpose.
        if (resourceProvider.getBoolean(R.bool.enable_mocking) || (request.url().toString()
                .substring(
                    request.url().toString().lastIndexOf('/') + 1
                ) ) == "FAQ"
        ) {
            getResponseFromLocalCash(request, chain)
        } else {
            response = chain.proceed(request)
            instance()!!.v("Complain URL", request.url())
            instance()!!.v("Complain Method", request.method())
            instance()!!.v("Complain Headers", headers.toString())
            instance()!!.v(
                "Complain Body",
                (if (request.body() != null) bodyToString(
                    Objects.requireNonNull(
                        request.body()!!
                    )
                ) else "NULL/Empty")!!
            )
            instance()!!.v("Complain Response Code", response!!.code())
        }
        return response!!
    }

    private fun getResponseFromLocalCash(
        request: Request,
        chain: Interceptor.Chain
    ) {
        val path = request.url().toString()
        val fileName = path.substring(path.lastIndexOf('/') + 1) + ".txt"
        val responseBodyString =
            readJsonFromAssetFile(fileName, resourceProvider)
        response = Response.Builder()
            .code(200)
            .message("back with mock data")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_0)
            .body(
                ResponseBody.create(
                    MediaType.parse("application/json"),
                    responseBodyString!!.toByteArray()
                )
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}