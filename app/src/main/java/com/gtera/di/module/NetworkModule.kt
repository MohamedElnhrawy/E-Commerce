package com.gtera.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull
import com.gtera.data.helper.interceptors.HeaderInterceptor
import com.gtera.data.helper.interceptors.LoggerInterceptor
import com.gtera.data.local.preferences.PreferencesHelper
import com.gtera.data.remote.ApiInterface
import com.gtera.di.providers.ResourceProvider
import com.gtera.utils.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module()
class NetworkModule {

    @Singleton
    @Provides
    fun provideLoggerInterceptor(
        resourceProvider: ResourceProvider
    ): LoggerInterceptor {
        return LoggerInterceptor(
            resourceProvider
        )
    }

    @Singleton
    @Provides
    fun providePreferenceHelper(
        context: Context,
        sharedPreferences: SharedPreferences
    ): PreferencesHelper {

        return PreferencesHelper(sharedPreferences, context)
    }


    @Singleton
    @Provides
    fun provideHeaderInterceptor(
        resourceProvider: ResourceProvider,
        context: Context,
        preferencesHelper: PreferencesHelper
    ): HeaderInterceptor {

        return HeaderInterceptor(
            resourceProvider,
            context,
            preferencesHelper
        )
    }


    @Provides
    @Singleton
    fun provideHttpClient(
        resourceProvider: ResourceProvider,
        headerInterceptor: HeaderInterceptor,
        loggerInterceptor: LoggerInterceptor
    ): OkHttpClient {


        //Request Encryption
        //Request Encryption
//        if (resourceProvider.getBoolean(R.bool.enable_encryption))
//            okHttpClient.interceptors().add(EncryptionInterceptor())
//
//        if (resourceProvider.getBoolean(R.bool.enable_decryption))
//            okHttpClient.interceptors().add(DecryptionInterceptor())

        //Logging Request Data
//        if (BuildConfig.DEBUG)
//        okHttpClient.interceptors().add(LoggerInterceptor())

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggerInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }


}