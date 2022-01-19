package com.gtera.ui.home.viewmodels

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.data.model.Model
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.utils.Utilities
import javax.inject.Inject

class ModelItemViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = R.layout.model_item_list_layout

    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var resourceProviderObservable: ObservableField<ResourceProvider>? = null

    var model: Model? = null

    var imageUrl = ObservableField("")
    var modelName = ObservableField("")
    var modelId = ObservableField(0)


    constructor(carModel: Model?, resourceProvider: ResourceProvider) : this() {
        resourceProviderObservable = ObservableField(resourceProvider)
        setupModel(carModel)
    }


    fun setupModel(carModel: Model?) {
        this.model = carModel
        if (!Utilities.isNullString(carModel?.image))
            imageUrl.set(carModel?.image)
        modelName.set(carModel?.name)
        modelId.set(model?.id)


    }

}