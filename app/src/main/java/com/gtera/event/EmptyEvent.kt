package com.gtera.event

class EmptyEvent : Event<Boolean?>() {
    fun setViewValue(size: Int) {
        setValue(size <= 0)
    }
}