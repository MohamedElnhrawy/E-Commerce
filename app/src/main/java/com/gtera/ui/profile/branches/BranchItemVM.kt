package com.gtera.ui.profile.branches

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Branche
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class BranchItemVM @Inject constructor(): BaseItemViewModel() {
    override val layoutId: Int
        get() = R.layout.branch_item_layout

    override fun hashKey(): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var branchName = ObservableField("")

    constructor(branch: Branche,
                resourceProvider: ResourceProvider
    ) : this() {
       this.branchName.set(branch.name)

    }

}