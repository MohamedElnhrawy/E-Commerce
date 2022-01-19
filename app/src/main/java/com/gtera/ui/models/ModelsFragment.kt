package com.gtera.ui.models

import com.gtera.R
import com.gtera.base.BaseFragment
import com.gtera.data.model.Brand
import com.gtera.databinding.ModelsLayoutBinding
import com.gtera.utils.APPConstants

class ModelsFragment :
    BaseFragment<ModelsLayoutBinding, ModelsViewModel>(),
    ModelsNavigator {
    override val layoutId: Int
        get() = R.layout.models_layout

    override val viewModelClass: Class<ModelsViewModel>
        get() = ModelsViewModel::class.java

    override fun setNavigator(viewModel: ModelsViewModel?) {
        viewModel!!.setNavigator(this)
    }

    override fun hasBack(): Boolean {
        return true
    }

    override fun shimmerLoadingLayout(): Int {
        return R.layout.shimmer_models_layout
    }

    override fun shimmerLoadingCount(): Int {
        return 6
    }


    override val isListingView: Boolean
        get() = true

    override val toolbarElevation: Boolean
        get() = false


    override val toolbarTitle: String?
        get() = (intentExtras?.getSerializable(APPConstants.EXTRAS_KEY_BRAND) as Brand)?.name


}