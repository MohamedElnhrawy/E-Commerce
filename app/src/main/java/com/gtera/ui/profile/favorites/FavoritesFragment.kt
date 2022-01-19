package com.gtera.ui.profile.favorites

import android.view.View
import androidx.databinding.ViewStubProxy
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.FavoritesLayoutBinding

class FavoritesFragment :
    BaseFragment<FavoritesLayoutBinding, FavoritesViewModel>(),
    FavoritesNavigator {
    override val layoutId: Int
        get() = R.layout.favorites_layout

    override val viewModelClass: Class<FavoritesViewModel>
        get() = FavoritesViewModel::class.java

    override fun setNavigator(viewModel: FavoritesViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true

    override val toolbarTitle: String?
        get() = getString(R.string.str_profile_favorite)

    override fun shimmerLoadingCount(): Int {
        return    10
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_offers_list_layout
    }

    override val emptyView: ViewStubProxy
        get() = viewDataBinding.emptyView

    override val loadingView: ViewStubProxy
        get() = viewDataBinding.loadingView

    override val dataView: View
        get() = viewDataBinding.swipeRef
}