package com.gtera.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.gtera.data.local.preferences.PreferencesHelper
import com.gtera.di.providers.BaseResourceProvider
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.splash.SplashViewModel
import com.gtera.utils.APPConstants.ABAZA_SHARED_PREFERENCES
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class ContextModule {
    @Singleton
    @Binds
    abstract fun provideContext(application: Application): Context

    @Singleton
    @Binds
    abstract fun provideResourceProvider(resourceProvider: ResourceProvider): BaseResourceProvider


    @Module
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(ABAZA_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        }

        @Provides
        fun provideSplashViewModelClass(): Class<*>? {
            return SplashViewModel::class.java
        }

        @Singleton
        @Provides
         fun providePreferenceHelper() = PreferencesHelper
    }
}