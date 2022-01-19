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
        const val PREF_KEY_APP_LANGUAGE = "PREF_KEY_APP_LANGUAGE"
        private const val PREF_KEY_CONFIGURATION_NOTE = "PREF_KEY_CONFIGURATION_NOTE"
        private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        private const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
        private const val PREF_KEY_DEVICE_TOKEN = "PREF_KEY_DEVICE_TOKEN"
        private const val PREF_KEY_DEVICE_TOKEN_REGISTERED = "PREF_KEY_DEVICE_TOKEN_REGISTERED"
        private const val PREF_KEY_PUSH_NOTIFICATION = "PREF_KEY_PUSH_NOTIFICATION"
        private const val PREF_KEY_APP_VERSION = "PREF_KEY_APP_VERSION"
        private const val PREF_KEY_ACTIVE_USER = "PREF_KEY_ACTIVE_USER"
        private const val PREF_KEY_GUEST_USER = "PREF_KEY_GUEST_USER"
        private const val PREF_KEY_IS_TESTING = "PREF_KEY_IS_TESTING"
        private const val PREF_KEY_APP_INTRO = "PREF_KEY_APP_INTRO"


    }

    private fun initializePreferences() {
        val versionCode: Int
        try {
            val packageInfo = context!!.packageManager
                .getPackageInfo(context!!.packageName, 0)
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
    fun deleteActiveState() {
        prefs.edit().remove(PREF_KEY_ACTIVE_USER).apply()
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
        get() = accessToken != null && !isGuestUser

    val isGuestUser: Boolean
        get() = prefs.getBoolean(PREF_KEY_GUEST_USER, false)

    fun removeGuest() {
        prefs.edit().remove(PREF_KEY_GUEST_USER).apply()
    }

    fun setIsGuest() {
        prefs.edit().putBoolean(PREF_KEY_GUEST_USER, true).apply()
    }

    var isActiveUser: Boolean
        get() = prefs.getBoolean(PREF_KEY_ACTIVE_USER, false)
        set(active) {
            prefs.edit().putBoolean(PREF_KEY_ACTIVE_USER, active).apply()
        }

    var isDeviceTokenRegistered: Int
        get() = prefs.getInt(PREF_KEY_DEVICE_TOKEN_REGISTERED, -1)
        set(isRegistered) {
            prefs.edit()
                .putInt(PREF_KEY_DEVICE_TOKEN_REGISTERED, isRegistered)
                .apply()
        }

    fun deleteDeviceToken() {
        prefs.edit().remove(PREF_KEY_DEVICE_TOKEN).apply()
    }

    var deviceToken: String?
        get() = prefs.getString(PREF_KEY_DEVICE_TOKEN, null)
        set(token) {
            prefs.edit().putString(PREF_KEY_DEVICE_TOKEN, token).apply()
        }

    var isTestingBuild: Boolean
        get() = prefs.getBoolean(PREF_KEY_IS_TESTING, false)
        set(isTestingBuild) {
            prefs.edit().putBoolean(PREF_KEY_IS_TESTING, isTestingBuild)
                .apply()
        }

    var configurationNote: String?
        get() = prefs.getString(PREF_KEY_CONFIGURATION_NOTE, null)
        set(note) {
            prefs.edit().putString(PREF_KEY_CONFIGURATION_NOTE, note)
                .apply()
        }

    val isIntroAlreadyShown: Boolean
        get() = prefs.getBoolean(PREF_KEY_APP_INTRO, false)

    fun setIntroAlreadyShown() {
        prefs.edit().putBoolean(PREF_KEY_APP_INTRO, true).apply()
    }



}