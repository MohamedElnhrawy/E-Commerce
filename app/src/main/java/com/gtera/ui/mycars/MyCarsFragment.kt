package com.gtera.ui.mycars

import android.view.View
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.databinding.MycarsLayoutBinding
import com.gtera.ui.common.ClickListener
import com.gtera.ui.helper.EmptyView

class MyCarsFragment :
    BaseFragment<MycarsLayoutBinding, MyCarsViewModel>(),
    MyCarsNavigator {
    override val layoutId: Int
        get() = R.layout.mycars_layout

    override val viewModelClass: Class<MyCarsViewModel>
        get() = MyCarsViewModel::class.java

    override fun setNavigator(viewModel: MyCarsViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return false
    }


    override val toolbarElevation: Boolean
        get() = false

    override val isListingView: Boolean
        get() = true


    override fun screenEmptyView(): EmptyView {

        return EmptyView(
            R.drawable.ic_my_cars_empty,
            getString(R.string.oops),
            R.string.str_my_car_empty_txt,
            object : ClickListener {
                override fun onViewClicked() {


                }
            },
            viewModel?.resourceProvider!!
        )
    }

    override val toolbarTitle: String?
        get() = getString(
            R.string.str_myCar
        )

    override fun viewRequiredLogIn() {
        if (!viewDataBinding.vsRequiredLogin.isInflated)
            viewDataBinding.vsRequiredLogin.viewStub!!.inflate()
        else
            viewDataBinding.vsRequiredLogin.viewStub!!.visibility = View.VISIBLE

        viewDataBinding.dataContainer.visibility = View.GONE
    }
}