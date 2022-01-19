package com.gtera.ui.search

import android.view.View
import androidx.databinding.ViewStubProxy
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.SearchLayoutBinding
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities


class SearchFragment :
    BaseFragment<SearchLayoutBinding, SearchViewModel>(),
    SearchNavigator {
    override val layoutId: Int
        get() = R.layout.search_layout

    override val loadingView: ViewStubProxy
        get() = viewDataBinding.loadingView

    override val emptyView: ViewStubProxy
        get() = viewDataBinding.emptyView
    override val dataView: View
        get() = viewDataBinding.swrefcarlist


    override val viewModelClass: Class<SearchViewModel>
         get() = SearchViewModel::class.java

    override fun setNavigator(viewModel: SearchViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return intentExtras?.getSerializable(APPConstants.EXTRAS_KEY_CATEGORY) != null
    }

    override fun hasSearch(): Boolean {
        return true
    }

    override fun hideKeyboard() {
        Utilities.hideSoftKeyboard(baseActivity!!)
    }
    override val isListingView: Boolean
        get() = true

}