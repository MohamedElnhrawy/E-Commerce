package com.gtera.ui.profile.favorites

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Favourite
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.FavoriteType
import com.gtera.utils.Utilities
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FavoriteItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = when {
            Objects.equals(
                favoriteType!!.get(),
                FavoriteType.USED_CAR
            ) -> R.layout.favorite_used_item_list_layout
            Objects.equals(
                favoriteType!!.get(),
                FavoriteType.NEW_CAR
            ) -> R.layout.favorite_new_car_item_list_layout
            Objects.equals(
                favoriteType!!.get(),
                FavoriteType.OFFER
            ) ->  favourite?.offer.let { if(it?.hotDeal!!) R.layout.favorite_top_deals_item_list_layout else R.layout.favorite_offers_item_list_layout }

            else -> R.layout.car_item_list_layout
        }

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var isFavoriteCar = ObservableField(false)
    var favoriteType: ObservableField<FavoriteType>? = null
    var resourceProviderObservable: ObservableField<ResourceProvider>? = null

    var favourite: Favourite? = null

    var imageUrl = ObservableField("")
    var carName = ObservableField("")
    var usedcarDate = ObservableField("")
    var carPrice = ObservableField("")


    // offer
    var offerName = ObservableField("")
    var offerAdvaced = ObservableField("")
    var offerMonthly = ObservableField("")
    var offerDiscount = ObservableField("")


    constructor(
        favourite: Favourite?,
        resourceProvider: ResourceProvider
    ) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        this.favourite = favourite
        setupFavorite(favourite)
    }

    constructor(
        favourite: Favourite?,
        resourceProvider: ResourceProvider,
        type: FavoriteType
    ) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        this.favourite = favourite
        this.favoriteType = ObservableField(type)
        setupFavorite(favourite)
    }


    private fun setupFavorite(favourite: Favourite?) {
        this.favourite = favourite

        when (favoriteType?.get()) {

            FavoriteType.USED_CAR ,  FavoriteType.NEW_CAR -> {
                setupCar(favourite)
            }
            FavoriteType.OFFER -> {
                favourite?.offer?.let { if(it.hotDeal!!) setupOffer(favourite)}
            }

        }


    }

    @SuppressLint("SimpleDateFormat")
    private fun setupCar(favourite: Favourite?) {

        if (!Utilities.isNullString(favourite?.car?.images?.get(0))) imageUrl.set(
            favourite?.car?.images?.get(0)
        )

        favourite?.car?.priceFrom.let { carPrice.set(favourite?.car?.priceFrom) }
        carName.set(favourite?.car?.brand?.name.let { it?:"" + " | " }  + favourite?.car?.name.let { it?:""+ " | " }  + favourite?.car?.manufactureYear.let { it?:"" })
        if (favourite?.car?.used!!) {
            usedcarDate.set(
                Utilities.formatDateTime(
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(
                        favourite.car.createdAt!!
                    )
                )
            )
        }
        isFavoriteCar.set(favourite.car.isFavorite)
    }

    fun setupOffer(favourite: Favourite?) {

        if (!Utilities.isNullString(favourite?.offer?.car?.images?.get(0))) imageUrl.set(
            favourite?.offer?.car?.images?.get(0)
        )
        offerName.set(favourite?.offer?.name)
        offerAdvaced.set(
           resourceProviderObservable?.get()
                ?.getString(R.string.str_egp,  favourite?.offer?.advance)
        )
        offerDiscount.set(
            resourceProviderObservable?.get()
                ?.getString(R.string.str_discount_off,  favourite?.offer?.discount)
        )
        offerMonthly.set(
             resourceProviderObservable?.get()
                ?.getString(R.string.str_egp, favourite?.offer?.monthlyPayment)
        )
        isFavoriteCar.set(favourite?.offer?.car?.isFavorite)


    }

}