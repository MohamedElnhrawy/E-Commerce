package com.gtera.ui.common

import androidx.annotation.IdRes

 interface ViewHolderInterface {
    fun onViewClicked(position: Int, @IdRes id: Int)
}