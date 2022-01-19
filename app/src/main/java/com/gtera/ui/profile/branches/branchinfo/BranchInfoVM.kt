package com.gtera.ui.profile.branches.branchinfo

import com.gtera.base.BaseViewModel
import com.gtera.data.model.Branche
import com.gtera.di.providers.ResourceProvider
import javax.inject.Inject

class BranchInfoVM @Inject constructor() : BaseViewModel<BranchInfoNavigator>(){

    var branche:Branche? = null

    constructor(
        branche: Branche,
        navigator: BranchInfoNavigator,
        resourceProvider: ResourceProvider
    ) : this() {
        setNavigator(navigator)
        this.branche = branche
        this.resourceProvider = resourceProvider
    }


  fun performCall(){
        navigator?.performCall(branche?.phoneNumber)
    }

    fun contactUs(){
        navigator?.contactUs(branche)
    }
}