package com.gtera.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import com.gtera.R
import com.gtera.data.model.Slider
import com.bumptech.glide.Glide
import com.zhpan.bannerview.holder.ViewHolder
import javax.inject.Inject

class CustomViewHolder : ViewHolder<Slider> {

    private var isLarge: Boolean
    lateinit var context: Context
        @Inject set

    constructor(isLarge: Boolean) {
        this.isLarge = isLarge
    }

    constructor() {
        isLarge = false
    }

    override fun getLayoutId(): Int {
        return if (isLarge) R.layout.large_banner_item else R.layout.banner_item
    }

    @SuppressLint("CheckResult")
    override fun onBind(
        itemView: View,
        data: Slider,
        position: Int,
        size: Int
    ) {
        val imageView =
            itemView.findViewById<ImageView>(R.id.banner_image)
        val context = imageView.context
//        val options = getImagePlaceholder(context)
//        options!!.transform(CenterCrop(), RoundedCorners(5))
        Glide.with(context)
            .load(data.image)
//            .apply(options)
            .into(imageView)
    }
}