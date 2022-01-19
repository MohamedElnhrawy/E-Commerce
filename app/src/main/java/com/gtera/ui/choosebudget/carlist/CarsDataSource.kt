package com.gtera.ui.choosebudget.carlist

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
import com.gtera.ui.base.CarType
import com.gtera.ui.home.viewmodels.CarItemViewModel

class CarsDataSourceFactory(
    private val viewModel: BaseViewModel<*>,
    private val brandsId: String?,
    private val searchName: String?,
    private val budgetId: Int?,
    private val sortBy: String?,
    private val desc: Int?,
    private val isNew: Boolean,
    private val carType: CarType,
    private val repository: AppRepository,
    private val resourceProvider: ResourceProvider,
    private val lifecycleOwner: LifecycleOwner?
) : DataSource.Factory<Int, CarItemViewModel>() {

    val carsLiveDataSource = MutableLiveData<CarsDataSource>()
    override fun create(): DataSource<Int, CarItemViewModel> {
        val BudgetsDataSource =
            CarsDataSource(
                viewModel,
                brandsId,
                searchName,
                budgetId,
                sortBy,
                desc,
                isNew,
                carType,
                repository,
                lifecycleOwner,
                resourceProvider
            )
        carsLiveDataSource.postValue(BudgetsDataSource)
        return BudgetsDataSource
    }

}


class CarsDataSource(
    private val viewModel: BaseViewModel<*>,
    private val brandsId: String?,
    private val searchName: String?,
    private val budgetId: Int?,
    private val sortBy: String?,
    private val desc: Int?,
    private val isNew: Boolean,
    private val carType: CarType,
    private val repository: AppRepository,
    private val lifecycleOwner: LifecycleOwner?,
    private val resourceProvider: ResourceProvider
) : PageKeyedDataSource<Int, CarItemViewModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CarItemViewModel>
    ) {


        repository.getCars(
            1,
            params.requestedLoadSize,
            if (isNew) 0 else 1,
            if (isNew) 1 else 0,
            null,
            brandsId,
            searchName,
            budgetId,
            sortBy,
            desc,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {


                    val newList = mutableListOf<CarItemViewModel>()
                    result?.data?.forEach {
                        newList += CarItemViewModel(it!!, resourceProvider, carType)
                    }
                    callback.onResult(newList, null, result?.meta?.currentPage?.toInt()?.plus(1))
                    viewModel.hideLoading()
                    viewModel.hasData(result?.data?.size!!)

                }

                override fun onError(throwable: ErrorDetails?) {
                    viewModel.hideLoading()
                    viewModel.hasData(0)
                    viewModel.showErrorBanner(throwable?.errorMsg)
                }


            })


    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CarItemViewModel>
    ) {

        repository.getCars(
            params.key,
            params.requestedLoadSize,
            if (isNew) 0 else 1,
            if (isNew) 1 else 0,
            null,
            brandsId,
            searchName,
            budgetId,
            sortBy,
            desc,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {


                    val newList = mutableListOf<CarItemViewModel>()
                    result?.data?.forEach {
                        newList += CarItemViewModel(it!!, resourceProvider, carType)
                    }
                    callback.onResult(newList, params.key + 1)
                    viewModel.hideLoading()

                }

                override fun onError(throwable: ErrorDetails?) {
                    viewModel.showErrorBanner(throwable?.errorMsg)
                }


            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CarItemViewModel>
    ) {

    }

}