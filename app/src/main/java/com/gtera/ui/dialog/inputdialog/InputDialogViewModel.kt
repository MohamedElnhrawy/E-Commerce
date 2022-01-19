package com.gtera.ui.dialog.inputdialog

import androidx.databinding.ObservableField
import com.gtera.base.BaseViewModel
import javax.inject.Inject

class InputDialogViewModel @Inject constructor() :
    BaseViewModel<InputDialogNavigator?>() {
    var isCancelable = false
    var inputTitle = ObservableField("")
    var buttonText = ObservableField("")
    var inputText = ObservableField("")
    var inputTextError = ObservableField("")



    constructor(
        titleMessage: String, buttonText: String,
        navigator: InputDialogNavigator, currentActivityClass: Class<*>?
    ) : this() {
        setNavigator(navigator)
        this.inputTitle.set(titleMessage)
        this.buttonText.set(buttonText)
    }




    fun setIsCancelable(isCancelable: Boolean) {
        this.isCancelable = isCancelable
    }

    fun onButtonClicked() {
        navigator!!.onSendButtonClicked(inputText.get()!!)
    }

}