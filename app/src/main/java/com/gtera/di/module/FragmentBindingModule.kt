package com.gtera.di.module

import com.gtera.ui.home.HomeFragment
import com.gtera.ui.profile.ProfileFragment
import com.gtera.ui.profile.language.LanguageFragment
import com.gtera.ui.search.SearchFragment
import com.gtera.ui.webview.WebViewFragment
import com.gtera.di.scope.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment?


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideSearchFragment(): SearchFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): ProfileFragment?




    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideLanguageFragment(): LanguageFragment?



    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideWebViewFragment(): WebViewFragment?





}