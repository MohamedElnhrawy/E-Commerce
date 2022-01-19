package com.gtera.event

class ErrorEvent : Event<ErrorData?>() {
    fun setViewValue(errorData: ErrorData?) {
        setValue(errorData)
    }
}