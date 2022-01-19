package com.gtera.event

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class Event<V> {
    private var liveData: MutableLiveData<V>? = null

    open fun Event() {}

    open fun getLiveData(): LiveData<V>? {
        if (liveData == null) liveData = MutableLiveData()
        return liveData
    }

    protected open fun getMutableLiveData(): MutableLiveData<V>? {
        return liveData
    }

    protected open fun setValue(value: V) {
        if (liveData == null) liveData = MutableLiveData()
        if (Looper.myLooper() == Looper.getMainLooper()) {
            liveData!!.setValue(value)
        } else {
            liveData!!.postValue(value)
        }
    }

    protected open fun resetLiveData() {
        liveData = null
    }
}