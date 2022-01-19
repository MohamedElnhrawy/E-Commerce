package com.gtera.ui.brands

import android.os.Bundle
import android.os.Handler
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.base.BaseViewModel
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.BrandItemViewModel
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.CONSTANT_PAGING_PER_PAGE_NUM
import javax.inject.Inject


class BrandsViewModel @Inject constructor() : BaseViewModel<BrandsNavigator>() {
    var prevSelected: Int = 0

    init {
        initBrandsView()
    }

    var searchListener: SearchActionListener = object : SearchActionListener {
        override fun preformSearch(searchText: String?) {
//            brandsAdapter?.filter?.filter(searchText)
            navigator!!.hideKeyboard()
            if (brandsDataSourceFactory.brandsLiveDataSource.value == null)
                initDataSource()
            else
                brandsDataSourceFactory.brandsLiveDataSource.value!!.invalidate()
        }
    }
    var searchTxt: ObservableField<String> = ObservableField("")

    lateinit var brandsDataSourceFactory: BrandsDataSourceFactory
    var isRefreshing = ObservableBoolean(false)

    var isNewCar: Boolean? = null

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var brandsOrientation: ListOrientation? = ListOrientation.ORIENTATION_GRID

    var brandsAdapter: BasePagedListAdapter<BrandItemViewModel>? = null

    var spanCount: Int = 3

    // adapter's lists
    lateinit var brandsList: LiveData<PagedList<BrandItemViewModel>>
    private var list: PagedList<BrandItemViewModel>? = null


    override fun onViewCreated() {
        super.onViewCreated()
        isNewCar = dataExtras?.getBoolean(APPConstants.EXTRAS_KEY_CAR_CATEGORY_SELECTION)



        brandsDataSourceFactory = BrandsDataSourceFactory(
            this,
            searchTxt,false
        )

        initDataSource()

    }


    protected fun initBrandsView() {
        brandsAdapter = BasePagedListAdapter(object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {

                brandsList.value!![prevSelected]?.isSelected?.set(false)
                brandsList.value!![position]?.isSelected?.set(true)
                prevSelected = position

                var handler = Handler()
                handler.postDelayed(Runnable {

                    val extras = Bundle()
                    extras.putInt(
                        APPConstants.EXTRAS_KEY_BRAND_ID,
                        brandsList.value!![position]?.brandId?.get()!!
                    )
                    extras.putSerializable(
                        APPConstants.EXTRAS_KEY_BRAND,
                        brandsList.value!![position]?.brand!!
                    )

                    extras.putBoolean(
                        APPConstants.EXTRAS_KEY_CAR_CATEGORY_SELECTION, isNewCar!!
                    )

                    openView(AppScreenRoute.BRAND_MODELS, extras)

                }, 500)


            }
        })


        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            isLoading.set(true)
            brandsDataSourceFactory.brandsLiveDataSource.value?.invalidate()
        }
    }


    private fun getBrands(): LiveData<PagedList<BrandItemViewModel>> {

        brandsDataSourceFactory = BrandsDataSourceFactory(
            this,
            searchTxt,
            false
        )
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(CONSTANT_PAGING_PER_PAGE_NUM)
            .setPageSize(CONSTANT_PAGING_PER_PAGE_NUM)
            .build()
        brandsList = LivePagedListBuilder(
            brandsDataSourceFactory,
            config
        ).build() as LiveData<PagedList<BrandItemViewModel>>
        return brandsList

    }

    private fun initDataSource() {

        brandsDataSourceFactory = BrandsDataSourceFactory(
            this,
            searchTxt,
            false
        )
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(CONSTANT_PAGING_PER_PAGE_NUM)
            .setPageSize(CONSTANT_PAGING_PER_PAGE_NUM)
            .build()

        brandsList = LivePagedListBuilder(
            brandsDataSourceFactory,
            config
        ).build() as LiveData<PagedList<BrandItemViewModel>>

        brandsList.observe(lifecycleOwner!!, Observer {
            list?.clear()
            list?.addAll(it)
            brandsAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }
            isRefreshing.set(false)
        })

    }

}