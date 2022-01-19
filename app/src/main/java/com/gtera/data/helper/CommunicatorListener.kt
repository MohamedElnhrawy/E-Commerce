package com.gtera.data.helper

import androidx.lifecycle.MutableLiveData
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.response.APIResponse

open class CommunicatorListener<T>(private val liveAPIResponse: MutableLiveData<APIResponse<T>>) :
    APICommunicatorListener<T> {
    override fun onSuccess(result: T) {
        liveAPIResponse.value = APIResponse(result)
    }

    override fun onError(throwable: ErrorDetails?) {
        liveAPIResponse.value = APIResponse(throwable)
    }

}