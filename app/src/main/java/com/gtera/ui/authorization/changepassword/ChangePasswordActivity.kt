package com.gtera.ui.authorization.changepassword

import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.ChangePasswordLayoutBinding
import com.gtera.ui.authorization.resetpassword.ChangePasswordNavigator
import com.gtera.ui.authorization.resetpassword.ChangePasswordViewModel
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity<ChangePasswordLayoutBinding, ChangePasswordViewModel>(),
    ChangePasswordNavigator {

    @Inject
    lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun getLayoutRes(): Int {
        return R.layout.change_password_layout
    }

    override val viewModelClass: Class<ChangePasswordViewModel>
        get() = ChangePasswordViewModel::class.java

    override fun setNavigator(viewModel: ChangePasswordViewModel?) {
        viewModel?.setNavigator(this)
    }

    override val toolbarTitle: String?
        get() = getString(R.string.str_change_password_header)

    override fun hasBack(): Boolean {
        return true
    }
}
