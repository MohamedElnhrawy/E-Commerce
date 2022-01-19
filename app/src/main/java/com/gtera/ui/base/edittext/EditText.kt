package com.gtera.ui.base.edittext

import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.TextView

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gtera.BR
import com.gtera.R
import com.gtera.utils.Utilities


class EditText(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    //    private EditTextLayoutBinding binding;
    private var binding: ViewDataBinding? = null

    private val viewEditText: TextInputEditText
        get() = binding!!.root.findViewById(R.id.et_name)

    private val viewTextView: TextView
        get() = binding!!.root.findViewById(R.id.tv_info_header_txt)

    private val viewInputLayout: TextInputLayout
        get() = binding!!.root.findViewById(R.id.il_name)

    //        return binding.etName;
    val inputET: TextInputEditText
        get() = viewEditText

    //        return binding.getEditText();
    //        binding.setEditText(txt);
    var editText: String?
        get() = if (viewEditText.text == null) "" else viewEditText.text!!.toString()
        set(txt) {
            binding!!.setVariable(BR.editText, txt)
        }

    init {
        init(context, attrs)
    }


    fun init(context: Context, attrs: AttributeSet) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_text_layout, this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.EditText)
        editText = attributes.getString(R.styleable.EditText_editText)
        setHint(attributes.getString(R.styleable.EditText_hint))
        setMessage(attributes.getString(R.styleable.EditText_message))
        setLabel(attributes.getString(R.styleable.EditText_label))
        setLabelSize(attributes.getDimension(R.styleable.EditText_labelSize,
                Utilities.getDimenValueFromRes(R.dimen._15ssp,viewTextView.context)))

        setInputType(attributes.getInt(R.styleable.EditText_inputType, 0))
        setNumOfLines(attributes.getInt(R.styleable.EditText_numoflines, 1))
        setLabelColor(attributes.getInt(R.styleable.EditText_labelColor, R.color.colorTextSecondary))

        setIsSuccess(attributes.getBoolean(R.styleable.EditText_isSuccess, false))
        setEnablePasswordToggle(
            attributes.getBoolean(
                R.styleable.EditText_passwordToggleEnabled,
                false
            )
        )


        attributes.recycle()
    }

    fun setHint(txt: String?) {
        viewEditText.hint = txt
        //        binding.etName.setHint(txt);
    }

    fun setLabel(txt: String?) {
        viewTextView.text = txt
        //        binding.tvTitle.setText(txt);
    }

    fun setLabelSize(textSize: Float) {
        var density = getResources().getDisplayMetrics().density;
        viewTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize/density)
        //        binding.tvTitle.setTextSize(textSize);
    }

    fun setLabelColor(color: Int) {
        viewTextView.setTextColor(color)
        //        binding.tvTitle.setTextColor(color);
    }

    fun setMessage(txt: String?) {
        binding!!.setVariable(BR.message, txt)
        //        binding.setMessage(txt);
    }

    fun setIsSuccess(isSuccess: Boolean) {
        binding!!.setVariable(BR.isSuccess, isSuccess)
        //        binding.setIsSuccess(isSuccess);
    }

    fun setEnablePasswordToggle(enablePasswordToggle: Boolean) {
        val v = binding!!.root.findViewById<TextInputEditText>(R.id.il_name) as TextInputLayout
        v.isPasswordVisibilityToggleEnabled = enablePasswordToggle
    }

    fun setNumOfLines(numOfLines: Int) {

        val inputEditText = binding!!.root.findViewById<TextInputEditText>(R.id.et_name)
        inputEditText.setLines(numOfLines)

    }

    fun setInputType(inputType: Int) {
        //        TextInputEditText inputEditText = binding.etName;
        val inputEditText = binding!!.root.findViewById<TextInputEditText>(R.id.et_name)

        when (inputType) {
            2 -> {
                inputEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                inputEditText.setTypeface(Typeface.DEFAULT)
            }
            3 -> inputEditText.inputType = InputType.TYPE_CLASS_NUMBER
            4 -> {
                inputEditText.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                inputEditText.setTypeface(Typeface.DEFAULT)
            }
            5 -> inputEditText.inputType = InputType.TYPE_CLASS_PHONE
            6 -> inputEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            7 -> inputEditText.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            8 -> inputEditText.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            0, 1 -> inputEditText.inputType = InputType.TYPE_CLASS_TEXT
            else -> inputEditText.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

}
