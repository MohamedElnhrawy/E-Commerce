package com.gtera.ui.slider

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.gtera.R
import com.gtera.data.model.Slider
import com.gtera.databinding.SliderViewLayoutBinding
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.utils.Utilities.isNullList
import java.util.*

class SliderView(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {
     var binding: SliderViewLayoutBinding? = null

    lateinit var sliderViewModel: SliderViewModel

    fun init(context: Context, attrs: AttributeSet?) {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            Objects.requireNonNull(inflater),
            R.layout.slider_view_layout, this, true
        )


        binding?.ivArrowLeft?.setOnClickListener {
            binding?.viewModel?.onPrevBannerClicked()
        }

        binding?.ivArrowRight?.setOnClickListener {
            binding?.viewModel?.onNextBannerClicked()
        }
//        if (binding?.getViewModel() == null) binding?.setViewModel(sliderViewModel)
    }


    fun setSliderItems(items: List<Slider?>?) {
        if (!isNullList(items)) {
            binding?.viewModel?.initSliderItems(items)
        }
    }

    fun setSliderItemListener(listener: ViewHolderInterface?) {
        if (listener != null) binding?.viewModel?.setClickListener(listener)
    }

    init {
        init(context, attrs)
    }
}