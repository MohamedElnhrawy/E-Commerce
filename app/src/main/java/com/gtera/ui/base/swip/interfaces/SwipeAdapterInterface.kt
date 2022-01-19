package com.gtera.ui.base.swip.interfaces

interface SwipeAdapterInterface {
    fun getSwipeLayoutResourceId(position: Int): Int
    fun notifyDatasetChanged()
}