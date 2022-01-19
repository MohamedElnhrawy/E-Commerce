package com.gtera.ui.cars

import android.view.View
import androidx.databinding.ViewStubProxy
import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.data.model.Model
import com.gtera.databinding.CarsListLayoutBinding
import com.gtera.utils.APPConstants

class CarsFragment :
    BaseFragment<CarsListLayoutBinding, CarsViewModel>(),
    CarsNavigator {

    override val layoutId: Int
        get() = R.layout.cars_list_layout

    override val loadingView: ViewStubProxy
        get() = viewDataBinding.loadingView

    override val emptyView: ViewStubProxy
        get() = viewDataBinding.emptyView
    override val dataView: View
        get() = viewDataBinding.swrefcarlist



    override val viewModelClass: Class<CarsViewModel>
        get() = CarsViewModel::class.java

    override fun setNavigator(viewModel: CarsViewModel?) {
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
        get() {
            if (intentExtras == null || !intentExtras!!.containsKey(APPConstants.EXTRAS_KEY_MODEL))
                return getString(R.string.str_home_categories_used_cars)
            return (intentExtras?.getSerializable(APPConstants.EXTRAS_KEY_MODEL) as Model?)?.name
        }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_cars_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 6
    }
}