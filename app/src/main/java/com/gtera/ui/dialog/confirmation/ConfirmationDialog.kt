package com.gtera.ui.dialog.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gtera.R
import com.gtera.databinding.ConfirmationDialogLayoutBinding
import com.gtera.ui.dialog.BaseDialog
import com.google.gson.Gson
import javax.inject.Inject

class ConfirmationDialog :
    BaseDialog<ConfirmationDialogLayoutBinding?, ConfirmationDialogViewModel?>() {

    override var viewModel: ConfirmationDialogViewModel? = null
        public get() {
        return field
    }
        @Inject
        internal set
    override val layoutId: Int
        get() = R.layout.confirmation_dialog_layout

    private fun setViewModel(viewModel: ConfirmationDialogViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && requireArguments().containsKey(VIEW_MODEL_KEY)) viewModel =
            Gson().fromJson(
                requireArguments().getString(VIEW_MODEL_KEY),
                ConfirmationDialogViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setDialogCancelable(viewModel!!.isCancelable)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun getInstance(viewModel: ConfirmationDialogViewModel): ConfirmationDialog {
            val instance = ConfirmationDialog()
            val bundle = Bundle()
            //        bundle.putString(VIEW_MODEL_KEY,new Gson().toJson(viewModel));
            instance.setViewModel(viewModel) //Not Recommended
            instance.arguments = bundle
            return instance
        }
    }
}