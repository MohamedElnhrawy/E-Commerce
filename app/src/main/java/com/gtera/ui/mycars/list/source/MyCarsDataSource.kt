package com.gtera.ui.mycars.list.source

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Car
import com.gtera.data.model.response.BaseResponse
import com.gtera.data.repository.AppRepository
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.mycars.list.MyCarListItemViewModel

class MyCarsDataSourceFactory(
    private val viewModel: BaseViewModel<*>,
    private val repository: AppRepository,
    private val resourceProvider: ResourceProvider,
    private val lifecycleOwner: LifecycleOwner?
) : DataSource.Factory<Int, MyCarListItemViewModel>() {

    val carsLiveDataSource = MutableLiveData<MyCarsDataSource>()
    override fun create(): DataSource<Int, MyCarListItemViewModel> {
        val myCarsDataSource =
            MyCarsDataSource(
                viewModel,
                repository,
                lifecycleOwner,
                resourceProvider
            )
        carsLiveDataSource.postValue(myCarsDataSource)
        return myCarsDataSource
    }

}


class MyCarsDataSource(
    private val viewModel: BaseViewModel<*>,
    private val repository: AppRepository,
    private val lifecycleOwner: LifecycleOwner?,
    private val resourceProvider: ResourceProvider
) : PageKeyedDataSource<Int, MyCarListItemViewModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MyCarListItemViewModel>
    ) {

        viewModel.showLoading(false)
        repository.getMyCars(
            1,
            params.requestedLoadSize,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {


                    val newList = mutableListOf<MyCarListItemViewModel>()
                    result?.data?.forEach {
                        newList += MyCarListItemViewModel(it!!, resourceProvider)
                    }
                    callback.onResult(newList, null, result?.meta?.currentPage?.toInt()?.plus(1))
                    viewModel.hideLoading()
                    viewModel.isLoading.set(false)
                    viewModel.hasData(result?.data?.size!!)

                }

                override fun onError(throwable: ErrorDetails?) {
                    viewModel.hideLoading()
                    viewModel.hasData(0)
                    viewModel.isLoading.set(false)
                    viewModel.showErrorBanner(throwable?.errorMsg)

                }


            })


    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MyCarListItemViewModel>
    ) {

        repository.getMyCars(
            params.key,
            params.requestedLoadSize,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {


                    val newList = mutableListOf<MyCarListItemViewModel>()
                    result?.data?.forEach {
                        newList += MyCarListItemViewModel(it!!, resourceProvider)
                    }
                    callback.onResult(newList, params.key + 1)
                    viewModel.hideLoading()
                    viewModel.isLoading.set(false)

                }

                override fun onError(throwable: ErrorDetails?) {
                    viewModel.hideLoading()
                    viewModel.isLoading.set(false)
                    viewModel.showErrorBanner(throwable?.errorMsg)
                }


            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MyCarListItemViewModel>
    ) {

    }

}