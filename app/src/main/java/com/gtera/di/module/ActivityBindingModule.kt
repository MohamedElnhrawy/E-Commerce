package com.gtera.di.module

import com.gtera.ui.authorization.changepassword.ChangePasswordActivity
import com.gtera.ui.authorization.login.SignInActivity
import com.gtera.ui.authorization.register.SignUpActivity
import com.gtera.ui.authorization.resetpassword.ResetPasswordActivity
import com.gtera.ui.authorization.resetpasswordconfirmation.ResetPasswordConfirmationActivity
import com.gtera.ui.cars_filter.CarsFilterActivity
import com.gtera.ui.mycars.add.AddMyCarActivity
import com.gtera.ui.mycars.add.selector.SelectorActivity
import com.gtera.ui.navigation.BottomNavigationActivity
import com.gtera.ui.splash.SplashActivity
import com.example.firestoresmartchatmvvm.injection.scope.ActivityScoped
import com.gtera.ui.product_details.ProductDetailsActivity
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
    abstract fun bindRegisterActivity(): SignUpActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindForgotPasswordActivity(): ResetPasswordActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindResetPasswordConfirmationActivity(): ResetPasswordConfirmationActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindChangePasswordActivity(): ChangePasswordActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindCarsFilterActivity(): CarsFilterActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindSelectorActivity(): SelectorActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindAddMyCarActivity():AddMyCarActivity?

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindProductDetailsActivity(): ProductDetailsActivity?

}