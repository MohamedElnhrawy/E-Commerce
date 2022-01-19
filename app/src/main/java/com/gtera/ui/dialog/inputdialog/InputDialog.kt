package com.gtera.ui.dialog.inputdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gtera.R
import com.gtera.databinding.InputDialogLayoutBinding
import com.gtera.ui.dialog.BaseDialog
import com.google.gson.Gson
import javax.inject.Inject

class InputDialog :
    BaseDialog<InputDialogLayoutBinding?, InputDialogViewModel?>() {

    override var viewModel: InputDialogViewModel? = null
        get() {
            return field
        }
        @Inject
        internal set
    override val layoutId: Int
        get() = R.layout.input_dialog_layout

    private fun setViewModel(viewModel: InputDialogViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && requireArguments().containsKey(VIEW_MODEL_KEY)) viewModel =
            Gson().fromJson(
                requireArguments().getString(VIEW_MODEL_KEY),
                InputDialogViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setDialogCancelable(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun getInstance(viewModel: InputDialogViewModel): InputDialog {
            val instance = InputDialog()
            val bundle = Bundle()
            instance.setViewModel(viewModel) //Not Recommended
            instance.arguments = bundle
            return instance
        }
    }
}