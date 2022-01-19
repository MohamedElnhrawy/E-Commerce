package com.gtera.ui.offers

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Offer
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.home.viewmodels.CartItemViewModel

class OffersDataSourceFactory(
    private val viewModel: BaseViewModel<*>,
    private val orientation: ListOrientation
) : DataSource.Factory<Int, CartItemViewModel>() {

    val brandsLiveDataSource = MutableLiveData<OffersDataSource>()
    override fun create(): DataSource<Int, CartItemViewModel> {
        var brandsDataSource: OffersDataSource?
        brandsDataSource = OffersDataSource(viewModel,orientation)

        brandsLiveDataSource.postValue(brandsDataSource)
        return brandsDataSource
    }

}


class OffersDataSource(
    private val viewModel: BaseViewModel<*>,
    private val orientation: ListOrientation
) : PageKeyedDataSource<Int, CartItemViewModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CartItemViewModel>
    ) {

        viewModel.showLoading(false)
        viewModel.appRepository.getOffers(
            params.requestedLoadSize,
            1,
            null,
            null,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Offer?>?>?> {

                override fun onSuccess(result: BaseResponse<ArrayList<Offer?>?>?) {


                    val newList = mutableListOf<CartItemViewModel>()
                    result?.data?.forEach {
                        newList += CartItemViewModel(it, viewModel.resourceProvider,
                            orientation)
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
        callback: LoadCallback<Int, CartItemViewModel>
    ) {

        viewModel.appRepository.getOffers(
            params.requestedLoadSize,
            params.key,
            null,
            null,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Offer?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Offer?>?>?) {


                    val newList = mutableListOf<CartItemViewModel>()
                    result?.data?.forEach {
                        newList += CartItemViewModel(it, viewModel.resourceProvider,
                            orientation)
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
        callback: LoadCallback<Int, CartItemViewModel>
    ) {

    }

}