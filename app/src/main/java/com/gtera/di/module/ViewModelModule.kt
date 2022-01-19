package com.gtera.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gtera.di.utils.ViewModelKey
import com.gtera.ui.authorization.login.SignInViewModel
import com.gtera.ui.authorization.register.SignUpViewModel
import com.gtera.ui.authorization.resetpassword.ChangePasswordViewModel
import com.gtera.ui.authorization.resetpassword.ResetPasswordViewModel
import com.gtera.ui.authorization.resetpasswordconfirmation.ResetPasswordConfirmationViewModel
import com.gtera.ui.base.toolbar.ToolbarViewModel
import com.gtera.ui.brands.BrandsViewModel
import com.gtera.ui.calculateit.CalculateItVM
import com.gtera.ui.calculateit.result.CalculateItCarItemVM
import com.gtera.ui.calculateit.result.CalculateItResultsVM
import com.gtera.ui.cardetials.CarDetailsViewModel
import com.gtera.ui.cars.CarsViewModel
import com.gtera.ui.cars_filter.CarsFilterViewModel
import com.gtera.ui.cars_filter.FilterItemViewModel
import com.gtera.ui.choosebudget.ChooseYourBudgetViewModel
import com.gtera.ui.choosebudget.carlist.BudgetCarListViewModel
import com.gtera.ui.choosebudget.viewmodels.BudgetBrandItemViewModel
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogItemViewModel
import com.gtera.ui.dialog.bottomsheet.BottomSheetDialogViewModel
import com.gtera.ui.dialog.confirmation.ConfirmationDialogViewModel
import com.gtera.ui.dialog.defaultdialog.DefaultDialogViewModel
import com.gtera.ui.dialog.inputdialog.InputDialogViewModel
import com.gtera.ui.home.HomeViewModel
import com.gtera.ui.home.viewmodels.*
import com.gtera.ui.models.ModelsViewModel
import com.gtera.ui.mycars.MyCarItemViewModel
import com.gtera.ui.mycars.MyCarsViewModel
import com.gtera.ui.mycars.add.AddMyCarViewModel
import com.gtera.ui.mycars.add.selector.SelectorItemViewModel
import com.gtera.ui.mycars.add.selector.SelectorViewModel
import com.gtera.ui.mycars.carcompare.list.CarCompareItemViewModel
import com.gtera.ui.mycars.carcompare.list.CarCompareListViewModel
import com.gtera.ui.mycars.carcompare.result.CarCompareDetailsItemListViewModel
import com.gtera.ui.mycars.carcompare.result.CarCompareDetailsItemViewModel
import com.gtera.ui.mycars.carcompare.result.CarCompareDetailsViewModel
import com.gtera.ui.mycars.insurance.carinsurancerenewal.RequestCarInsuranceViewModel
import com.gtera.ui.mycars.insurance.endorsement.EndorsementViewModel
import com.gtera.ui.mycars.insurance.insurancedetails.InsuranceDetailsViewModel
import com.gtera.ui.mycars.insurance.insurancerequeatslist.CarInsuranceListItemViewModel
import com.gtera.ui.mycars.insurance.insurancerequeatslist.InsuranceListViewModel
import com.gtera.ui.mycars.insurance.online_payment.OnlinePaymentVM
import com.gtera.ui.mycars.list.MyCarsListViewModel
import com.gtera.ui.mycars.maintenance.MaintenanceListVM
import com.gtera.ui.mycars.maintenance.add_maintenance.NewMaintenanceVM
import com.gtera.ui.navigation.BottomNavViewModel
import com.gtera.ui.news.NewsViewModel
import com.gtera.ui.news.newsdetails.NewsDetailsViewModel
import com.gtera.ui.offers.OffersViewModel
import com.gtera.ui.ordernow.OrderNowViewModel
import com.gtera.ui.ordernow.acknowledgment.AcknowledgmentViewModel
import com.gtera.ui.ordernow.details.OrderDetailsViewModel
import com.gtera.ui.ordernow.list.OrderListViewModel
import com.gtera.ui.ordernow.pay.OrderOnlinePaymentViewModel
import com.gtera.ui.product_details.ProductDetailsViewModel
import com.gtera.ui.profile.ProfileViewModel
import com.gtera.ui.profile.branches.BranchesVM
import com.gtera.ui.profile.contactus.ContactUsVM
import com.gtera.ui.profile.edit.EditProfileViewModel
import com.gtera.ui.profile.faq.FAQItemViewModel
import com.gtera.ui.profile.faq.FAQViewModel
import com.gtera.ui.profile.favorites.FavoritesViewModel
import com.gtera.ui.profile.info.ProfileInfoViewModel
import com.gtera.ui.profile.language.LanguageViewModel
import com.gtera.ui.profile.messages.MessagesListItemViewModel
import com.gtera.ui.profile.messages.MessagesListViewModel
import com.gtera.ui.profile.messages.details.MessageDetailsItemViewModel
import com.gtera.ui.profile.messages.details.MessageDetailsViewModel
import com.gtera.ui.profile.notifications.NotificationListItemViewModel
import com.gtera.ui.profile.notifications.NotificationListViewModel
import com.gtera.ui.search.SearchViewModel
import com.gtera.ui.search.filter.SearchFilterViewModel
import com.gtera.ui.search.result.SearchResultViewModel
import com.gtera.ui.slider.SliderViewModel
import com.gtera.ui.splash.SplashViewModel
import com.gtera.ui.topdeals.TopDealsViewModel
import com.gtera.ui.usedcardetails.UsedCarDetailsViewModel
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
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(signUpViewModel: SignUpViewModel?): ViewModel?

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
    @ViewModelKey(MyCarsViewModel::class)
    abstract fun bindMyCarsViewModel(myCarsViewModel: MyCarsViewModel?): ViewModel?


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
    @ViewModelKey(NewsItemViewModel::class)
    abstract fun bindNewsItemViewModel(newsItemViewModel: NewsItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(TotDealsItemViewModel::class)
    abstract fun bindTopDealItemViewModel(hotDealsItemViewModel: TotDealsItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(BrandsViewModel::class)
    abstract fun bindBrandsViewModel(brandsViewModel: BrandsViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(BrandItemViewModel::class)
    abstract fun bindBrandItemViewModel(brandItemViewModel: BrandItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarItemViewModel::class)
    abstract fun bindCarItemViewModel(carItemViewModel: CarItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ModelsViewModel::class)
    abstract fun bindModelsViewModel(modelsViewModel: ModelsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    abstract fun bindForgotPasswordViewModel(resetPasswordViewModel: ResetPasswordViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    abstract fun bindChangePasswordViewModel(changePasswordViewModel: ChangePasswordViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(CarsViewModel::class)
    abstract fun bindCarsViewModel(carsViewModel: CarsViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordConfirmationViewModel::class)
    abstract fun bindResetPasswordConfirmationViewModel(resetPasswordConfirmationViewModel: ResetPasswordConfirmationViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(CarDetailsViewModel::class)
    abstract fun bindCarDetailsViewModel(carDetailsViewModel: CarDetailsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarImageItemViewModel::class)
    abstract fun bindCarImageItemViewModel(carIImagetemViewModel: CarImageItemViewModel?): ViewModel?

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
    @ViewModelKey(OffersViewModel::class)
    abstract fun bindOffersViewModel(offersViewModel: OffersViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(TopDealsViewModel::class)
    abstract fun bindHotDealsViewModel(bottomSheetDialogItemViewModel: TopDealsViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(newsViewModel: NewsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailsViewModel::class)
    abstract fun bindNewsDetailsViewModel(newsDetailsViewModel: NewsDetailsViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(ChooseYourBudgetViewModel::class)
    abstract fun bindChooseYourBudgetViewModel(chooseYourBudgetViewModel: ChooseYourBudgetViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(BudgetBrandItemViewModel::class)
    abstract fun bindBudgetBrandItemViewModel(cudgetBrandItemViewModel: BudgetBrandItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(BudgetCarListViewModel::class)
    abstract fun bindBudgetCarListViewModel(budgetCarListViewModel: BudgetCarListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarCompareListViewModel::class)
    abstract fun bindCarCompareListViewModel(carCompareListViewModel: CarCompareListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarCompareItemViewModel::class)
    abstract fun bindCarCompareItemViewModel(carCompareItemViewModel: CarCompareItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(MyCarItemViewModel::class)
    abstract fun bindMyCarItemViewModel(myCarItemViewModel: MyCarItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarCompareDetailsViewModel::class)
    abstract fun bindCarCompareDetailsViewModel(carCompareDetailsViewModel: CarCompareDetailsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarCompareDetailsItemListViewModel::class)
    abstract fun bindCarCompareDetailsItemListViewModel(carCompareDetailsItemListViewModel: CarCompareDetailsItemListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarCompareDetailsItemViewModel::class)
    abstract fun bindCarCompareDetailsItemViewModel(carCompareDetailsItemViewModel: CarCompareDetailsItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProfileInfoViewModel::class)
    abstract fun bindProfileInfoViewModel(profileInfoViewModel: ProfileInfoViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindEditProfileViewModel(editProfileViewModel: EditProfileViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(SearchResultViewModel::class)
    abstract fun bindSearchResultViewModel(searchResultViewModel: SearchResultViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(SearchFilterViewModel::class)
    abstract fun bindSearchFilterViewModel(searchFilterViewModel: SearchFilterViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarsFilterViewModel::class)
    abstract fun bindCarsFilterViewModel(carsFilterViewModel: CarsFilterViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(FilterItemViewModel::class)
    abstract fun bindFilterItemViewModel(filterItemViewModel: FilterItemViewModel): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(LanguageViewModel::class)
    abstract fun bindLanguageViewModel(languageViewModel: LanguageViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(UsedCarDetailsViewModel::class)
    abstract fun bindUsedCarDetailsViewModel(usedCarDetailsViewModel: UsedCarDetailsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(RequestCarInsuranceViewModel::class)
    abstract fun bindRequestCarInsuranceViewModel(requestCarInsuranceViewModel: RequestCarInsuranceViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun bindFavoritesViewModel(favoritesViewModel: FavoritesViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(InsuranceListViewModel::class)
    abstract fun bindInsuranceListViewModel(insuranceListViewModel: InsuranceListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(InsuranceDetailsViewModel::class)
    abstract fun bindInsuranceDetailsViewModel(insuranceDetailsViewModel: InsuranceDetailsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CarInsuranceListItemViewModel::class)
    abstract fun bindCarInsuranceListItemViewModel(carInsuranceListItemViewModel: CarInsuranceListItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(EndorsementViewModel::class)
    abstract fun bindEndorsementViewModel(endorsementViewModel: EndorsementViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(SelectorViewModel::class)
    abstract fun bindSelectorViewModel(selectorViewModel: SelectorViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(SelectorItemViewModel::class)
    abstract fun bindSelectoritemViewModel(selectorItemViewModel: SelectorItemViewModel?): ViewModel?
    @Binds
    @IntoMap
    @ViewModelKey(CalculateItVM::class)
    abstract fun bindCalculateItViewModel(calculateItVM: CalculateItVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(CalculateItResultsVM::class)
    abstract fun bindCCalculateItResultsViewModel(calculateItResultsVM: CalculateItResultsVM?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(CalculateItCarItemVM::class)
    abstract fun bindCalculateItCarItemVMViewModel(calculateItCarItemVM: CalculateItCarItemVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(MyCarsListViewModel::class)
    abstract fun bindMyCarsListViewModel(calculateItCarItemVM: MyCarsListViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(AddMyCarViewModel::class)
    abstract fun bindAddMyCarViewModel(addMyCarViewModel: AddMyCarViewModel?): ViewModel?


    @Binds
    @IntoMap
    @ViewModelKey(BranchesVM::class)
    abstract fun bindBranchesViewModel(branchesVM: BranchesVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(MaintenanceListVM::class)
    abstract fun bindMaintenanceViewModel(maintenanceListVM: MaintenanceListVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(NewMaintenanceVM::class)
    abstract fun bindNewMaintenanceViewModel(newMaintenanceVM: NewMaintenanceVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsVM::class)
    abstract fun bindContactUsViewModel(contactUsVM: ContactUsVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(WebViewVM::class)
    abstract fun bindWebViewViewModel(webViewVM: WebViewVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(OnlinePaymentVM::class)
    abstract fun bindOnlinePaymentViewModel(webViewVM: OnlinePaymentVM?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(MessagesListViewModel::class)
    abstract fun bindMessagesListViewModel(webViewVM: MessagesListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(MessagesListItemViewModel::class)
    abstract fun bindMessagesListItemViewModel(webViewVM: MessagesListItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(NotificationListViewModel::class)
    abstract fun bindNotificationListViewModel(webViewVM: NotificationListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(NotificationListItemViewModel::class)
    abstract fun bindNotificationListItemViewModel(notificationListItemViewModel: NotificationListItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(MessageDetailsViewModel::class)
    abstract fun bindMessageDetailsViewModel(messageDetailsViewModel: MessageDetailsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(MessageDetailsItemViewModel::class)
    abstract fun bindMessageDetailsItemViewModel(messageDetailsItemViewModel: MessageDetailsItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(FAQViewModel::class)
    abstract fun bindFAQViewModel(faqViewModel: FAQViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(FAQItemViewModel::class)
    abstract fun bindFAQItemViewModel(webViewVM: FAQItemViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(InputDialogViewModel::class)
    abstract fun bindInputDialogViewModel(inputDialogViewModel: InputDialogViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(OrderNowViewModel::class)
    abstract fun bindOrderNowViewModel(orderNowViewModel: OrderNowViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(OrderListViewModel::class)
    abstract fun bindOrderListViewModel(orderListViewModel: OrderListViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailsViewModel::class)
    abstract fun bindOrderDetailsViewModel(orderDetailsViewModel: OrderDetailsViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(AcknowledgmentViewModel::class)
    abstract fun bindAcknowledgmentViewModel(acknowledgmentViewModel: AcknowledgmentViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(OrderOnlinePaymentViewModel::class)
    abstract fun bindOrderOnlinePaymentViewModel(orderOnlinePaymentViewModel: OrderOnlinePaymentViewModel?): ViewModel?

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailsViewModel::class)
    abstract fun bindProductDetailsViewModel(productDetailsViewModel: ProductDetailsViewModel?): ViewModel?

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory?): ViewModelProvider.Factory?
}