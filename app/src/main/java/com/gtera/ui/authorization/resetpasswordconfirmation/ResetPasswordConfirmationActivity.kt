package com.gtera.ui.authorization.resetpasswordconfirmation

import android.os.Bundle
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.ResetPasswordConfirmationLayoutBinding
import javax.inject.Inject

class ResetPasswordConfirmationActivity : BaseActivity<ResetPasswordConfirmationLayoutBinding, ResetPasswordConfirmationViewModel>(),
    ResetPasswordConfirmationNavigator {

    @Inject
    lateinit var resetPasswordConfirmationViewModel: ResetPasswordConfirmationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.goToLoginBtn.setOnClickListener({

            viewModel?.backToLogin()
        })
    }

    override fun getLayoutRes(): Int {
        return R.layout.reset_password_confirmation_layout
    }

    override val viewModelClass: Class<ResetPasswordConfirmationViewModel>
        get() = ResetPasswordConfirmationViewModel::class.java

    override fun setNavigator(viewModel: ResetPasswordConfirmationViewModel?) {
        viewModel?.setNavigator(this)
    }

    override fun hasToolbar(): Boolean {
        return false
    }
}
