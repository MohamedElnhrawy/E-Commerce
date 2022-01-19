package com.gtera.ui.profile.branches.branchinfo

import com.gtera.data.model.Branche

interface BranchInfoNavigator {

    fun performCall(string: String?)
    fun contactUs(branche: Branche?)
}