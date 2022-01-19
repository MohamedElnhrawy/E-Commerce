package com.gtera.ui.base.edittext

import android.text.Editable
import android.text.TextWatcher

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods

@InverseBindingMethods(InverseBindingMethod(type = EditText::class, attribute = "editText"))
object EditTextBinding {
    @BindingAdapter("message")
    @JvmStatic
    fun setMessage(view: EditText, value: String) {
        view.setMessage(value)
    }

    @BindingAdapter("editText")
    @JvmStatic
    fun setEditText(view: EditText, value: String) {
        view.editText = value
    }

    @BindingAdapter( "editTextAttrChanged")
    @JvmStatic
    fun setListener(view: EditText, textAttrChanged: InverseBindingListener?) {
        if (textAttrChanged != null) {
            view.inputET.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun afterTextChanged(editable: Editable) {
                    textAttrChanged.onChange()
                }
            })
        }
    }

    @InverseBindingAdapter(attribute = "editText")
    @JvmStatic
    fun getEditText(view: EditText): String {
        return view.editText!!
    }

    @BindingAdapter("isSuccess")
    @JvmStatic
    fun setIsSuccess(view: EditText, value: Boolean) {
        view.setIsSuccess(value)
    }
}
