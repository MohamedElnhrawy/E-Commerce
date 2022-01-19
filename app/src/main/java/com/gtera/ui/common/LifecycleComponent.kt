package com.gtera.ui.common

import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.gtera.utils.Utilities.hideSoftKeyboard
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class LifecycleComponent(private val lifecycle: Lifecycle) :
    androidx.databinding.DataBindingComponent,
    LifecycleObserver, ViewBindings {
    private var isMultipleClick = false


    private val disposables: CompositeDisposable?


    override fun getViewBindings(): ViewBindings {
        return this
    }

    override fun bindListener(
        view: View?,
        listener: View.OnClickListener?
    ) {
        if (isMultipleClick) {
            view!!.setOnClickListener { v: View? ->
                hideSoftKeyboard(v?.context!!, v!!)
                listener!!.onClick(v)
            }
            isMultipleClick = false
        } else {
            val dispose = view!!.clicks()
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(
                    { ignore: Unit ->
                        hideSoftKeyboard(
                            view.context,
                            view
                        )
                        listener!!.onClick(view)
                    },
                    {
                        Log.d("ClickThrowable", "Error: " + it.message)
                    }
                )
            disposables!!.add(dispose)
        }
    }

    override fun enableMultipleClick(
        view: View?,
        isMultiClick: Boolean
    ) {
        isMultipleClick = isMultiClick
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dispose() {
        if (disposables != null && disposables.size() > 0) disposables.dispose()
        lifecycle.removeObserver(this)
    }

    init {
        lifecycle.addObserver(this)
        disposables = CompositeDisposable()
    }
}