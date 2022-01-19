package com.gtera.utils

import android.content.Context
import com.gtera.data.local.preferences.PreferencesHelper
import java.util.*
import javax.inject.Singleton

@Singleton
object LocaleHelper {
    fun onAttach(
        context: Context
    ): Context {
        val preferencesHelper = PreferencesHelper(
            context.getSharedPreferences(
                APPConstants.ABAZA_SHARED_PREFERENCES,
                Context.MODE_PRIVATE
            ),
            context
        )
        val defaultLanguage: String =
            if (Utilities.isNullString(preferencesHelper.appLanguage))
                context.resources.configuration.locale.language
            else preferencesHelper.appLanguage!!
        return setLocale(context, defaultLanguage)
    }

    fun setLocale(
        context: Context,
        language: String
    ): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLayoutDirection(locale)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }
}