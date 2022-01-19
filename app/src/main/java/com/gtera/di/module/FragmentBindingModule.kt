package com.gtera.di.module

import com.gtera.ui.brands.BrandsFragment
import com.gtera.ui.calculateit.CalculateItFragment
import com.gtera.ui.calculateit.result.CalculateItResultsFragment
import com.gtera.ui.cardetials.CarDetailsFragment
import com.gtera.ui.cars.CarsFragment
import com.gtera.ui.cars_filter.CarsFilterFragment
import com.gtera.ui.choosebudget.ChooseYourBudgetFragment
import com.gtera.ui.choosebudget.carlist.BudgetCarListFragment
import com.gtera.ui.home.HomeFragment
import com.gtera.ui.models.ModelsFragment
import com.gtera.ui.mycars.MyCarsFragment
import com.gtera.ui.mycars.carcompare.list.CarCompareListFragment
import com.gtera.ui.mycars.carcompare.result.CarCompareDetailsFragment
import com.gtera.ui.mycars.insurance.carinsurancerenewal.RequestCarInsuranceFragment
import com.gtera.ui.mycars.insurance.endorsement.EndorsementFragment
import com.gtera.ui.mycars.insurance.insurancedetails.InsuranceDetailsFragment
import com.gtera.ui.mycars.insurance.insurancerequeatslist.InsuranceListFragment
import com.gtera.ui.mycars.insurance.online_payment.OnlinePaymentFragment
import com.gtera.ui.mycars.list.MyCarsListFragment
import com.gtera.ui.mycars.maintenance.MaintenanceListListFragment
import com.gtera.ui.mycars.maintenance.add_maintenance.NewMaintenanceFragment
import com.gtera.ui.news.NewsFragment
import com.gtera.ui.news.newsdetails.NewsDetailsFragment
import com.gtera.ui.offers.OffersFragment
import com.gtera.ui.ordernow.OrderNowFragment
import com.gtera.ui.ordernow.acknowledgment.AcknowledgmentFragment
import com.gtera.ui.ordernow.details.OrderDetailsFragment
import com.gtera.ui.ordernow.list.OrderListFragment
import com.gtera.ui.ordernow.pay.OrderOnlinePaymentFragment
import com.gtera.ui.profile.ProfileFragment
import com.gtera.ui.profile.branches.BranchesFragment
import com.gtera.ui.profile.contactus.ContactUsFragment
import com.gtera.ui.profile.edit.EditProfileFragment
import com.gtera.ui.profile.faq.FAQFragment
import com.gtera.ui.profile.favorites.FavoritesFragment
import com.gtera.ui.profile.info.ProfileInfoFragment
import com.gtera.ui.profile.language.LanguageFragment
import com.gtera.ui.profile.messages.MessagesListFragment
import com.gtera.ui.profile.messages.details.MessageDetailsFragment
import com.gtera.ui.profile.notifications.NotificationListFragment
import com.gtera.ui.search.SearchFragment
import com.gtera.ui.search.filter.SearchFilterFragment
import com.gtera.ui.search.result.SearchResultFragment
import com.gtera.ui.topdeals.TopDealsFragment
import com.gtera.ui.usedcardetails.UsedCarDetailsFragment
import com.gtera.ui.webview.WebViewFragment
import com.example.firestoresmartchatmvvm.injection.scope.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideMyCarsFragment(): MyCarsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideSearchFragment(): SearchFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideProfileFragment(): ProfileFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideBrandsFragment(): BrandsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideModelsFragment(): ModelsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideCarsFragment(): CarsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideCarDetailsFragment(): CarDetailsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideHotDealsFragment(): TopDealsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideOffersFragment(): OffersFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideNewsFragment(): NewsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideNewsDetailsFragment(): NewsDetailsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideChooseYourBudgetFragment(): ChooseYourBudgetFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideBudgetCarListFragment(): BudgetCarListFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideCarCompareListFragment(): CarCompareListFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideCarCompareDetailsFragment(): CarCompareDetailsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideProfileInfoFragment(): ProfileInfoFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideEditProfileFragment(): EditProfileFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideSearchResultFragment(): SearchResultFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideSearchFilterFragment(): SearchFilterFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideCarsFilterFragment(): CarsFilterFragment?


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideLanguageFragment(): LanguageFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideRequestCarInsuranceFragment(): RequestCarInsuranceFragment?


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideInsuranceListFragment(): InsuranceListFragment?


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideInsuranceDetailsFragment(): InsuranceDetailsFragment?


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideFavoritesFragment(): FavoritesFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideEndorsementFragment(): EndorsementFragment?


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideUsedCarDetailsFragment(): UsedCarDetailsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideCalculateItFragment(): CalculateItFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideCalculateItResultsFragment(): CalculateItResultsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideMyCarsListFragment(): MyCarsListFragment?



    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideBranchesFragment(): BranchesFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideMaintenanceFragment(): MaintenanceListListFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideNewMaintenanceFragment(): NewMaintenanceFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideContactUsFragment(): ContactUsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideWebViewFragment(): WebViewFragment?


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideOnlinePaymentFragment(): OnlinePaymentFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideMessagesListFragment(): MessagesListFragment?


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideNotificationListFragment(): NotificationListFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideMessageDetailsFragment(): MessageDetailsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideFAQFragment(): FAQFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideOrderNowFragment(): OrderNowFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideAcknowledgmentFragment(): AcknowledgmentFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideOrderListFragment(): OrderListFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideOrderDetailsFragment(): OrderDetailsFragment?

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun provideOrderOnlinePaymentFragment(): OrderOnlinePaymentFragment?


}