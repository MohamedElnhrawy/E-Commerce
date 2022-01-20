package com.gtera.di.module

import com.gtera.di.scope.ActivityScoped
import com.gtera.ui.authorization.login.SignInActivity
import com.gtera.ui.authorization.otp.OtpActivity
import com.gtera.ui.cart.CartActivity
import com.gtera.ui.navigation.BottomNavigationActivity
import com.gtera.ui.product_details.ProductDetailsActivity
import com.gtera.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity?

    @ActivityScoped
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun bindBottomNavigationActivity():BottomNavigationActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): SignInActivity?


    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindCartActivity(): CartActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindOtpActivity(): OtpActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindProductDetailsActivity(): ProductDetailsActivity?

}