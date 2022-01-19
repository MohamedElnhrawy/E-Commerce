package com.gtera.data.interfaces

import com.gtera.data.error.ErrorDetails

interface APICommunicatorListener<T>  {
    fun onSuccess(result: T)
    fun onError(throwable: ErrorDetails?)
}