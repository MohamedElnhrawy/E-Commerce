package com.gtera.data.local

import android.content.Context
import android.os.Build
import com.gtera.data.local.preferences.PreferencesHelper
import com.gtera.utils.APPConstants.APP_ARABIC_LANGUAGE
import com.gtera.utils.APPConstants.APP_ENGLISH_LANGUAGE
import java.util.*

object LocaleHelper {


    @JvmStatic
    fun onAttach(context: Context, preferencesHelper:PreferencesHelper): Context {
        return setLocale(
            context,
            preferencesHelper.appLanguage
        )
    }

    fun onAttach(
        context: Context,
        defaultLanguage: String?
    ): Context {
        return setLocale(
            context,
            defaultLanguage
        )
    }

    fun setLocale(
        context: Context,
        language: String?
    ): Context {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLayoutDirection(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
            context = context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return context
    }

    fun changeLanguage(preferencesHelper:PreferencesHelper) {
        var currentLang = preferencesHelper.appLanguage
        if (currentLang == APP_ARABIC_LANGUAGE) currentLang =
            APP_ENGLISH_LANGUAGE else currentLang = APP_ARABIC_LANGUAGE
        preferencesHelper.appLanguage = currentLang
        /* setLocale(BaseApplication.getContext(), currentLang); */
    }
}