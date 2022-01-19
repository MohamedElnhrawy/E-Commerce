package com.gtera.ui.base.swip.interfaces

import com.gtera.ui.base.swip.SwipeLayout
import com.gtera.ui.base.swip.util.Attributes

interface SwipeItemMangerInterface {
    fun openItem(position: Int)
    fun closeItem(position: Int)
    fun closeAllExcept(layout: SwipeLayout?)
    fun closeAllItems()
    val openItems: List<Int?>?
    val openLayouts: List<SwipeLayout?>?
    fun removeShownLayouts(layout: SwipeLayout?)
    fun isOpen(position: Int): Boolean
    fun getMode(): Attributes.Mode?

    fun setMode(mode: Attributes.Mode?)
}