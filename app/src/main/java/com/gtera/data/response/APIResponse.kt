package com.gtera.data.response

import com.gtera.data.error.ErrorDetails

class APIResponse<T> {
    var response: T? = null
        private set
    var error: ErrorDetails? = null
        private set
    var isSuccess: Boolean
        private set

    constructor() {
        isSuccess = true
    }

    constructor(response: T) {
        this.response = response
        isSuccess = true
    }

    constructor(error: ErrorDetails?) {
        this.error = error
        isSuccess = false
    }

}