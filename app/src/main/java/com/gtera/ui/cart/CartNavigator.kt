package com.gtera.ui.cart

import com.gtera.data.model.Promotion

interface CartNavigator{
    fun getPromotion(): Promotion?
}