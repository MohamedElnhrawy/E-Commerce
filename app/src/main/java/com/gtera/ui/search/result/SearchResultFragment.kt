package com.gtera.ui.search.result

import android.view.View
import androidx.databinding.ViewStubProxy
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.SearchResultLayoutBinding

class SearchResultFragment :
    BaseFragment<SearchResultLayoutBinding, SearchResultViewModel>(),
    SearchResultNavigator {

    override val layoutId: Int
        get() = R.layout.search_result_layout

    override val loadingView: ViewStubProxy
        get() = viewDataBinding.loadingView

    override val emptyView: ViewStubProxy
        get() = viewDataBinding.emptyView
    override val dataView: View
        get() = viewDataBinding.swrefcarlist


    override val viewModelClass: Class<SearchResultViewModel>
        get() = SearchResultViewModel::class.java

    override fun setNavigator(viewModel: SearchResultViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override fun hasSearch(): Boolean {
        return true
    }


    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true





}