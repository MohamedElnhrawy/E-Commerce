package com.gtera.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.pm.PackageInfoCompat
import com.gtera.utils.Utilities.getAppDefaultLanguage
import io.michaelrocks.paranoid.Obfuscate
import javax.inject.Inject
import javax.inject.Singleton

@Obfuscate
@Singleton
class PreferencesHelper @Inject constructor(private val prefs: SharedPreferences,  val context: Context) {

    init {

        if (prefs.getInt(
                PREF_KEY_APP_VERSION,
                -1
            ) == -1
        ) initializePreferences()
    }
    companion object {
        const val PREF_KEY_APP_LANGUAGE = "PREF_KEY_GTERA_APP_LANGUAGE"
        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val PREF_KEY_PUSH_NOTIFICATION = "PREF_KEY_PUSH_NOTIFICATION"
        private const val PREF_KEY_APP_VERSION = "PREF_KEY_APP_VERSION"
        private const val PREF_KEY_ACTIVE_USER = "PREF_KEY_ACTIVE_USER"


    }

    private fun initializePreferences() {
        val versionCode: Int
        try {
            val packageInfo = context.packageManager
                .getPackageInfo(context.packageName, 0)
            versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toInt()
            setAppVersion(versionCode)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    fun setAppVersion(versionCode: Int) {
        prefs.edit().putInt(PREF_KEY_APP_VERSION, versionCode).apply()
    }

    var appLanguage: String?
        get() = prefs.getString(
            PREF_KEY_APP_LANGUAGE,
            getAppDefaultLanguage(context)
        )
        set(language) {
            prefs.edit().putString(PREF_KEY_APP_LANGUAGE, language).apply()
        }

    val isPushNotificationEnabled: Boolean
        get() = prefs.getBoolean(PREF_KEY_PUSH_NOTIFICATION, true)

    fun pushNotificationUpdate(isEnabled: Boolean) {
        prefs.edit().putBoolean(PREF_KEY_PUSH_NOTIFICATION, isEnabled)
            .apply()
    }

    fun deleteAccessToken() {
        prefs.edit().remove(PREF_KEY_ACCESS_TOKEN).apply()
    }

    fun deleteRefreshToken() {
        prefs.edit().remove(PREF_KEY_REFRESH_TOKEN).apply()
    }


    var accessToken: String?
        get() = prefs.getString(PREF_KEY_ACCESS_TOKEN, null)
        set(token) {
            prefs.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply()
        }

    var refreshToken: String?
        get() = prefs.getString(PREF_KEY_REFRESH_TOKEN, null)
        set(token) {
            prefs.edit().putString(PREF_KEY_REFRESH_TOKEN, token).apply()
        }


    val isUserLoggedIn: Boolean
        get() = prefs.getBoolean(PREF_KEY_ACTIVE_USER, false)

    fun setIsUserLoggedIn(status:Boolean) {
        prefs.edit().putBoolean(PREF_KEY_ACTIVE_USER, status).apply()
    }





}