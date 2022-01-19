package com.gtera.ui.base.toolbar

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import androidx.annotation.DrawableRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.common.ClickListener
import io.michaelrocks.paranoid.Obfuscate
import javax.inject.Inject

@Obfuscate
class ToolbarViewModel @Inject constructor() :
    BaseViewModel<ToolbarNavigator?>(), View.OnClickListener {
    var hideToolbar = ObservableBoolean(false)
    var hasElevation = ObservableBoolean(true)
    var hasBackObservable = ObservableBoolean(true)
    var hasAction = ObservableBoolean(false)
    var hasSecondaryActionIcon = ObservableBoolean(false)
    var hasFilterAction = ObservableBoolean(false)
    var hasTitle = ObservableBoolean(false)
    var pageTitle = ObservableField("")
    var isSecondaryActionEnabled = ObservableBoolean(false)
    var isFilterActionEnabled = ObservableBoolean(false)
    var actionText = ObservableField("")
    var actionTextColor = ObservableInt()

    var secondaryActionIcon = ObservableField<Drawable>()
    var filterActionIcon = ObservableField<Drawable>()
    var actionListener: View.OnClickListener = this
     var secondaryIconListener: ClickListener? = null
     var filterIconListener: ClickListener? = null
    var searchListener: SearchActionListener? = null
    var searchTxt: ObservableField<String> = ObservableField("")
    override fun onViewCreated() {
        super.onViewCreated()
        val title = toolbarTitle!!
        hasElevation.set(toolbarElevation!!)
        setupViewToolbar(title, hasSecondaryActionIcon.get(), hasFilterAction.get(), hasBackObservable.get())
    }

    override fun setupViewToolbar(
        title: String?,
        hasSecondaryAction: Boolean,
        hasFilterAction: Boolean,
        hasBack: Boolean
    ) {
        if (TextUtils.isEmpty(pageTitle.get())) pageTitle.set(title) else if (title != null && title != pageTitle.get()) pageTitle.set(
            title
        )
        hasTitle.set(!TextUtils.isEmpty(pageTitle.get()))
        this.hasBackObservable.set(hasBack)
        this.hasSecondaryActionIcon.set(hasSecondaryAction)
        this.hasFilterAction.set(hasFilterAction)
    }

    override fun setToolbarSecondaryActionField(
        @DrawableRes icon: Int,
        listener: ClickListener?
    ) {
        secondaryActionIcon.set(getDrawableResources(icon));
        secondaryIconListener = listener
    }

    override fun setToolbarFilterActionField(
        @DrawableRes icon: Int,
        listener: ClickListener?,
        isFilterEnabled :Boolean
    ) {
        filterActionIcon.set(getDrawableResources(icon));
        filterIconListener = listener
        this.isFilterActionEnabled.set(isFilterEnabled)
    }


    override fun setToolbarSearchListener(
        listener: SearchActionListener?
    ) {

        searchListener = listener
    }



    fun onBackButtonClicked() {
        goBack()
    }



    override fun onClick(v: View) {
        if (v.getId() == R.id.iv_secondary_action && secondaryIconListener != null)
            secondaryIconListener?.onViewClicked();
        else if (v.getId() == R.id.iv_filter_action && filterIconListener != null)
            filterIconListener?.onViewClicked();
    }

    override fun setToolbarSecondaryActionEnabled(isEnabled: Boolean) {
        isSecondaryActionEnabled.set(isEnabled)
    }

    override fun setToolbarFilterActionEnabled(isEnabled: Boolean) {
        isFilterActionEnabled.set(isEnabled)
    }

    override fun setToolbarSearchText(searchTxt: String) {
        this.searchTxt.set(searchTxt)
    }

}