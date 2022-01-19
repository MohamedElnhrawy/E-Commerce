package com.gtera.ui.webview

import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.CallSuper
import androidx.databinding.ObservableField
import com.gtera.base.BaseViewModel
import com.gtera.utils.APPConstants
import javax.inject.Inject

open class WebViewVM  @Inject constructor() : BaseViewModel<WebViewNavigator>(){



    var url = ObservableField("")

    override fun onViewCreated() {
        super.onViewCreated()
        getWebUrl()
    }

    private fun getWebUrl() {
        if (dataExtras?.containsKey(APPConstants.EXTRAS_KEY_WEB_URL)!!) {
            url.set(dataExtras?.getString(APPConstants.EXTRAS_KEY_WEB_URL))
        }
    }

    fun initWebView(webView: WebView) {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                loadUrl(view, url)
                return true
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                loadUrl(view, request.url.toString())
                return true
            }


            override fun onPageFinished(view: WebView, url: String) {
                pageFinished(url)
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                if (view.settings.cacheMode == WebSettings.LOAD_NO_CACHE && url.get() == failingUrl) {
                    view.settings.cacheMode = WebSettings.LOAD_DEFAULT
                    view.loadUrl(url.get())
                    return
                } else if (url.get() == failingUrl) {
                    hasData(0)
                } else
                    super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.allowContentAccess = true
        settings.setSupportZoom(true)
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
    }

    @CallSuper
    protected open fun loadUrl(webView: WebView, url: String) {
        isLoading.set(true)
        webView.loadUrl(url)
    }

    @CallSuper
    protected open fun pageFinished(url: String) {
        isLoading.set(false)
    }


    fun reload(webView: WebView) {
        webView.loadUrl(url.get())
        hasData(2)
    }
}