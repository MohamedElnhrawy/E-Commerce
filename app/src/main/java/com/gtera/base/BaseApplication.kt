package com.gtera.base

import android.content.Context
import com.gtera.di.component.ApplicationComponent
import com.gtera.di.component.DaggerApplicationComponent
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseAppRoute
import com.gtera.utils.LocaleHelper
import dagger.android.DaggerApplication
import javax.inject.Inject

class BaseApplication : DaggerApplication() {

    @Inject
    lateinit var resourceProvider: ResourceProvider


    private val application: BaseApplication? = null
    override fun onCreate() {
        super.onCreate()
    }

    internal var appRoute: BaseAppRoute? = null

    override fun applicationInjector(): ApplicationComponent? {
        val component =
            DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)
        return component
    }

    fun getAppRoute(): BaseAppRoute? {

        if (this.appRoute == null) this.appRoute = BaseAppRoute(resourceProvider)
        return this.appRoute
    }

    fun getApplication(): BaseApplication? {
        return application
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base!!))
    }


}