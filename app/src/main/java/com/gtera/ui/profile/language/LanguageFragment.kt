package com.gtera.ui.profile.language

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.LanguageLayoutBinding

class LanguageFragment :
    BaseFragment<LanguageLayoutBinding, LanguageViewModel>(),
    LanguageNavigator {
    override val layoutId: Int
        get() = R.layout.language_layout

    override val viewModelClass: Class<LanguageViewModel>
        get() = LanguageViewModel::class.java

    override fun setNavigator(viewModel: LanguageViewModel?) {
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
        get() = getString(R.string.str_language_title)
}