package com.gtera.ui.mycars.add

import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.AddMyCarLayoutBinding

class AddMyCarActivity :
    BaseActivity<AddMyCarLayoutBinding, AddMyCarViewModel>(),
    AddMyCarNavigator {


    override val viewModelClass: Class<AddMyCarViewModel>
        get() = AddMyCarViewModel::class.java

    override fun setNavigator(viewModel: AddMyCarViewModel?) {
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
        get() = getString(R.string.str_add_my_car_header)

    override fun getLayoutRes(): Int {
        return R.layout.add_my_car_layout
    }
}