package com.gtera.ui.authorization.resetpassword

import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.ResetPasswordLayoutBinding
import javax.inject.Inject

class ResetPasswordActivity : BaseActivity<ResetPasswordLayoutBinding, ResetPasswordViewModel>(),
    ResetPasswordNavigator {

    @Inject
    lateinit var forgotPasswordViewModel: ResetPasswordViewModel




    override fun getLayoutRes(): Int {
        return R.layout.reset_password_layout
    }

    override val viewModelClass: Class<ResetPasswordViewModel>
        get() = ResetPasswordViewModel::class.java

    override fun setNavigator(viewModel: ResetPasswordViewModel?) {
        viewModel?.setNavigator(this)
    }

    override fun hasToolbar(): Boolean {
        return false
    }
}
