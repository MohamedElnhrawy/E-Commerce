package com.gtera.event

import com.gtera.ui.helper.EmptyView

class ErrorData {
    var isError = false
    var data: EmptyView
        private set

    constructor() {
        data = EmptyView()
    }

    constructor(isError: Boolean, data: EmptyView?) {
        this.isError = isError
        this.data = data!!
    }

}