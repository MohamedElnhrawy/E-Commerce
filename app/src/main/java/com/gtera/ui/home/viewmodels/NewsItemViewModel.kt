package com.gtera.ui.home.viewmodels

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.New
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.ListOrientation
import com.gtera.utils.Utilities
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NewsItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() =
            when {
                Objects.equals(orientation, ListOrientation.ORIENTATION_HOME) -> R.layout.news_item_home_layout
                Objects.equals(orientation, ListOrientation.ORIENTATION_HORIZONTAL) -> R.layout.news_item_horizontal_layout
                else -> R.layout.news_item_vertical_layout
            }


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var orientation: ListOrientation? = null

    private var resourceProviderObservable: ObservableField<ResourceProvider>? = null

    var news: New? = null

    var imageUrl = ObservableField("")
    var newsTitle = ObservableField("")
    var newsDate = ObservableField("")


    constructor(
        news: New?,
        resourceProvider: ResourceProvider,
        orientation: ListOrientation
    ) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        this.orientation = orientation
        setupNews(news)
    }


    private fun setupNews(news: New?) {
        this.news = news
        if (!Utilities.isNullString(news?.image?.get(0))) imageUrl.set(
            news?.image?.get(0)
        )
        newsTitle.set(news?.title)
        newsDate.set(Utilities.formatDateTime(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(news?.date)))
    }

}