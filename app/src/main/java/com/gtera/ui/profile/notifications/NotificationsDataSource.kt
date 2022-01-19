package com.gtera.ui.profile.notifications

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Notification
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.base.ListOrientation


class NotificationsDataSourceFactory(
    private val viewModel: BaseViewModel<*>,
    private val orientation: ListOrientation
) : DataSource.Factory<Int, NotificationListItemViewModel>() {

    val brandsLiveDataSource = MutableLiveData<NotificationsDataSource>()
    override fun create(): DataSource<Int, NotificationListItemViewModel> {
        var brandsDataSource: NotificationsDataSource?
        brandsDataSource = NotificationsDataSource(viewModel,orientation)

        brandsLiveDataSource.postValue(brandsDataSource)
        return brandsDataSource
    }

}


class NotificationsDataSource(
    private val viewModel: BaseViewModel<*>,
    private val orientation: ListOrientation
) : PageKeyedDataSource<Int, NotificationListItemViewModel>() {


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NotificationListItemViewModel>
    ) {

        viewModel.showLoading(false)
        viewModel.appRepository.getMyNotifications(
            params.requestedLoadSize,
            1,
            null,
            null,
            null,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Notification?>?>?> {

                override fun onSuccess(result: BaseResponse<ArrayList<Notification?>?>?) {


                    val newList = mutableListOf<NotificationListItemViewModel>()
                    result?.data?.forEach {
                        newList += NotificationListItemViewModel(it)
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
        callback: LoadCallback<Int, NotificationListItemViewModel>
    ) {

        viewModel.appRepository.getMyNotifications(
            params.requestedLoadSize,
            params.key,
            null,
            null,
            null,
            viewModel.lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Notification?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Notification?>?>?) {


                    val newList = mutableListOf<NotificationListItemViewModel>()
                    result?.data?.forEach {
                        newList += NotificationListItemViewModel(it)
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
        callback: LoadCallback<Int, NotificationListItemViewModel>
    ) {

    }

}