package com.gtera.ui.slider

import androidx.databinding.BindingAdapter
import com.gtera.data.model.Slider
import com.gtera.ui.common.ViewHolderInterface

internal object SliderViewBinding {

    @JvmStatic
    @BindingAdapter("sliderItems")
    fun setSliderItems(view: SliderView, items: List<Slider?>?) {
        view.setSliderItems(items)
    }
    @JvmStatic
    @BindingAdapter("sliderItemListener")
    fun setSliderItemListener(view: SliderView, listener: ViewHolderInterface?) {
        view.setSliderItemListener(listener)
    }
}