package com.gtera.event

import androidx.annotation.CallSuper

class LoadingEvent : Event<Boolean?>() {
    private var loadingCount = 0
    @CallSuper
    @Synchronized
    fun showLoading() {
        if (++loadingCount == 1) setValue(true)
    }

    @CallSuper
    @Synchronized
    fun hideLoading() {
        if (--loadingCount == 0) setValue(false)
    }
}