package com.gtera.ui.profile.notifications

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.base.BaseViewModel
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.utils.APPConstants
import javax.inject.Inject

class NotificationListViewModel @Inject constructor() : BaseViewModel<NotificationListNavigator>() {

    init {
        initNotificationsView()
    }

    lateinit var notificationsDataSourceFactory: NotificationsDataSourceFactory

    var notificationsAdapter: BasePagedListAdapter<NotificationListItemViewModel>? = null


    // adapter's lists
    lateinit var notificationsList: LiveData<PagedList<NotificationListItemViewModel>>
    private var list: PagedList<NotificationListItemViewModel>? = null



    var isRefreshing = ObservableBoolean(false)
    var isList = ObservableBoolean(true)

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var notificationsOrientation: ListOrientation = ListOrientation.ORIENTATION_VERTICAL


    override fun onViewCreated() {
        super.onViewCreated()

        initDataSource()

    }

    private fun initDataSource() {

        notificationsDataSourceFactory = NotificationsDataSourceFactory(
            this,
            notificationsOrientation
        )
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .setPageSize(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .build()

        notificationsList = LivePagedListBuilder(
            notificationsDataSourceFactory,
            config
        ).build() as LiveData<PagedList<NotificationListItemViewModel>>


        notificationsList.observe(lifecycleOwner!!, Observer {
            list?.clear()
            list?.addAll(it)
            notificationsAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }
            isRefreshing.set(false)
        })

    }

    protected fun initNotificationsView() {

        notificationsAdapter = BasePagedListAdapter(object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {


            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            isLoading.set(true)
            notificationsDataSourceFactory.brandsLiveDataSource.value?.invalidate()
        }
    }




}