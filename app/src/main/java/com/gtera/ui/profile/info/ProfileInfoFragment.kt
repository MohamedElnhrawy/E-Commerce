package com.gtera.ui.profile.info

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.ProfileInfoLayoutBinding

class ProfileInfoFragment :
    BaseFragment<ProfileInfoLayoutBinding, ProfileInfoViewModel>(),
    ProfileInfoNavigator {
    override val layoutId: Int
        get() = R.layout.profile_info_layout

    override val viewModelClass: Class<ProfileInfoViewModel>
        get() = ProfileInfoViewModel::class.java

    override fun setNavigator(viewModel: ProfileInfoViewModel?) {
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
        get() = getString(R.string.str_account_details_header)
}