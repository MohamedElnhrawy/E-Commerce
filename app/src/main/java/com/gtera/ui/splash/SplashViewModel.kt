package com.gtera.ui.splash

import com.gtera.base.BaseViewModel
import com.gtera.ui.utils.AppScreenRoute
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashViewModel : BaseViewModel<SplashNavigator> {


    private val SPLASH_DURATION = 2

    constructor(currentActivityClass: Class<*>?) : super(currentActivityClass) {

    }

    @Inject
    constructor() : this(SplashViewModel::class.java)

    override fun onViewCreated() {
        super.onViewCreated()
        showSplashTimer()
    }

    private fun showSplashTimer() {
        val scheduler =
            Executors.newScheduledThreadPool(1)
        scheduler.schedule(
            { this.openMainScreen() },
            SPLASH_DURATION.toLong(),
            TimeUnit.SECONDS
        )
    }


    private fun openMainScreen() {
        if (appRepository.isUserLoggedIn())
            openView(AppScreenRoute.MAIN_SCREEN, null)
        else
            openView(AppScreenRoute.SIGN_IN_SCREEN, null)

    }


}