package com.gtera.ui.base

import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel

abstract class BaseItemViewModel : ViewModel() {
    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun hashKey(): Any

    open fun filter(searchKey:String): Boolean {
        return this.javaClass.name.contains(searchKey)
    }
}