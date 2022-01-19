package com.gtera.ui.mycars.add

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.ui.base.BaseItemViewModel
import com.example.famousapp.famous.utils.display.ScreenUtils
import javax.inject.Inject

class MyCarAddImageItemViewModel @Inject constructor() : BaseItemViewModel() {

    var isUploadItem = false
    var context: Context? = null
    var isUriPath = ObservableBoolean(false)
    var imagePath = ObservableField("")
    var imageRoundedCorner = ObservableField(0)
    var itemSize = ObservableField(0)
    var itemHeight = ObservableField(0)
    var imageBitmap = ObservableField<Bitmap?>()


    constructor(isUploadItem: Boolean, context: Context?) : this() {
        this.isUploadItem = isUploadItem
        setItemSize(context, 1f, 2.7f)
    }

    constructor(bitmap: Bitmap?, context: Context?) : this() {
        imageBitmap.set(bitmap)
        this.context = context
        setItemSize(context, 1.6f, 2.7f)
    }

    constructor(imageUrl: String?) : this() {
        this.imagePath.set(imageUrl)
        setItemSize(context, 1.6f, 2.7f)
    }

    constructor(
        path: String,
        isUriPath: Boolean,
        context: Context?
    ) : this() {
        imagePath.set(path)
        this.isUriPath.set(isUriPath)
        this.context = context
        setItemSize(context, 1.6f, 2.7f)

    }

    private fun setItemSize(context: Context?, widthRatio: Float, heightRatio: Float) {
        val screenWidth: Int
        val itemWidth: Int
        screenWidth = ScreenUtils.getScreenWidth(context) * 95 / 100
        itemSize.set((screenWidth / widthRatio).toInt())
        itemHeight.set((screenWidth / heightRatio).toInt())
    }

    val bitmapImage: Bitmap?
        get() = if (imageBitmap.get() != null) imageBitmap.get() else if (isUriPath.get()) MediaStore.Images.Media.getBitmap(
            context!!.contentResolver,
            Uri.parse(imagePath.get())
        ) else null

    override val layoutId: Int
        get() = if (isUploadItem) R.layout.my_car_image_upload_layout else R.layout.my_car_image_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }


}