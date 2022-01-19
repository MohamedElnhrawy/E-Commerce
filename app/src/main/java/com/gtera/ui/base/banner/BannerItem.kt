package com.gtera.ui.base.banner

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel

class BannerItem(imgUrl: String, resourceProvider: ResourceProvider) : BaseItemViewModel() {

    var imgUrl = ObservableField(imgUrl)
    var resourceProviderObservable = ObservableField(resourceProvider)
    override val layoutId: Int
        get() = R.layout.large_banner_item

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }
}
