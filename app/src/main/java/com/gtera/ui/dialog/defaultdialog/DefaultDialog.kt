package com.gtera.ui.dialog.defaultdialog

import android.os.Bundle
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.DefaultDialogLayoutBinding
import com.gtera.ui.dialog.BaseDialog
import javax.inject.Inject

class DefaultDialog :
    BaseDialog<DefaultDialogLayoutBinding?, DefaultDialogViewModel?>() {


    override var viewModel: DefaultDialogViewModel? = null
        @Inject
        internal set
    override val layoutId: Int
        get() = R.layout.default_dialog_layout

    private fun setViewModel(viewModel: DefaultDialogViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun getInstance(
            title: String?,
            message: String?,
            positiveText: String?,
            negativeText: String?,
            navigator: DefaultDialogNavigator,
            currentActivityClass: Class<out BaseActivity<*, *>?>?
        ): DefaultDialog {
            val viewModel = DefaultDialogViewModel.getInstance(
                title!!,
                message!!,
                positiveText!!,
                negativeText,
                navigator,
                currentActivityClass
            )
            viewModel.setNavigator(navigator)
            val instance = DefaultDialog()
            val bundle = Bundle()
            //        bundle.putString(VIEW_MODEL_KEY,new Gson().toJson(viewModel));          //Must be serializable
            instance.arguments = bundle
            instance.setViewModel(viewModel) //Not Recommended
            //TODO: another way of setting viewModel must be set
            return instance
        }

        fun getInstance(
            title: String?,
            message: String?,
            positiveText: String?,
            negativeText: String?,
            navigator: DefaultDialogNavigator,
            currentActivityClass: Class<out BaseActivity<*, *>?>?,
            cancelable: Boolean
        ): DefaultDialog {
            val viewModel = DefaultDialogViewModel.getInstance(
                title!!,
                message!!,
                positiveText!!,
                negativeText,
                navigator,
                currentActivityClass
            )
            viewModel.setNavigator(navigator)
            val instance = DefaultDialog()
            val bundle = Bundle()
            //        bundle.putString(VIEW_MODEL_KEY,new Gson().toJson(viewModel));          //Must be serializable
            instance.arguments = bundle
            instance.setViewModel(viewModel) //Not Recommended
            if (BaseDialog.instance != null) {
                BaseDialog.instance!!.isCancelable = cancelable
            }
            //TODO: another way of setting viewModel must be set
            return instance
        }
    }
}