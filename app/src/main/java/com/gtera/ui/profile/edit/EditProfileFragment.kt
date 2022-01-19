package com.gtera.ui.profile.edit

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.EditProfileLayoutBinding

class EditProfileFragment :
    BaseFragment<EditProfileLayoutBinding, EditProfileViewModel>(),
    EditProfileNavigator {
    override val layoutId: Int
        get() = R.layout.edit_profile_layout

    override val viewModelClass: Class<EditProfileViewModel>
        get() = EditProfileViewModel::class.java

    override fun setNavigator(viewModel: EditProfileViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }



    override val toolbarElevation: Boolean
        get() = true

    override val isListingView: Boolean
        get() = true

    override val toolbarTitle: String?
        get() = getString(R.string.str_account_details_header)
}