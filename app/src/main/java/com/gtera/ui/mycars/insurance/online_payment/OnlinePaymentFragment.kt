package com.gtera.ui.mycars.insurance.online_payment

import android.os.Bundle
import android.view.View
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.OnlinePaymentLayoutBinding
import com.gtera.ui.webview.WebViewNavigator

class OnlinePaymentFragment : BaseFragment<OnlinePaymentLayoutBinding, OnlinePaymentVM>(),
    OnlinePaymentNavigator,
    WebViewNavigator {
    override val layoutId: Int
        get() = R.layout.online_payment_layout
    override val viewModelClass: Class<OnlinePaymentVM>
        get() = OnlinePaymentVM::class.java

    override fun setNavigator(viewModel: OnlinePaymentVM?) {
        viewModel?.setNavigator(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.initWebView(viewDataBinding.webView)
    }
}