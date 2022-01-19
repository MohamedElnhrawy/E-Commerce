package com.gtera.ui.helper

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.common.ClickListener
import javax.inject.Inject

class EmptyView {
    @Inject
    lateinit var resourceProvider: ResourceProvider
    var viewImg: Drawable? = null
        private set
    var viewTitle: String? = null
        private set
    var viewDesc: String? = null
        private set
    var btnTxt = ""
        private set
    private var listener: ClickListener? = null

    constructor() {}
    constructor(viewTitle: String?) {
        this.viewTitle = viewTitle
        viewDesc = ""
    }

    constructor(viewTitle: String?, viewDesc: String?) {
        this.viewTitle = viewTitle
        this.viewDesc = viewDesc
    }

    constructor(@DrawableRes viewImg: Int, @StringRes viewTitle: Int) {
        this.viewImg = resourceProvider!!.getDrawable(viewImg)
        this.viewTitle = resourceProvider!!.getString(viewTitle)
    }

    constructor(@DrawableRes viewImg: Int, @StringRes viewTitle: Int, @StringRes viewDesc: Int, resourceProvider: ResourceProvider) {
        this.viewImg = resourceProvider!!.getDrawable(viewImg)
        this.viewTitle = resourceProvider!!.getString(viewTitle)
        this.viewDesc = resourceProvider!!.getString(viewDesc)
        this.resourceProvider = resourceProvider
    }

    constructor(
        @DrawableRes viewImg: Int, @StringRes viewTitle: Int, @StringRes viewDesc: Int,
        @StringRes viewBtnTxt: Int, listener: ClickListener?
    ) {
        this.viewImg = resourceProvider!!.getDrawable(viewImg)
        this.viewTitle = resourceProvider!!.getString(viewTitle)
        this.viewDesc = resourceProvider!!.getString(viewDesc)
        btnTxt = resourceProvider!!.getString(viewBtnTxt)
        this.listener = listener
    }

    constructor(
        @DrawableRes viewImg: Int, viewTitle: String?,
        @StringRes viewBtnTxt: Int, listener: ClickListener?, resourceProvider: ResourceProvider
    ) {
        this.viewImg = resourceProvider!!.getDrawable(viewImg)
        this.viewTitle = viewTitle
        btnTxt = resourceProvider!!.getString(viewBtnTxt)
        this.listener = listener
    }

    constructor(
        @DrawableRes viewImg: Int, viewTitle: String?, viewDesc: String?,
        @StringRes viewBtnTxt: Int, listener: ClickListener?, resourceProvider: ResourceProvider
    ) {
        this.viewImg = resourceProvider!!.getDrawable(viewImg)
        this.viewTitle = viewTitle
        this.viewDesc = viewDesc
        btnTxt = resourceProvider!!.getString(viewBtnTxt)
        this.listener = listener
    }

    constructor(@DrawableRes viewImg: Int, viewTitle: String?, viewDesc: String?, resourceProvider:ResourceProvider) {
        this.viewImg = resourceProvider!!.getDrawable(viewImg)
        this.viewTitle = viewTitle
        this.viewDesc = viewDesc
    }

    constructor(viewImg: Drawable?, viewTitle: String?, viewDesc: String?) {
        this.viewImg = viewImg
        this.viewTitle = viewTitle
        this.viewDesc = viewDesc
    }

//    constructor(
//        drawable: Int,
//        errorMessage: String?,
//        tryAgain: Int,
//        function: Function0<Unit>
//    ) {
//    }

    fun onBtnClick() {
        if (listener != null) listener!!.onViewClicked()
    }
}