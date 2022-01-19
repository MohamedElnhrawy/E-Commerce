package com.gtera.ui.home.viewmodels

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.gtera.R
import com.gtera.data.model.Car
import com.gtera.data.model.Offer
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.ListOrientation
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import javax.inject.Inject

class CartItemViewModel @Inject constructor() : BaseItemViewModel() {


    var isFavoriteCar = ObservableField(false)
    var resourceProvider: ResourceProvider? = null
    override val layoutId: Int
        get() = if (orientation?.equals(ListOrientation.ORIENTATION_VERTICAL)!!)
            R.layout.offers_item_vertical_layout
        else R.layout.offers_item_horizontal_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var offerID = ObservableInt(-1)
    var offerItem: Offer? = null
    var resourceProviderObservable: ObservableField<ResourceProvider>? = null
    var orientation: ListOrientation? = null
    private var offersItem: Offer? = null
    var car: Car? = null
    var imageUrl = ObservableField("")
    var offerName = ObservableField("")
    var offerAdvanced = ObservableField("")
    var offerMonthly = ObservableField("")
    var offerDiscount = ObservableField("")


    constructor(
        offersItem: Offer?,
        resourceProvider: ResourceProvider,
        orientation: ListOrientation
    ) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        this.resourceProvider = resourceProvider
        this.orientation = orientation
        this.car =  offersItem?.car
        this.offerItem =offersItem
        this.isFavoriteCar.set(offersItem?.isFavorite)
        setupOffer(offersItem)
    }


    fun setupOffer(offersItem: Offer?) {
        if (!Utilities.isNullString(offersItem?.car?.images?.get(0))) imageUrl.set(
            offersItem?.car?.images?.get(0)
        )
        offerName.set(offersItem?.name)
        offerAdvanced.set(
            resourceProvider?.getString(
                R.string.str_advance_of, resourceProvider?.getString(
                    R.string.str_egp, offersItem?.advance
                )
            )
        )
        offerDiscount.set(
            resourceProvider?.getString(
                if (offersItem?.discountType?.equals(
                        APPConstants.OFFER_PERCENTAGE
                    )!!
                ) R.string.str_percentage else R.string.str_egp,
                offersItem?.discount
            )
        )
        offerMonthly.set(
            resourceProvider?.getString(
                  R.string.str_egp, offersItem?.monthlyPayment
            )
        )

    }

}