package com.gtera.ui.profile.faq

import android.view.View
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.FAQ
import com.gtera.ui.base.BaseItemViewModel
import javax.inject.Inject

class FAQItemViewModel @Inject constructor() : BaseItemViewModel() {

    var FAQQuestition = ObservableField("")
    var FAQAnswer = ObservableField("")
    var isexpanded = ObservableField(false)

    var expandCollapseClick =
        View.OnClickListener { v: View? ->
            isexpanded.set(isexpanded.get()?.not())
        }

    constructor(
        faq: FAQ?
    ) : this() {

        this.FAQQuestition.set(faq?.question)
        this.FAQAnswer.set(faq?.answer)
    }

    override val layoutId: Int
        get() = R.layout.faq_list_item_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
}