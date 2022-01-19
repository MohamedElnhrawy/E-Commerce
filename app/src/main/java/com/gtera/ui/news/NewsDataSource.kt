package com.gtera.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.New
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.home.viewmodels.NewsItemViewModel


class NewsDataSourceFactory(
    private val viewModel: BaseViewModel<*>,
    private val orientation: ListOrientation
) : DataSource.Factory<Int, NewsItemViewModel>() {

    val brandsLiveDataSource = MutableLiveData<NewsDataSource>()
    override fun create(): DataSource<Int, NewsItemViewModel> {
        var brandsDataSource: NewsDataSource?
        brandsDataSource = NewsDataSource(viewModel,orientation)

        brandsLiveDataSource.postValue(brandsDataSource)
        return brandsDataSource
    }

}


class NewsDataSource(
    private val viewModel: BaseViewModel<*>,
    private val orientation: ListOrientation
) : PageKeyedDataSource<Int, NewsItemViewModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NewsItemViewModel>
    ) {

        viewModel.showLoading(false)
        viewModel.appRepository.getNews(
            params.requestedLoadSize,
            1,
            null,
            null,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<New?>?>?> {

                override fun onSuccess(result: BaseResponse<ArrayList<New?>?>?) {


                    val newList = mutableListOf<NewsItemViewModel>()
                    result?.data?.forEach {
                        newList += NewsItemViewModel(it, viewModel.resourceProvider,
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
        callback: LoadCallback<Int, NewsItemViewModel>
    ) {

        viewModel.appRepository.getNews(
            params.requestedLoadSize,
            params.key,
            null,
            null,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<New?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<New?>?>?) {


                    val newList = mutableListOf<NewsItemViewModel>()
                    result?.data?.forEach {
                        newList += NewsItemViewModel(it, viewModel.resourceProvider,
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
        callback: LoadCallback<Int, NewsItemViewModel>
    ) {

    }

}