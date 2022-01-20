package com.gtera.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.gtera.R
import com.gtera.data.error.ErrorDetails
import com.gtera.di.providers.ResourceProvider
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
object Utilities {


    fun getErrorMessage(throwable: Throwable): String? {
        val responseBody: ResponseBody? =
            Objects.requireNonNull((throwable as HttpException).response())?.errorBody()
        val errorBody: Reader? = Objects.requireNonNull(responseBody)?.charStream()
        try {
            val errorStr = responseBody?.string()
            try {
                val jsonObject = JSONObject(errorStr)
                return jsonObject.optString("message")
            } catch (e: JSONException) {
                e.printStackTrace()
                try {
                    val gson = Gson()
                    val errorMessage: ErrorDetails =
                        gson.fromJson(errorBody, ErrorDetails::class.java)
                    return errorMessage.errorMsg
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    fun getAppDefaultLanguage(context: Context): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.resources.configuration.locales[0].language
        else
            context.resources.configuration.locale.language
    }

    /*
     * Check if String is Null/empty or not
     */
    fun isNullString(str: String?): Boolean { //        return !(str != null && str.compareToIgnoreCase("null") != 0 && str.trim().length() > 0);
        return TextUtils.isEmpty(str) || str?.compareTo("null", ignoreCase = true) == 0
    }


    fun getDeviceId(context: Context): String? {
        var deviceId: String? = ""
        try {
            deviceId = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        } catch (e: java.lang.Exception) {
        }
        if (context != null && isNullString(deviceId!!)) {
            val tel =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tel.imei
        }
        return deviceId
    }


    fun hideSoftKeyboard(activity: Activity) {
        val imm = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        view.clearFocus()
    }

    @JvmStatic
    fun bodyToString(request: RequestBody): String? {
        return try {
            val buffer = Buffer()
            request.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }


    /*read json file from the assets folder and returns a string of it's content */
    fun readJsonFromAssetFile(fileName: String?, resourceProvider: ResourceProvider): String? {
        var jsonResponse: String? = null
        try {
            jsonResponse = ""
            val stringBuilder = StringBuilder()
            val inputStream: InputStream =
                resourceProvider.getAsset().open(fileName!!)
            val reader =
                BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            jsonResponse = stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return jsonResponse
    }


    fun hideSoftKeyboard(
        context: Context,
        view: View
    ) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }


    fun isXlargeScreen(context: Context): Boolean {
        return context.resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_XLARGE
    }


    fun isNullList(list: List<*>?): Boolean {
        return !(list != null && list.size > 0)
    }


    @SuppressLint("CheckResult")
    fun getImagePlaceholder(context: Context): RequestOptions? {
        val options = RequestOptions()
        options.placeholder(getDrawableLoadingRes(context))
        options.error(R.color.placeHolderColor)
        return options
    }


    @SuppressLint("ResourceAsColor")
    fun getDrawableLoadingRes(context: Context): CircularProgressDrawable? {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 50f
        circularProgressDrawable.setColorSchemeColors(
            context.resources.getColor(
                R.color.primary_text
            )
        )
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    fun isValidPassword(password: CharSequence): Boolean {
        return password.length >= 6
    }

    /* * email address validation */
    fun isValidEmail(target: CharSequence): Boolean {
        val regex: Regex =
            "(^[_A-Za-z0-9-+]{2,}(\\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9-]{2,}(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,4})$)".toRegex()
        return target.toString().trim().matches(regex)
    }

    fun isValidPersonName(name: String): Boolean {
        val regex = "^[A-Za-z][A-Za-z ]{2,19}$"
        val regex_ar =
            "^[\u0621-\u064A\u0660-\u0669][\u0621-\u064A\u0660-\u0669 ]{2,19}$"
        return name.matches(regex.toRegex()) || name.matches(regex_ar.toRegex())
    }

    fun isValidChassisNumber(chassisNumber: String): Boolean {
        val regex = "^[+]?[_A-Za-z0-9-+]{5,20}$"
        return chassisNumber.matches(regex.toRegex())
    }

    fun formatDate(oldDate: Date?): String? {
        val sdf_newFormat =
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        sdf_newFormat.timeZone = TimeZone.getDefault()
        return sdf_newFormat.format(oldDate)
    }

    fun formatDateTime(oldDate: Date?): String? {
        val sdf_newFormat =
            SimpleDateFormat("MMMM dd, yyyy, hh:mm", Locale.getDefault())
        sdf_newFormat.timeZone = TimeZone.getDefault()
        return sdf_newFormat.format(oldDate)
    }

    fun isValidPhoneNumber(phoneNumber: CharSequence): Boolean {

        val regex = "^[+]?[0-9]{8,15}$"
        val regex_ar = "^[+]?[\\u0660-\\u0669]{8,15}$"
        return phoneNumber.toString().matches(regex.toRegex()) || phoneNumber.toString()
            .matches(regex_ar.toRegex())
    }

    fun getColorFromRes(context: Context, @ColorRes id: Int): Int {
        return ContextCompat.getColor(context, id)
    }
    @SuppressLint("CheckResult")
    fun getImageLoadingOptions(context: Context): RequestOptions {
        val options = RequestOptions()
        options.placeholder(Utilities.getDrawableLoadingRes(context))
        options.error(R.color.grey_500)
        return options
    }

    fun reFormatDateEnglish(oldDate: String): String {
        val sdf_oldFormat = SimpleDateFormat("dd MMMM yyyy - hh:mm a", Locale.getDefault())
        sdf_oldFormat.timeZone = TimeZone.getTimeZone("UTC")
        val sdf_newFormat = SimpleDateFormat("MMMM dd,yyyy - hh:mm a", Locale.US)
        sdf_newFormat.timeZone = TimeZone.getDefault()
        if (Locale.getDefault().language == "ar")
            return reFormatDateTime(oldDate, sdf_oldFormat, sdf_newFormat)
        else
            return oldDate
    }

    fun reFormatDateTime(oldDate: String): String {
        val sdf_oldFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        sdf_oldFormat.timeZone = TimeZone.getTimeZone("UTC")
        val sdf_newFormat: SimpleDateFormat
        if (Locale.getDefault().language == "ar")
            sdf_newFormat = SimpleDateFormat("dd MMMM yyyy - hh:mm a", Locale.getDefault())
        else
            sdf_newFormat = SimpleDateFormat("MMMM dd,yyyy - hh:mm a", Locale.getDefault())

        sdf_newFormat.timeZone = TimeZone.getDefault()
        return reFormatDateTime(oldDate, sdf_oldFormat, sdf_newFormat)
    }


    fun reFormatDate(oldDate: String): String {
        val sdf_oldFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        sdf_oldFormat.timeZone = TimeZone.getTimeZone("UTC")
        val sdf_newFormat = SimpleDateFormat("dd/mm/yyyy ", Locale.getDefault())
        sdf_newFormat.timeZone = TimeZone.getDefault()
        return reFormatDateTime(oldDate, sdf_oldFormat, sdf_newFormat)
    }


    fun reFormatDateTime(
        oldDate: String,
        sdf_oldFormat: SimpleDateFormat,
        sdf_newFormat: SimpleDateFormat
    ): String {
        var newString = ""
        try {
            val date = sdf_oldFormat.parse(oldDate)
            newString = sdf_newFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return newString
    }






    fun formatNumbers(number: String?, isProductQt: Boolean): String? {
        var output = ""
        number?.let {

        output =
            (if (Locale.getDefault().language == "ar") if (isProductQt) "x" +
                changeNumbersToArabic(
                    it
                ) else  changeNumbersToArabic(it)
            else if (isProductQt)
                changeNumbersToEnglish(
                    it
                )
             + "x" else  changeNumbersToEnglish(it) )!!
        }

        return output
    }


    fun changeNumbersToArabic(input: String): String? {
        return input.replace("0".toRegex(), Character.toString('\u0660'))
            .replace("1".toRegex(), Character.toString('\u0661'))
            .replace("2".toRegex(), Character.toString('\u0662'))
            .replace("3".toRegex(), Character.toString('\u0663'))
            .replace("4".toRegex(), Character.toString('\u0664'))
            .replace("5".toRegex(), Character.toString('\u0665'))
            .replace("6".toRegex(), Character.toString('\u0666'))
            .replace("7".toRegex(), Character.toString('\u0667'))
            .replace("8".toRegex(), Character.toString('\u0668'))
            .replace("9".toRegex(), Character.toString('\u0669'))
    }

    fun changeNumbersToEnglish(input: String): String? {
        return input.replace("[\u0660\u06f0]".toRegex(), "0")
            .replace("[\u0661\u06f1]".toRegex(), "1")
            .replace("[\u0662\u06f2]".toRegex(), "2")
            .replace("[\u0663\u06f3]".toRegex(), "3")
            .replace("[\u0664\u06f4]".toRegex(), "4")
            .replace("[\u0665\u06f5]".toRegex(), "5")
            .replace("[\u0666\u06f6]".toRegex(), "6")
            .replace("[\u0667\u06f7]".toRegex(), "7")
            .replace("[\u0668\u06f8]".toRegex(), "8")
            .replace("[\u0669\u06f9]".toRegex(), "9")
    }

    fun bitmapToBase64(bitmap: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun getDimenValueFromRes(resId: Int, context: Context): Float {
        return context.getResources().getDimension(resId)
    }

    fun toCalendar(date: Date?): Calendar? {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    fun stringToData(text:String?) : Date?{
        return if (text != null)
            SimpleDateFormat("yyyy-MM-dd").parse(text)
        else null
    }

    fun getCurrentDate(): Date {
        val stamp = Timestamp(System.currentTimeMillis())
        return java.sql.Date(stamp.time)
    }

    fun getMessagesDate(dateString: String?, context: Context): String? {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString)
        val currentDate = Calendar.getInstance()
        val cal = Calendar.getInstance()
        cal.time = date

        if (cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
            cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
            cal.get(Calendar.WEEK_OF_MONTH) == currentDate.get(Calendar.WEEK_OF_MONTH) &&
            cal.get(Calendar.DAY_OF_WEEK) == currentDate.get(Calendar.DAY_OF_WEEK) &&
            cal.get(Calendar.HOUR_OF_DAY) == currentDate.get(Calendar.HOUR_OF_DAY)
        )
            return (currentDate.get(Calendar.MINUTE) - cal.get(Calendar.MINUTE)).toString() + " " + context.getString(
                R.string.str_messages_ago
            )
        else if (cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
            cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
            cal.get(Calendar.WEEK_OF_MONTH) == currentDate.get(Calendar.WEEK_OF_MONTH) &&
            cal.get(Calendar.DAY_OF_WEEK) == currentDate.get(Calendar.DAY_OF_WEEK)
        )
            return context.getString(R.string.str_messages_today) + " " + SimpleDateFormat("HH:mm a").format(
                date
            )
        else if (cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
            cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
            cal.get(Calendar.WEEK_OF_MONTH) == currentDate.get(Calendar.WEEK_OF_MONTH)
        )
            return SimpleDateFormat("EEE").format(
                date
            )
        else if (cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
            cal.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)
        )
            return SimpleDateFormat("MMM dd").format(
                date
            )
        else if (cal.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
        )
            return SimpleDateFormat("MMM dd").format(
                date
            )
        else
            return SimpleDateFormat("MMM dd,yyyy").format(
                date
            )
    }
}