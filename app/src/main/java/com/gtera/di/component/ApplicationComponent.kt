package com.gtera.di.component

import android.app.Application
import com.gtera.base.BaseApplication
import com.gtera.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class,
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    PersistenceModule::class,
    ViewModelModule::class,
    FragmentBindingModule::class]
    )
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
    fun inject(application: BaseApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}