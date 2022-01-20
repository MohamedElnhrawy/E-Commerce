package com.gtera.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gtera.di.utils.ViewModelKey
import com.gtera.ui.authorization.login.SignInViewModel
import com.gtera.ui.authorization.otp.OtpViewModel
import com.gtera.ui.base.toolbar.ToolbarViewModel
import com.gtera.ui.cart.CartViewModel

import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogItemViewModel
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogViewModel
import com.gtera.ui.dialog.confirmation.ConfirmationDialogViewModel
import com.gtera.ui.dialog.defaultdialog.DefaultDialogViewModel
import com.gtera.ui.dialog.inputdialog.InputDialogViewModel
import com.gtera.ui.home.HomeViewModel
import com.gtera.ui.home.viewmodels.*
import com.gtera.ui.navigation.BottomNavViewModel

import com.gtera.ui.product_details.ProductDetailsViewModel
import com.gtera.ui.profile.ProfileViewModel

import com.gtera.ui.profile.language.LanguageViewModel

import com.gtera.ui.search.SearchViewModel

import com.gtera.ui.slider.SliderViewModel
import com.gtera.ui.splash.SplashViewModel

import com.gtera.ui.webview.WebViewVM
import com.gtera.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignInViewModel(signInViewModel: SignInViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(ConfirmationDialogViewModel::class)
    abstract fun bindConfirmationDialogViewModel(confirmationDialogViewModel: ConfirmationDialogViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(DefaultDialogViewModel::class)
    abstract fun bindDefaultDialogViewModel(defaultDialogViewModel: DefaultDialogViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(ToolbarViewModel::class)
    abstract fun bindToolbarViewModel(toolbarViewModel: ToolbarViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(BottomNavViewModel::class)
    abstract fun bindBottomNavViewModel(bottomNavViewModel: BottomNavViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel?): ViewModel?



    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(SearchViewModel: SearchViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel?): ViewModel?


    @Binds
    abstract fun bindSliderViewModel(sliderViewModel: SliderViewModel?): ViewModel?



    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetDialogViewModel::class)
    abstract fun bindBottomSheetDialogViewModel(bottomSheetDialogViewModel: BottomSheetDialogViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetDialogItemViewModel::class)
    abstract fun bindBottomSheetDialogItemViewModel(bottomSheetDialogItemViewModel: BottomSheetDialogItemViewModel?): ViewModel?




    @Binds
    @IntoMap
    @ViewModelKey(LanguageViewModel::class)
    abstract fun bindLanguageViewModel(languageViewModel: LanguageViewModel?): ViewModel?



    @Binds
    @IntoMap
    @ViewModelKey(WebViewVM::class)
    abstract fun bindWebViewViewModel(webViewVM: WebViewVM?): ViewModel?



    @Binds
    @IntoMap
    @ViewModelKey(InputDialogViewModel::class)
    abstract fun bindInputDialogViewModel(inputDialogViewModel: InputDialogViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailsViewModel::class)
    abstract fun bindProductDetailsViewModel(productDetailsViewModel: ProductDetailsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    abstract fun bindCartViewModel(cartViewModel: CartViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(OtpViewModel::class)
    abstract fun bindOtpViewModel(otpViewModel: OtpViewModel?): ViewModel?

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory?): ViewModelProvider.Factory?
}