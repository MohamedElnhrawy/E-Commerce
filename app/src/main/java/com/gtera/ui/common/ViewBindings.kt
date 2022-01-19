package com.gtera.ui.common

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Binding component for view clicks.
 */
interface ViewBindings {
    /**
     * Bind view clicks to listener
     *
     * @param view     View to add click listener to
     * @param listener Click listener
     */
    @BindingAdapter("android:onClick")
    fun bindListener(
        view: View?,
        listener: View.OnClickListener?
    )

    @BindingAdapter("multipleClick")
    fun enableMultipleClick(
        view: View?,
        isMultiClick: Boolean
    )
}