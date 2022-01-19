package com.gtera.ui.splash

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.SplashLayoutBinding
import javax.inject.Inject

class SplashActivity : BaseActivity<SplashLayoutBinding, SplashViewModel>(), SplashNavigator {

    @Inject
     lateinit var splashViewModel: SplashViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        super.onCreate(savedInstanceState)

    }
    override fun getLayoutRes(): Int {
        return R.layout.splash_layout
    }

    override val viewModelClass: Class<SplashViewModel>
        get() = SplashViewModel::class.java

     override fun setNavigator(viewModel: SplashViewModel?) {
        viewModel?.setNavigator(this)
    }


    override fun hasToolbar(): Boolean {
        return false
    }


}
