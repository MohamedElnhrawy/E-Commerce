package com.gtera.ui.ordernow.pay

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.ui.webview.WebViewVM
import com.gtera.utils.APPConstants
import javax.inject.Inject

class OrderOnlinePaymentViewModel @Inject constructor() : WebViewVM() {

     override fun loadUrl(webView: WebView, url: String) {
        if (url.contains("payment/callback?")) {
            val success =
                url.contains("payment/callback?") && url.contains("data.message=Approved") &&
                        url.contains("success=true")
            val uri = Uri.parse(url)
            if (success) {

                val orderId = uri.getQueryParameter("order_id")
                val extras = Bundle()
//                extras.putString(APPConstants.EXTRAS_KEY_PAYMENT_ORDER_ID, orderId)
                setViewResult(AppScreenRoute.PAYMENT_ONLINE_SUCCESS, Activity.RESULT_OK, extras)
            } else {

                val errorMsg = uri.getQueryParameter("data.message")
                val extras = Bundle()
                extras.putString(APPConstants.EXTRAS_KEY_PAYMENT_MESSAGE, errorMsg)
                setViewResult(
                    AppScreenRoute.PAYMENT_ONLINE_FAIL,
                    APPConstants.RESULT_CODE_ERROR,
                    extras)
            }
        } else
            super.loadUrl(webView, url)
    }

    protected override fun pageFinished(url: String) {
        super.pageFinished(url)
    }
}