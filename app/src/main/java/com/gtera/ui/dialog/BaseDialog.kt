package com.gtera.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setDefaultComponent
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.gtera.BR
import com.gtera.base.BaseActivity
import com.gtera.base.BaseViewModel
import com.gtera.ui.common.LifecycleComponent
import java.util.*

abstract class BaseDialog<T : ViewDataBinding?, V : BaseViewModel<*>?> :
    DialogFragment() {
    var viewDataBinding: ViewDataBinding? = null
        private set
    private var isCancelble = false
    @get:LayoutRes
    abstract val layoutId: Int

    abstract val viewModel: V
    private val bindingVariable: Int
        private get() = BR.viewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*,*>) {
            context.onFragmentAttached()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object :
            Dialog(Objects.requireNonNull(activity)!!, theme) {
            override fun onBackPressed() {
                if (isCancelable) Objects.requireNonNull(activity)?.onBackPressed()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setDefaultComponent(LifecycleComponent(lifecycle))
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding!!.setVariable(bindingVariable, viewModel)
        viewDataBinding!!.executePendingBindings()
        Objects.requireNonNull(dialog)?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog!!.window != null) dialog!!.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog!!.setCancelable(isCancelable)
        dialog!!.setCanceledOnTouchOutside(isCancelable)
        return viewDataBinding!!.root
    }

    override fun onDetach() {
        super.onDetach()
    }

    protected fun setDialogCancelable(cancelable: Boolean) {
        isCancelable = cancelable
    }

    companion object {
         const val VIEW_MODEL_KEY = "view_model"
         val instance: BaseDialog<*, *>?
            get() = null
    }
}