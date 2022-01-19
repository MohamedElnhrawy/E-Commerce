package com.gtera.ui.profile.faq

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.FaqLayoutBinding

class FAQFragment :
    BaseFragment<FaqLayoutBinding, FAQViewModel>(),
    FAQNavigator {
    override val layoutId: Int
        get() = R.layout.faq_layout

    override val viewModelClass: Class<FAQViewModel>
        get() = FAQViewModel::class.java

    override fun setNavigator(viewModel: FAQViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_faq_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 15
    }

    override fun hasBack(): Boolean {
        return true
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true

    override val toolbarTitle: String?
        get() = getString(R.string.str_faq)
}