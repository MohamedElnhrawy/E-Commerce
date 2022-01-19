package com.gtera.ui.ordernow.list.datasource

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Order
import com.gtera.data.model.response.BaseResponse
import com.gtera.data.repository.AppRepository
import com.gtera.di.providers.ResourceProvider
import com.gtera.ui.ordernow.list.OrderListItemViewModel

class MyOrdersDataSourceFactory(
    private val viewModel: BaseViewModel<*>,
    private val repository: AppRepository,
    private val resourceProvider: ResourceProvider,
    private val lifecycleOwner: LifecycleOwner?
) : DataSource.Factory<Int, OrderListItemViewModel>() {

    val carsLiveDataSource = MutableLiveData<MyOrdersDataSource>()
    override fun create(): DataSource<Int, OrderListItemViewModel> {
        val myCarsDataSource =
            MyOrdersDataSource(
                viewModel,
                repository,
                lifecycleOwner,
                resourceProvider
            )
        carsLiveDataSource.postValue(myCarsDataSource)
        return myCarsDataSource
    }

}


class MyOrdersDataSource(
    private val viewModel: BaseViewModel<*>,
    private val repository: AppRepository,
    private val lifecycleOwner: LifecycleOwner?,
    private val resourceProvider: ResourceProvider
) : PageKeyedDataSource<Int, OrderListItemViewModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, OrderListItemViewModel>
    ) {

        viewModel.showLoading(false)
        repository.getUserOrders(
            params.requestedLoadSize,
            1,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Order?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Order?>?>?) {


                    val newList = mutableListOf<OrderListItemViewModel>()
                    result?.data?.forEach {
                        newList += OrderListItemViewModel(it!!, resourceProvider)
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
        callback: LoadCallback<Int, OrderListItemViewModel>
    ) {

        repository.getUserOrders(
            params.requestedLoadSize,
            params.key,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Order?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Order?>?>?) {


                    val newList = mutableListOf<OrderListItemViewModel>()
                    result?.data?.forEach {
                        newList += OrderListItemViewModel(it!!, resourceProvider)
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
        callback: LoadCallback<Int, OrderListItemViewModel>
    ) {

    }

}