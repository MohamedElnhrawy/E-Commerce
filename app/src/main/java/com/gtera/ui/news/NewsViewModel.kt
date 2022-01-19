package com.gtera.ui.news

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.base.BaseViewModel
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.NewsItemViewModel
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import javax.inject.Inject

class NewsViewModel @Inject constructor() : BaseViewModel<NewsNavigator>() {

    init {
        initNews()
    }

    lateinit var newsDataSourceFactory: NewsDataSourceFactory

    var newsAdapter: BasePagedListAdapter<NewsItemViewModel>? = null


    // adapter's lists
    lateinit var newsList: LiveData<PagedList<NewsItemViewModel>>
    private var list: PagedList<NewsItemViewModel>? = null


    //Orientation
    var newsOrientation: ListOrientation = ListOrientation.ORIENTATION_VERTICAL

    var isRefreshing = ObservableBoolean(false)

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    override fun onViewCreated() {
        super.onViewCreated()
        initDataSource()

    }


    private fun initDataSource() {

        newsDataSourceFactory = NewsDataSourceFactory(
            this,
            newsOrientation
        )
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .setPageSize(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .build()

        newsList = LivePagedListBuilder(
            newsDataSourceFactory,
            config
        ).build() as LiveData<PagedList<NewsItemViewModel>>


        newsList.observe(lifecycleOwner!!, Observer {
            list?.clear()
            list?.addAll(it)
            newsAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }
            isRefreshing.set(false)
        })

    }

    private fun initNews() {
        newsAdapter = BasePagedListAdapter( object : ViewHolderInterface {

            override fun onViewClicked(position: Int, id: Int) {
                val extras = Bundle()
                extras.putSerializable(APPConstants.EXTRAS_KEY_NEWS, newsList.value!![position]?.news)
                openView(AppScreenRoute.NEWS_DETAILS, extras)
            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            isLoading.set(true)
            newsDataSourceFactory.brandsLiveDataSource.value?.invalidate()
        }


    }

}