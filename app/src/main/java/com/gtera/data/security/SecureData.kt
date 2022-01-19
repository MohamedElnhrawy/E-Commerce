package com.gtera.data.security

import android.content.Context
import android.util.Base64
import com.gtera.data.local.preferences.PreferencesHelper
import com.gtera.utils.Utilities.getDeviceId
import com.google.gson.Gson
import okhttp3.*
import okio.Okio
import java.io.ByteArrayInputStream
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object SecureData {
    //Back-end Constants
    private const val DEVICE_OS = "Android"
    private const val AUTHORIZATION_KEY = "Bearer "
    private const val HEADER_KEY = "Accept"
    private const val SECURE_ALGORITHM = "AES"

    //CFB Constants
    private const val AES_ALGORITHM_CFB = "AES/CFB/NoPadding"

    //Digest Constants
    private const val DIGEST_ALGORITHM = "HmacSHA256"

    // AES CFB Algorithm decryption
    @JvmStatic
    fun decryptResponse_CFB(
        response: Response,
        method: String?,
        time_stamp: Long,
        accessToken: String,
        context: Context
    ): Response {
        try {
            val dataBytes =
                Objects.requireNonNull(response.body())!!.bytes()
            var x = decryptResponse_CFB(
                dataBytes,
                method!!,
                time_stamp,
                accessToken,
                context
            )
            if (x.indexOf('[') == x.indexOf('{')) return response
            val start =
                if (x.indexOf('[') == -1 || x.indexOf('{') != -1 && x.indexOf('{') < x.indexOf('[')) '{' else '['
            val end = if (start == '[') ']' else '}'
            x = x.substring(x.indexOf(start), x.lastIndexOf(end) + 1)
            val responseBody = ResponseBody.create(
                MediaType.parse("application/json"),
                x.length.toLong(),
                Okio.buffer(Okio.source(ByteArrayInputStream(x.toByteArray())))
            )
            return response.newBuilder().body(responseBody).build()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidAlgorithmParameterException::class
    )
    private fun decryptResponse_CFB(
        data: ByteArray,
        method: String,
        time_stamp: Long,
        accessToken: String,
        context: Context
    ): String {
        val ivParameterSpec = IvParameterSpec(
            getIvKey(
                method,
                time_stamp,
                accessToken,
                context
            )
        )
        val cipher =
            Cipher.getInstance(SecureData.AES_ALGORITHM_CFB)
        val secretKeySpec = SecretKeySpec(
            getPrivateKey(
                method,
                time_stamp,
                accessToken,
                context
            ), SecureData.SECURE_ALGORITHM
        )
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        val result =
            cipher.doFinal(Base64.decode(data, Base64.NO_WRAP))
        return String(Base64.decode(result, Base64.NO_WRAP))
    }

    // AES CFB Algorithm encryption
    @JvmStatic
    fun encryptRequest_CFB(
        original: Request,
        requestData: String?,
        time_stamp: Long,
        accessToken: String,
        context: Context
    ): Request {
        try {
            val encrypted = encrypt_CFB(
                original.method(),
                requestData!!,
                time_stamp,
                accessToken,
                context
            )
            val requestBody =
                RequestBody.create(MediaType.parse("text/plain"), encrypted)
            val requestBuilder = original.newBuilder()
            requestBuilder.method(original.method(), requestBody)
            return requestBuilder.build()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return original
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    private fun encrypt_CFB(
        method: String,
        data: String,
        time_stamp: Long,
        accessToken: String,
        context: Context
    ): String {
        val ivParameterSpec = IvParameterSpec(
            getIvKey(
                method,
                time_stamp,
                accessToken,
            context)
        )
        val cipher =
            Cipher.getInstance(SecureData.AES_ALGORITHM_CFB)
        val secretKeySpec = SecretKeySpec(
            getPrivateKey(method, time_stamp, accessToken, context),
            SecureData.SECURE_ALGORITHM
        )
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val res = cipher.doFinal(
            Base64.encode(
                data.toByteArray(),
                Base64.NO_WRAP
            )
        )
        return Base64.encodeToString(res, Base64.NO_WRAP)
    }

    fun encrypt_CFB(
        method: String?,
        headers: Headers,
        time_stamp: Long,
        accessToken: String,
        context: Context
    ): Headers {
        try {
            val headerJson =
                Gson().toJson(headersToMap(headers))
            var encryptHeader =
                encrypt_CFB(
                    method!!,
                    Base64.encodeToString(
                        headerJson.toByteArray(),
                        Base64.NO_WRAP
                    ),
                    time_stamp,
                    accessToken,
                    context
                )
            encryptHeader = Base64.encodeToString(
                encryptHeader.toByteArray(),
                Base64.NO_WRAP
            )
            val privateKeyEncoded = Base64.encodeToString(
                getPrivateKey(
                    method,
                    time_stamp,
                    accessToken,
                    context
                ), Base64.NO_WRAP
            )
            val ivKeyEncode = Base64.encodeToString(
                getIvKey(
                    method,
                    time_stamp,
                    accessToken,
                    context
                ), Base64.NO_WRAP
            )
            val keys = privateKeyEncoded + ivKeyEncode
            val index = encryptHeader.length / 2
            var headersData = encryptHeader.substring(0, index)
            headersData = headersData + keys
            headersData = headersData + encryptHeader.substring(index)
            return Headers.Builder()
                .set(
                    SecureData.HEADER_KEY,
                    Base64.encodeToString(
                        headersData.toByteArray(),
                        Base64.NO_WRAP
                    )
                )
                .build()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return headers
    }

    private fun headersToMap(headers: Headers): Map<String, String> {
        val result: MutableMap<String, String> =
            TreeMap()
        var i = 0
        val size = headers.size()
        while (i < size) {
            val name = headers.name(i)
            result[name] = headers.value(i)
            i++
        }
        return result
    }

    //Dynamic Digest Creator
    fun getDigest(
        method: String,
        time_stamp: Long,
        preferencesHelper: PreferencesHelper,
        context: Context
    ): String {
        val deviceId = getDeviceId(context)
        val token: String? = preferencesHelper.accessToken
        val authorization =
            if (token == null) "" else SecureData.AUTHORIZATION_KEY + token
        val mTime = time_stamp / 1000
        val mTimeStamp = Base64.encodeToString(
            mTime.toString().toByteArray(),
            Base64.NO_WRAP
        )
        val data =
            authorization + method + deviceId + SecureData.DEVICE_OS + mTimeStamp
        var returnString = ""
        try {
            val mac =
                Mac.getInstance(SecureData.DIGEST_ALGORITHM)
            val keySpec = SecretKeySpec(
                getDigestSecret(
                    mTimeStamp,
                    authorization,
                    deviceId!!,
                    method
                ),
                SecureData.DIGEST_ALGORITHM
            )
            mac.init(keySpec)
            val dataByteArray = mac.doFinal(data.toByteArray())
            returnString =
                Base64.encodeToString(dataByteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return returnString
    }

    private fun getDigestSecret(
        timeStamp: String, authorization: String,
        deviceId: String, method: String
    ): ByteArray {
        val secretKey =
            timeStamp + deviceId + method + authorization + SecureData.DEVICE_OS
        val result =
            Base64.encodeToString(secretKey.toByteArray(), Base64.NO_WRAP)
        val index = result.length - 32
        var res = result.substring(0, 32)
        res = res + result.substring(index)
        return res.toByteArray()
    }

    private fun getIvKey(
        method: String,
        time_stamp: Long,
        accessToken: String,
        context: Context
    ): ByteArray {
        val deviceId = getDeviceId(context)
        val token: String? = accessToken
        val authorization =
            if (token == null) "" else SecureData.AUTHORIZATION_KEY + token
        val mTime = time_stamp / 1000
        val mTimeStamp = Base64.encodeToString(
            mTime.toString().toByteArray(),
            Base64.NO_WRAP
        )
        val data =
            authorization + deviceId + mTimeStamp + method + SecureData.DEVICE_OS
        val iv_encode =
            Base64.encodeToString(data.toByteArray(), Base64.NO_WRAP)
        val index = iv_encode.length - 8
        var res = iv_encode.substring(0, 8)
        res = res + iv_encode.substring(index)
        return res.toByteArray()
    }

    private fun getPrivateKey(
        method: String,
        time_stamp: Long,
        accessToken: String,
        context: Context
    ): ByteArray {
        val deviceId = getDeviceId(context)
        val token: String? = accessToken
        val authorization =
            if (token == null) "" else SecureData.AUTHORIZATION_KEY + token
        val mTime = time_stamp / 1000
        val mTimeStamp = Base64.encodeToString(
            mTime.toString().toByteArray(),
            Base64.NO_WRAP
        )
        val data =
            deviceId + authorization + mTimeStamp + SecureData.DEVICE_OS + method
        val private_encode =
            Base64.encodeToString(data.toByteArray(), Base64.NO_WRAP)
        val index = private_encode.length - 16
        var res = private_encode.substring(0, 16)
        res = res + private_encode.substring(index)
        return res.toByteArray()
    }
}