package com.gtera.ui.webview

import android.os.Bundle
import android.view.View
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.WebViewLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView

class WebViewFragment : BaseFragment<WebViewLayoutBinding, WebViewVM>(), WebViewNavigator {
    override val layoutId: Int
        get() = R.layout.web_view_layout
    override val viewModelClass: Class<WebViewVM>
        get() = WebViewVM::class.java

    override fun setNavigator(viewModel: WebViewVM?) {
        viewModel?.setNavigator(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.initWebView(viewDataBinding.webView)
    }

    override val isListingView: Boolean
        get() = true

    override fun screenEmptyView(): EmptyView {

        return EmptyView(R.drawable.ic_empty_list,
            getString(R.string.oops),
            R.string.str_reload,
            object : ClickListener {
                override fun onViewClicked() {
                    viewModel?.reload(viewDataBinding.webView)
                }
            },
            viewModel?.resourceProvider!!
        )
    }
}