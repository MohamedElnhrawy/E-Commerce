package com.gtera.ui.models

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Brand
import com.gtera.data.model.Model
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.ModelItemViewModel
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import javax.inject.Inject

class ModelsViewModel @Inject constructor() : BaseViewModel<ModelsNavigator>() {

    init {
        initModelsView()
    }

    var isRefreshing = ObservableBoolean(false)
    var brandName = ObservableField("  ")

    var brand: Brand? = null
    var isNewCar: Boolean? = null

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var modelsOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL

    var modelsAdapter: BaseAdapter<ModelItemViewModel>? = null

    // adapter's lists
    protected var modelsList: ArrayList<ModelItemViewModel> =
        ArrayList<ModelItemViewModel>()


    override fun onViewCreated() {
        super.onViewCreated()
        brand = dataExtras?.getSerializable(APPConstants.EXTRAS_KEY_BRAND) as Brand
        isNewCar = dataExtras?.getBoolean(APPConstants.EXTRAS_KEY_CAR_CATEGORY_SELECTION)
        brandName.set(brand?.name)
        getModels()

    }

    private fun getModels() {

        showLoading(false)
        appRepository.getModels(
            brand?.id,
            null,
            null,
            null,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Model?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Model?>?>?) {
                    hideLoading()
                    addModels(result?.data!!)
                    hasData(result?.data?.size!!)
                    isRefreshing.set(false)
                }

                override fun onError(throwable: ErrorDetails?) {
                    isRefreshing.set(false)
                    hasData(0)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getModels()
                            }
                        })
                }


            })
    }


    protected fun initModelsView() {
        modelsAdapter = BaseAdapter(modelsList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {


                if (id == R.id.iv_share_icon){

                        launchShareIntent(modelsList.get(position).modelName.get(),
                            modelsList[position].model?.shareLink)
                }else{
                val extras = Bundle()
                extras.putInt(
                    APPConstants.EXTRAS_KEY_MODEL_ID,
                    modelsList.get(position).modelId.get()!!
                )
                extras.putSerializable(
                    APPConstants.EXTRAS_KEY_MODEL,
                    modelsList.get(position).model!!
                )
                extras.putBoolean(
                    APPConstants.EXTRAS_KEY_CAR_CATEGORY_SELECTION, isNewCar!!
                )


                openView(AppScreenRoute.MODEL_CARS, extras)
            }}
        })


        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getModels()
        }
    }

    protected fun addModels(List: List<Model?>) {
        if (Utilities.isNullList(List)) return

        this.modelsList.clear()
        for (model in List) {
            val modelItemViewModel = ModelItemViewModel(
                model!!, resourceProvider
            )
            this.modelsList.add(modelItemViewModel)
        }
        modelsAdapter?.updateList(modelsList)
        modelsAdapter?.notifyDataSetChanged()
    }

}