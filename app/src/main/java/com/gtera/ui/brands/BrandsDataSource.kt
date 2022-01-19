package com.gtera.ui.brands

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Brand
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.home.viewmodels.BrandItemViewModel

class BrandsDataSourceFactory(
    private val viewModel: BaseViewModel<*>,
    var searchTxt: ObservableField<String>,
    var isHorizentalScrollableGrid:Boolean
) : DataSource.Factory<Int, BrandItemViewModel>() {

    val brandsLiveDataSource = MutableLiveData<BrandsDataSource>()
    override fun create(): DataSource<Int, BrandItemViewModel> {
        var brandsDataSource: BrandsDataSource?
        brandsDataSource = BrandsDataSource(viewModel, searchTxt,isHorizentalScrollableGrid)

        brandsLiveDataSource.postValue(brandsDataSource)
        return brandsDataSource
    }

}


class BrandsDataSource(
    private val viewModel: BaseViewModel<*>,
    var searchTxt: ObservableField<String>,
    var isHorizentalScrollableGrid:Boolean
) : PageKeyedDataSource<Int, BrandItemViewModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, BrandItemViewModel>
    ) {

        viewModel.showLoading(false)
        viewModel.appRepository.getBrands(
            searchTxt.get(),
            params.requestedLoadSize,
            null,
            null,
            1,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Brand?>?>?> {

                override fun onSuccess(result: BaseResponse<ArrayList<Brand?>?>?) {


                    val newList = mutableListOf<BrandItemViewModel>()
                    result?.data?.forEach {
                        newList += BrandItemViewModel(it!!, viewModel.context,isHorizentalScrollableGrid)
                    }
                    callback.onResult(newList, null, result?.meta?.currentPage?.plus(1))
                    viewModel.hasData( result?.data?.size!!)
                    viewModel.hideLoading()


                }

                override fun onError(throwable: ErrorDetails?) {

                    viewModel.showErrorBanner(throwable?.errorMsg)
                    viewModel.hasData(0)
                    viewModel.hideLoading()
                }


            })


    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, BrandItemViewModel>
    ) {

        viewModel.appRepository.getBrands(
            searchTxt.get(),
            params.requestedLoadSize,
            null,
            null,
            params.key,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Brand?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Brand?>?>?) {


                    val newList = mutableListOf<BrandItemViewModel>()
                    result?.data?.forEach {
                        newList += BrandItemViewModel(it!!, viewModel.context)
                    }
                    callback.onResult(newList, params.key + 1)
                    viewModel.hideLoading()


                }

                override fun onError(throwable: ErrorDetails?) {
                    viewModel.hideLoading()
                }


            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, BrandItemViewModel>
    ) {

    }

}