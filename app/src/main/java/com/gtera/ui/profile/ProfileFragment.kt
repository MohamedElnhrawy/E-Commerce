package com.gtera.ui.profile

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.ProfileLayoutBinding

class ProfileFragment :
    BaseFragment<ProfileLayoutBinding, ProfileViewModel>(),
    ProfileNavigator {
    override val layoutId: Int
        get() = R.layout.profile_layout

    override val viewModelClass: Class<ProfileViewModel>
        get() = ProfileViewModel::class.java

    override fun setNavigator(viewModel: ProfileViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return false
    }

    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true

    override val toolbarTitle: String?
        get() = getString(R.string.str_profile_title)
}