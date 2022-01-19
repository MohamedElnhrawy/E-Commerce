package com.gtera.ui.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.authorization.changepassword.ChangePasswordActivity
import com.gtera.ui.authorization.login.SignInActivity
import com.gtera.ui.authorization.register.SignUpActivity
import com.gtera.ui.authorization.resetpassword.ResetPasswordActivity
import com.gtera.ui.authorization.resetpasswordconfirmation.ResetPasswordConfirmationActivity
import com.gtera.ui.navigation.BottomNavigationActivity
import com.gtera.ui.product_details.ProductDetailsActivity
import com.gtera.ui.splash.SplashActivity
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.ui.utils.AppScreenRoute.*
import javax.inject.Inject

class BaseAppRoute @Inject constructor(val resourceProvider: ResourceProvider) {
    protected var navigator: BaseNavigator? = null
    protected var currentActivityClass: Class<*>? = null
    protected var IS_APP_BOTTOM_NAV: Boolean

//    protected fun bottomNavClass(): Class<*>? { //        return BottomNavActivity.class;
//        return null
//    }
//
//    protected val isBottomNavigation: Boolean
//        protected get() = IS_APP_BOTTOM_NAV &&
//                currentActivityClass == bottomNavClass()

    @CallSuper
    fun appRoute(
        navigator: BaseNavigator?,
        currentActivityClass: Class<*>?,
        route: AppScreenRoute?,
        extras: Bundle?
    ) {
        appRoute(navigator, currentActivityClass, route, null, extras)
    }

    @CallSuper
    fun appRoute(
        navigator: BaseNavigator?,
        currentActivityClass: Class<*>?,
        route: AppScreenRoute?,
        requestCode: Int?,
        extras: Bundle?
    ) {
        this.navigator = navigator
        this.currentActivityClass = currentActivityClass
        this.navigator = navigator
        this.currentActivityClass = currentActivityClass
        when (route) {

            MAIN_SCREEN -> openMainScreen(extras)
            SIGN_IN_SCREEN -> openSignInScreen(extras)
            PRODUCT_DETAILS_SCREEN -> openProductDetailScreen(extras)
            SIGN_UP_SCREEN -> openSignUpScreen(extras)
            CAR_BRANDS -> openBrandsScreen(extras)
            BRAND_MODELS -> openBrandModelsScreen(extras)
            MODEL_CARS -> openModelCarsScreen(extras)
            CHANGE_PASSWORD -> openChangePasswordScreen(extras)
            RESET_PASSWORD -> openPasswordResetScreen(extras)
            RESET_PASSWORD_CONFIRMATION -> openPasswordResetConfirmationScreen(extras)
            CAR_DETAILS -> openCarDetailsScreen(extras)
            TOP_DEALS_LISTING -> openTopDealsScreen(extras)
            OFFERS_LISTING -> openOffersScreen(extras)
            NEWS_LISTING -> openNewsScreen(extras)
            NEWS_DETAILS -> openNewsDetailsScreen(extras)
            HOME_NEWS_DETAILS -> openHomeNewsDetailsScreen(extras)
            CHOOSE_YOUR_BUDGET -> openChooseYourBudgetScreen(extras)
            CHOOSE_YOUR_BUDGET_CAR_LIST -> openChooseYourBudgetCarListScreen(extras)
            CAR_COMPARE_SCREEN -> openCarCompareScreen(extras)
            CAR_COMPARE_DETAILS_SCREEN -> openCarCompareDetailsScreen(extras)
            SEARCH_RESULT -> openSearchDetailsScreen(extras)
            SEARCH_VIEW -> openSearchViewScreen(extras)
            SEARCH_RESULT_FILTER -> openSearchFilterScreen(extras)
            PROFILE_INFO -> openProfileInfoScreen(extras)
            EDIT_PROFILE -> openEditProfileScreen(extras)
            CHANGE_LANGUAGE -> openChangeLanguageScreen(extras)
            FAVORITES -> openFavoritesScreen(extras)
            CAR_RENEWAL_INSURANCE -> openCarRenewalInsuranceScreen(extras)
            CAR_RENEWAL_INSURANCE_LIST -> openInsuranceListScreen(extras)
            CAR_RENEWAL_INSURANCE_DETAILS -> openInsuranceDetailsScreen(extras)
            CAR_RENEWAL_INSURANCE_ENDORSEMENT -> openInsuranceEndorsementScreen(extras)
            USED_CAR_DETAILS -> openUsedCarDetailsScreen(extras)
            CALCULATE_IT_YOURSELF -> openCalculateItYourselfScreen(extras)
            CALCULATE_IT_RESULTS -> openCalculateItResultdScreen(extras)
            CAR_MAINTENANCE_LIST -> openCarMaintenanceListScreen(extras)
            ADD_MAINTENANCE -> openAddCarMaintenanceScreen(extras)
            CONTACT_US -> openContactUsScreen(extras)
            RENEWAL_INSURANCE_PAYMENT -> requestCode?.let { openOnlinePaymentScreen(it, extras) }
            MY_CARS_LIST -> openMyCarsListScreen(extras)
            NOTIFICATIONS -> openNotificationsListScreen(extras)
            MESSAGES -> openMessagesListScreen(extras)
            MESSAGE_DETAILS -> openMessageDetailsScreen(extras)
            FAQ -> openFAQScreen(extras)
            ORDER_NOW -> openOrderNowScreen(extras)
            ORDER_LIST -> openOrderListScreen(extras)
            ORDER_DETAILS -> openOrderDetailsScreen(extras)
            ORDER_ACKNOWLEDGMENT -> openOrderAcknowlegmentScreen(extras)
            ORDER_PAYMENT -> openOrderPaymentScreen(extras)
            ADVERTISE_LIST -> openAdvertiseListScreen(extras)
            CAR_ADVERTISE -> openCarAdvertiseScreen(extras)
        }
    }


    @CallSuper
    fun appResultRoute(
        navigator: BaseNavigator?,
        route: AppScreenRoute?,
        currentActivityClass: Class<*>?,
        isFragmentView: Boolean,
        requestCode: Int,
        resultCode: Int,
        extras: Bundle?
    ) {
        this.navigator = navigator
        this.navigator = navigator;
        when (route) {
            SIGN_IN_SUCCESS -> {
                setAuthorizationsResult(isFragmentView, requestCode, resultCode, extras);
            }

            PAYMENT_ONLINE_FAIL -> {
                setPaymentViewResult(isFragmentView, requestCode, resultCode, extras)
            }
            PAYMENT_ONLINE_SUCCESS -> {
                setPaymentViewResult(isFragmentView, requestCode, resultCode, extras)
            }

        }


    }

    protected fun goBack() {
        navigator!!.goBack()
    }

    protected fun launchMapIntent(lat: String?, lng: String?) {
        navigator!!.launchMapIntent(lat, lng)
    }

    protected fun openView(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        navigator!!.openActivity(activity, extras)
    }

    protected fun openViewAndFinish(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        navigator!!.openActivityAndFinish(activity, extras)
    }

    protected fun openView(@IdRes actionId: Int, extras: Bundle?) {
        navigator!!.openFragment(actionId, extras)
    }

    protected fun openNewView(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        navigator!!.openNewActivity(activity, extras)
    }

    protected fun openNewView(
        activity: Class<out BaseActivity<*, *>?>?, @IdRes actionId: Int,
        bundle: Bundle?
    ) {
        val extras = Bundle()
        //        extras.putBoolean(AppConstants.OPEN_ACTIVITY_FRAGMENT, true);
//        extras.putInt(AppConstants.SELECTED_FRAGMENT_ACTION_ID, actionId);
//        if (bundle != extras)
//            extras.putBundle(AppConstants.FRAGMENT_DATA, bundle);
        navigator!!.openNewActivity(activity, extras)
    }


    protected fun openViewForResult(
        activity: Class<out BaseActivity<*, *>?>?,
        requestCode: Int,
        extras: Bundle?
    ) {
        navigator!!.openActivityForResult(activity, requestCode, extras)
    }

    protected fun openViewForResult(
        @IdRes actionId: Int, requestCode: Int,
        extras: Bundle?
    ) {
        navigator!!.openFragmentForResult(actionId, requestCode, extras)
    }

    private fun openMainScreen(extras: Bundle?) {

        openNewView(BottomNavigationActivity::class.java, extras);
    }

    private fun openSignInScreen(extras: Bundle?) {

        openNewView(SignInActivity::class.java, extras);
    }

    private fun openProductDetailScreen(extras: Bundle?) {

        openView(ProductDetailsActivity::class.java, extras);
    }

    protected fun openSignUpScreen(extras: Bundle?) {

        openView(SignUpActivity::class.java, extras);
    }

    protected fun restartApp() {
        openNewView(SplashActivity::class.java, null)
    }


    protected fun openPasswordResetScreen(extras: Bundle?) {
        openView(ResetPasswordActivity::class.java, extras)
    }


    protected fun openPasswordResetConfirmationScreen(extras: Bundle?) {
        openView(ResetPasswordConfirmationActivity::class.java, extras)
    }


    protected fun openChangePasswordScreen(extras: Bundle?) {
        openView(ChangePasswordActivity::class.java, extras)
    }


    protected fun openBrandsScreen(extras: Bundle?) {
        openView(R.id.action_brands_list, extras)
    }

    protected fun openBrandModelsScreen(extras: Bundle?) {
        openView(R.id.action_models_list, extras)
    }

    protected fun openModelCarsScreen(extras: Bundle?) {
        openView(R.id.action_cars_list, extras)
    }

    protected fun openCarDetailsScreen(extras: Bundle?) {
        openView(R.id.action_car_details, extras)
    }

    protected fun openUsedCarDetailsScreen(extras: Bundle?) {
        openView(R.id.action_used_car_details, extras)
    }


    protected fun openTopDealsScreen(extras: Bundle?) {
        openView(R.id.action_topdeals_list, extras)
    }

    protected fun openOffersScreen(extras: Bundle?) {
        openView(R.id.action_offers_list, extras)
    }

    protected fun openNewsScreen(extras: Bundle?) {
        openView(R.id.action_news_list, extras)
    }

    protected fun openNewsDetailsScreen(extras: Bundle?) {
        openView(R.id.action_news_details, extras)
    }

    protected fun openHomeNewsDetailsScreen(extras: Bundle?) {
        openView(R.id.action_home_newsDetails, extras)
    }

    protected fun openChooseYourBudgetScreen(extras: Bundle?) {
        openView(R.id.action_choose_your_budget, extras)
    }

    protected fun openChooseYourBudgetCarListScreen(extras: Bundle?) {
        openView(R.id.action_choose_your_budget_car_list, extras)
    }

    protected fun openCarCompareScreen(extras: Bundle?) {
        openView(R.id.action_car_compare, extras)
    }

    protected fun openCarRenewalInsuranceScreen(extras: Bundle?) {
        openView(R.id.action_car_renewal_insurance, extras)
    }

    protected fun openCarCompareDetailsScreen(extras: Bundle?) {
        openView(R.id.action_car_compare_details, extras)
    }

    protected fun openSearchDetailsScreen(extras: Bundle?) {
        openView(R.id.action_search_result, extras)
    }
    private fun openSearchViewScreen(extras: Bundle?) {
        openView(R.id.action_search, extras)
    }

    protected fun openSearchFilterScreen(extras: Bundle?) {
        openView(R.id.action_search_filter, extras)
    }

    protected fun openProfileInfoScreen(extras: Bundle?) {
        openView(R.id.action_profile_info, extras)
    }

    protected fun openEditProfileScreen(extras: Bundle?) {
        openView(R.id.action_edit_profile, extras)
    }


    protected fun openChangeLanguageScreen(extras: Bundle?) {
        openView(R.id.action_change_language, extras)
    }

    protected fun openFavoritesScreen(extras: Bundle?) {
        openView(R.id.action_favorites, extras)
    }


    protected fun openInsuranceListScreen(extras: Bundle?) {
        openView(R.id.action_insurance_list, extras)
    }

    protected fun openInsuranceDetailsScreen(extras: Bundle?) {
        openView(R.id.action_insurance_details, extras)
    }

    protected fun openInsuranceEndorsementScreen(extras: Bundle?) {
        openView(R.id.action_insurance_endorsement, extras)
    }

    protected fun openCalculateItYourselfScreen(extras: Bundle?) {
        openView(R.id.action_calculateIt_yourself, extras)
    }

    protected fun openCalculateItResultdScreen(extras: Bundle?) {
        openView(R.id.action_calculateIt_Results, extras)
    }

    protected fun openCarMaintenanceListScreen(extras: Bundle?) {
        openView(R.id.action_maintenance_list, extras)
    }

    protected fun openAddCarMaintenanceScreen(extras: Bundle?) {
        openView(R.id.action_new_Maintenance, extras)
    }

    protected fun openMyCarsListScreen(extras: Bundle?) {
        openView(R.id.action_my_cars_list, extras)
    }

    protected fun openNotificationsListScreen(extras: Bundle?) {
        openView(R.id.action_show_notifications, extras)
    }

    protected fun openMessagesListScreen(extras: Bundle?) {
        openView(R.id.action_show_messages, extras)
    }

    protected fun openMessageDetailsScreen(extras: Bundle?) {
        openView(R.id.action_show_message_details, extras)
    }

    protected fun openFAQScreen(extras: Bundle?) {
        openView(R.id.action_show_faq, extras)
    }

    protected fun openOrderNowScreen(extras: Bundle?) {
        openView(R.id.action_order_now, extras)
    }


    protected fun openOrderListScreen(extras: Bundle?) {
        openView(R.id.action_order_list, extras)
    }

    protected fun openOrderDetailsScreen(extras: Bundle?) {
        openView(R.id.action_order_orderDetails, extras)
    }

    protected fun openOrderAcknowlegmentScreen(extras: Bundle?) {
        openView(R.id.action_order_acknowledgment, extras)
    }

    protected fun openOrderPaymentScreen(extras: Bundle?) {
        openView(R.id.action_order_orderOnlinePayment, extras)
    }

    protected fun openContactUsScreen(extras: Bundle?) {
        openView(R.id.action_contactUs, extras)
    }

    protected fun openAdvertiseListScreen(extras: Bundle?) {
        openView(R.id.action_show_advertiseList, extras)
    }

    protected fun openCarAdvertiseScreen(extras: Bundle?) {
        openView(R.id.action_contactUs, extras)
    }

    protected fun openOnlinePaymentScreen(requestCode: Int, extras: Bundle?) {
        openViewForResult(R.id.action_endorsement_payment, requestCode, extras)
    }

    protected fun setPaymentViewResult(
        isFragmentView: Boolean,
        requestCode: Int,
        resultCode: Int,
        extras: Bundle?
    ) {
        if (isFragmentView) navigator!!.setFragmentResult(
            requestCode,
            resultCode,
            extras
        ) else navigator!!.setActivityResult(resultCode, extras)
    }


    protected fun setAuthorizationsResult(
        isFragment: Boolean,
        requestCode: Int,
        resultCode: Int,
        extras: Bundle?
    ) {
        navigator!!.setActivityResult(resultCode, extras)
    }

    init {
        IS_APP_BOTTOM_NAV = resourceProvider.getBoolean(R.bool.is_bottom_nav)
    }
}