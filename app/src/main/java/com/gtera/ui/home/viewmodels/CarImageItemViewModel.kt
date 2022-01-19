package com.gtera.ui.home.viewmodels

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.CarMediaType
import com.gtera.utils.Utilities
import javax.inject.Inject

class CarImageItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = if (carMediaType?.equals(CarMediaType.CAR_IMAGE)!!) R.layout.car_image_item_list_layout else R.layout.car_video_item_list_layout

    var carMediaType: CarMediaType? = null

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var imageUrl = ObservableField("")
    var videoUrl = ObservableField("")

    constructor(carMedia: String?, carMediaType: CarMediaType?) : this() {

        this.carMediaType = carMediaType
        if (carMediaType?.equals(CarMediaType.CAR_IMAGE)!! && !Utilities.isNullString(carMedia)) imageUrl.set(
            carMedia
        ) else if (!Utilities.isNullString(carMedia)) videoUrl.set(
            carMedia
        )
    }


}