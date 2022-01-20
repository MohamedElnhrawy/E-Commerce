package com.gtera.ui.authorization.otp

import android.app.Activity
import android.os.Bundle
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.OtpLayoutBinding
import javax.inject.Inject


class OtpActivity : BaseActivity<OtpLayoutBinding, OtpViewModel>(), OtpNavigator{

    @Inject
    lateinit var signInViewModel: OtpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override val viewModelClass: Class<OtpViewModel>
        get() = OtpViewModel::class.java

    override fun getLayoutRes(): Int {
        return R.layout.otp_layout
    }

    override fun setNavigator(viewModel: OtpViewModel?) {
        viewModel?.setNavigator(this)
    }

    override val toolbarTitle: String
        get() = getString(R.string.phone_verification)

    override fun getActivity(): Activity {
        return this
    }




    override fun hasBack(): Boolean {
        return false
    }





}
