package com.gtera.utils.extention

import android.content.ContextWrapper
import android.view.View
import dagger.android.DaggerActivity

fun View.getParentActivity(): DaggerActivity?{
    var context = this.context
    while (context is ContextWrapper) {
        if (context is DaggerActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}