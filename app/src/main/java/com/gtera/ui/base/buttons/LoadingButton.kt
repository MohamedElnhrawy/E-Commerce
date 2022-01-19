package com.gtera.ui.base.buttons


import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.gtera.R
import com.gtera.databinding.LoadingButtonBinding

class LoadingButton(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

     var binding: LoadingButtonBinding? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater, R.layout.loading_button,
            this, true
        )

        binding!!.isEnabled = true
        binding!!.isLoading = false

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)

        setBtnText(attributes.getString(R.styleable.LoadingButton_text))
        setBtnBackgroundDrawable(attributes.getDrawable(R.styleable.LoadingButton_background))
        setBtnAltBackgroundDrawable(attributes.getDrawable(R.styleable.LoadingButton_altBackground))
        setBtnStartDrawableIcon(attributes.getDrawable(R.styleable.LoadingButton_startDrawableIcon))


        attributes.recycle()
    }

    fun setBtnText(text: String?) {
        binding!!.btnText = text
    }

    fun setIsLoading(isLoading: Boolean) {
        binding!!.isLoading = isLoading
    }

    fun setIsEnabled(isEnabled: Boolean) {
        binding!!.isEnabled = isEnabled
    }

    fun setBtnBackgroundDrawable(drawable: Drawable?) {
        binding!!.backgroundDrawable =
            drawable ?: resources.getDrawable(R.drawable.rounded_corners_primary, null)
    }

    fun setBtnAltBackgroundDrawable(drawable: Drawable?) {
        binding!!.altBackgroundDrawable =
            drawable ?: resources.getDrawable(R.drawable.rounded_corners_light, null)
    }

    fun setBtnStartDrawableIcon(drawable: Drawable?) {
        binding!!.btnStartDrawableIcon =
            drawable ?: resources.getDrawable(R.drawable.rounded_corners_light, null)
    }

    fun setClickListener(listener: View.OnClickListener) {
        binding!!.clickListener = listener
        binding?.btn?.setOnClickListener(listener)
    }
}
