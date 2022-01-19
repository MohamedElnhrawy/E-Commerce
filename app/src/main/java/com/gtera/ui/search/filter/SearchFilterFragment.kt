package com.gtera.ui.search.filter

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.SearchFilterLayoutBinding
import com.gtera.utils.Utilities

class SearchFilterFragment :
    BaseFragment<SearchFilterLayoutBinding, SearchFilterViewModel>(),
    SearchFilterNavigator {
    override val layoutId: Int
        get() = R.layout.search_filter_layout

    override val viewModelClass: Class<SearchFilterViewModel>
        get() = SearchFilterViewModel::class.java

    override fun setNavigator(viewModel: SearchFilterViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hideKeyboard() {
        Utilities.hideSoftKeyboard(baseActivity!!)
    }

    override fun hasBack(): Boolean {
        return true
    }



    override fun shimmerLoadingCount(): Int {
        return 6
    }

    override val toolbarElevation: Boolean
        get() = false


    override val toolbarTitle: String?
        get() = getString(R.string.str_brands_title)

    override val isListingView: Boolean
        get() = true
}