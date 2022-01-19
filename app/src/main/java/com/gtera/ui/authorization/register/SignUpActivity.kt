package com.gtera.ui.authorization.register

import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.SignUpLayoutBinding
import javax.inject.Inject

class SignUpActivity : BaseActivity<SignUpLayoutBinding, SignUpViewModel>(), SignUpNavigator {

    @Inject
    lateinit var signUpViewModel: SignUpViewModel




    override fun getLayoutRes(): Int {
        return R.layout.sign_up_layout
    }

    override val toolbarTitle: String?
        get() = getString(R.string.sign_up)

    override val viewModelClass: Class<SignUpViewModel>
        get() = SignUpViewModel::class.java

    override fun setNavigator(viewModel: SignUpViewModel?) {
        viewModel?.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true;
    }


}
