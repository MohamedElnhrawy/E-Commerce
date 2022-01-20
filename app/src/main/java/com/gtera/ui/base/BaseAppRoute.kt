package com.gtera.ui.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.authorization.login.SignInActivity
import com.gtera.ui.authorization.otp.OtpActivity
import com.gtera.ui.cart.CartActivity
import com.gtera.ui.navigation.BottomNavigationActivity
import com.gtera.ui.product_details.ProductDetailsActivity
import com.gtera.ui.splash.SplashActivity
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.ui.utils.AppScreenRoute.*
import javax.inject.Inject

class BaseAppRoute @Inject constructor(val resourceProvider: ResourceProvider) {
     var navigator: BaseNavigator? = null
     var currentActivityClass: Class<*>? = null

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
            OTP_SCREEN -> openOtpScreen(extras)
            CART_SCREEN -> openCartScreen(extras)
            PRODUCT_DETAILS_SCREEN -> openProductDetailScreen(extras)
            SEARCH_VIEW -> openSearchViewScreen(extras)
            CHANGE_LANGUAGE -> openChangeLanguageScreen(extras)


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

    private fun goBack() {
        navigator!!.goBack()
    }

    private fun launchMapIntent(lat: String?, lng: String?) {
        navigator!!.launchMapIntent(lat, lng)
    }

    private fun openView(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        navigator!!.openActivity(activity, extras)
    }

    private fun openViewAndFinish(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        navigator!!.openActivityAndFinish(activity, extras)
    }

    private fun openView(@IdRes actionId: Int, extras: Bundle?) {
        navigator!!.openFragment(actionId, extras)
    }

    private fun openNewView(
        activity: Class<out BaseActivity<*, *>?>?,
        extras: Bundle?
    ) {
        navigator!!.openNewActivity(activity, extras)
    }

    private fun openNewView(
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


    private fun openViewForResult(
        activity: Class<out BaseActivity<*, *>?>?,
        requestCode: Int,
        extras: Bundle?
    ) {
        navigator!!.openActivityForResult(activity, requestCode, extras)
    }

    private fun openViewForResult(
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

    private fun openOtpScreen(extras: Bundle?) {

        openNewView(OtpActivity::class.java, extras);
    }

    private fun openProductDetailScreen(extras: Bundle?) {

        openView(ProductDetailsActivity::class.java, extras);
    }

    private fun openCartScreen(extras: Bundle?) {

        openView(CartActivity::class.java, extras);
    }

    private fun restartApp() {
        openNewView(SplashActivity::class.java, null)
    }

    private fun openSearchViewScreen(extras: Bundle?) {
        openView(R.id.action_search, extras)
    }


    protected fun openChangeLanguageScreen(extras: Bundle?) {
        openView(R.id.action_change_language, extras)
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


    private fun setAuthorizationsResult(
        isFragment: Boolean,
        requestCode: Int,
        resultCode: Int,
        extras: Bundle?
    ) {
        navigator!!.setActivityResult(resultCode, extras)
    }

}