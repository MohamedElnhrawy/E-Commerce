package com.gtera.ui.profile.contactus

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.ContactUsLayoutBinding

class ContactUsFragment : BaseFragment<ContactUsLayoutBinding, ContactUsVM>(), ContactUsNavigator {
    override val layoutId: Int
        get() = R.layout.contact_us_layout
    override val viewModelClass: Class<ContactUsVM>
        get() = ContactUsVM::class.java

    override fun setNavigator(viewModel: ContactUsVM?) {
        viewModel?.setBaseNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }
    override val toolbarTitle: String?
        get() = getString(R.string.str_contact_us)
}