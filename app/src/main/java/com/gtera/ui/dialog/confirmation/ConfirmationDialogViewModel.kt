package com.gtera.ui.dialog.confirmation

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import com.gtera.base.BaseViewModel
import javax.inject.Inject

class ConfirmationDialogViewModel @Inject constructor() :
    BaseViewModel<ConfirmationDialogNavigator?>() {
    var isCancelable = false
    var confirmationMessage = ObservableField("")
    var confirmationTitle = ObservableField("")
    var confirmationIcon = ObservableField<Drawable>()
    var confirmationIconBackgroundColor = ObservableField<Drawable>()
    var positiveTxt = ObservableField("")
    var negativeTxt = ObservableField("")
    var isInformationDailoge = ObservableField(false)

    constructor(
        confirmationMessage: String?,
        navigator: ConfirmationDialogNavigator, currentActivityClass: Class<*>?
    ) : this() {
        setNavigator(navigator)
        this.confirmationMessage.set(confirmationMessage)
    }

    constructor(
        confirmationMessage: String,  icon: Drawable,
        navigator: ConfirmationDialogNavigator, currentActivityClass: Class<*>?
    ) : this() {
        setNavigator(navigator)
        this.confirmationMessage.set(confirmationMessage)
        confirmationIcon.set(icon)
    }

    constructor(
        icon: Drawable,
        confirmationMessage: String,
        positiveTxt: String,
        negativeTxt: String,
        navigator: ConfirmationDialogNavigator,
        currentActivityClass: Class<*>?
    ) : this(confirmationMessage, icon, navigator, currentActivityClass) {
        this.positiveTxt.set(positiveTxt)
        this.negativeTxt.set(negativeTxt)
    }



    constructor(
        icon: Drawable,
        hasTitle:Boolean,
        confirmationMessage: String,
        confirmationTitle: String,
        positiveTxt: String,
        negativeTxt: String,
        navigator: ConfirmationDialogNavigator,
        confirmationIconBackgroundColor:Drawable,
        currentActivityClass: Class<*>?
    ) : this(confirmationMessage, icon, navigator, currentActivityClass) {
        this.confirmationTitle.set(confirmationTitle)
        this.confirmationIconBackgroundColor.set(confirmationIconBackgroundColor)
        this.isInformationDailoge.set(hasTitle)
        this.positiveTxt.set(positiveTxt)
        this.negativeTxt.set(negativeTxt)
    }

    fun setIsCancelable(isCancelable: Boolean) {
        this.isCancelable = isCancelable
    }

    fun onYesButtonClicked() {
        navigator!!.onYesClicked()
    }

    fun onNoButtonClicked() {
        navigator!!.onNoClicked()
    }
}