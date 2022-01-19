package com.gtera.ui.ordernow.pay

import android.os.Bundle
import android.view.View
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.OnlinePaymentLayoutBinding
import com.gtera.ui.webview.WebViewNavigator

class OrderOnlinePaymentFragment : BaseFragment<OnlinePaymentLayoutBinding, OrderOnlinePaymentViewModel>(),
    OrderOnlinePaymentNavigator,
    WebViewNavigator {
    override val layoutId: Int
        get() = R.layout.online_payment_layout
    override val viewModelClass: Class<OrderOnlinePaymentViewModel>
        get() = OrderOnlinePaymentViewModel::class.java

    override fun setNavigator(viewModel: OrderOnlinePaymentViewModel?) {
        viewModel?.setNavigator(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.initWebView(viewDataBinding.webView)
    }
}