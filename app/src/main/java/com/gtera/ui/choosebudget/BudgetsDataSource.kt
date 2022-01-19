package com.gtera.ui.choosebudget

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Budget
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.choosebudget.viewmodels.BudgetItemListViewModel

class BudgetsDataSourceFactory(
    private val viewModel: ChooseYourBudgetViewModel
) : DataSource.Factory<Int, BudgetItemListViewModel>() {

    val budgetsLiveDataSource = MutableLiveData<BudgetsDataSource>()
    override fun create(): DataSource<Int, BudgetItemListViewModel> {
        val BudgetsDataSource =
            BudgetsDataSource(viewModel)
        budgetsLiveDataSource.postValue(BudgetsDataSource)
        return BudgetsDataSource
    }

}


class BudgetsDataSource(
    private val viewModel: ChooseYourBudgetViewModel
) : PageKeyedDataSource<Int, BudgetItemListViewModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, BudgetItemListViewModel>
    ) {


        viewModel.showLoading(false)
        viewModel.appRepository.getBudgets(
            1,
            params.requestedLoadSize,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Budget?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Budget?>?>?) {


                    val newList = mutableListOf<BudgetItemListViewModel>()
                    result?.data?.forEach {
                        newList += BudgetItemListViewModel(it!!, viewModel.resourceProvider)
                    }
                    callback.onResult(newList, null, result?.meta?.currentPage?.toInt()?.plus(1))
                    viewModel.hideLoading()
                }

                override fun onError(throwable: ErrorDetails?) {
                    viewModel.hideLoading()
                }


            })


    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, BudgetItemListViewModel>
    ) {

        viewModel.showLoading(false)
        viewModel.appRepository.getBudgets(
            params.key,
            params.requestedLoadSize,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Budget?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Budget?>?>?) {


                    val newList = mutableListOf<BudgetItemListViewModel>()
                    result?.data?.forEach {
                        newList += BudgetItemListViewModel(it!!, viewModel.resourceProvider)
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
        callback: LoadCallback<Int, BudgetItemListViewModel>
    ) {

    }

}