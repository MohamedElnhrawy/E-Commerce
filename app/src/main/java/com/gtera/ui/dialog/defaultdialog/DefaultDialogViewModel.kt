package com.gtera.ui.dialog.defaultdialog

import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.base.BaseViewModel
import com.gtera.di.providers.ResourceProvider
import javax.inject.Inject

class DefaultDialogViewModel @Inject constructor() : BaseViewModel<DefaultDialogNavigator?>() {
    var title = ""
    var message = ""
    var positiveBtnText = ""
    var negativeBtnText = ""
    var hasNegativeButton = true
    fun onPositiveButtonClicked() {
        navigator!!.onPositiveButtonClicked()
    }

    fun onNegativeButtonClicked() {
        navigator!!.onNegativeButtonClicked()
    }

    constructor(
        title: String, message: String, positiveText: String,
        negativeText: String?, navigator: DefaultDialogNavigator?,
        currentActivityClass: Class<out BaseActivity<*, *>?>?
    ) : this(){

//        super() TODO
        setNavigator(navigator)
        this.title = title
        this.message = message
        positiveBtnText = positiveText
        if (negativeText == null) hasNegativeButton = false else negativeBtnText = negativeText

    }



    companion object {


        fun getInstance(
            positiveText: String, negativeText: String?,
            navigator: DefaultDialogNavigator?,
            currentActivityClass: Class<out BaseActivity<*,*>?>?,
            resourceProvider: ResourceProvider
        ): DefaultDialogViewModel {
            return getInstance(
                resourceProvider.getString(R.string.error_title),
                resourceProvider.getString(R.string.error_message),
                positiveText, negativeText, navigator, currentActivityClass
            )
        }

        @JvmStatic
        fun getInstance(
            title: String,
            message: String,
            positiveText: String,
            negativeText: String?,
            navigator: DefaultDialogNavigator?,
            currentActivityClass: Class<out BaseActivity<*,*>?>?
        ): DefaultDialogViewModel {
            return DefaultDialogViewModel(
                title,
                message,
                positiveText,
                negativeText,
                navigator,
                currentActivityClass
            )
        }
    }


}