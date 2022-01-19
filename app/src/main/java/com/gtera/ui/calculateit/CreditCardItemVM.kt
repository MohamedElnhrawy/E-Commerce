package com.gtera.ui.calculateit

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.CreditCard
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class CreditCardItemVM @Inject constructor() : BaseItemViewModel() {



    override val layoutId: Int
        get() = R.layout.credit_card_item_layout


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var cardNumber = ObservableField("")
    var cardName = ObservableField("")
    var cardLimits = ObservableField("")




    lateinit var resourceProviderObservable: ResourceProvider

    var creditCard: CreditCard? = null

    constructor(
        car: CreditCard?,
        resourceProvider: ResourceProvider
    ) : this() {
        resourceProviderObservable = resourceProvider
        this.creditCard = car
        cardNumber.set(creditCard?.id)
    }



    fun isValidCard():Boolean{
        return !TextUtils.isEmpty(cardName.get()) && !TextUtils.isEmpty(cardLimits.get())
    }

    fun getCard():CreditCard{
        return  CreditCard(creditCard?.id,cardName.get(),cardLimits.get())
    }
}