package com.gtera.ui.choosebudget

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.choosebudget.viewmodels.BudgetItemListViewModel
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.EXTRAS_KEY_BUDGET
import com.gtera.utils.APPConstants.EXTRAS_KEY_BUDGET_BRAND_ID
import javax.inject.Inject

class ChooseYourBudgetViewModel @Inject constructor() : BaseViewModel<ChooseYourBudgetNavigator>() {

    init {
        initBudgetsView()
    }


    lateinit var budgetsDataSourceFactory: BudgetsDataSourceFactory
    var budgetsAdapter: BasePagedListAdapter<BudgetItemListViewModel>? = null

    // adapter's lists
    lateinit var budgetsList: LiveData<PagedList<BudgetItemListViewModel>>
    private var list: PagedList<BudgetItemListViewModel>? = null
    var isRefreshing = ObservableBoolean(false)

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null
    var budgetOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL


    override fun onViewCreated() {

        super.onViewCreated()

        getBudgets()


    }

    protected fun initBudgetsView() {

        budgetsAdapter = BasePagedListAdapter(object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {

                if (id == R.id.iv_budgets_arrow) {

                    budgetsList.value?.get(position)?.isexpanded?.get()?.not()

                } else {


                    val extras = Bundle()
                    budgetsList.value?.get(position)?.budget?.let {
                        extras.putSerializable(
                            EXTRAS_KEY_BUDGET,
                            it
                        )
                    }

                    budgetsList.value?.get(position)?.selectedBrands.let {
                        if (it?.size!! > 0)
                        extras.putString(
                            EXTRAS_KEY_BUDGET_BRAND_ID,
                            it.joinToString(separator = ",")
                        )
                    }

                    openView(AppScreenRoute.CHOOSE_YOUR_BUDGET_CAR_LIST, extras)

                }

            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            budgetsDataSourceFactory.budgetsLiveDataSource.value?.invalidate()
        }

    }


    private fun getBudgets() {


        budgetsDataSourceFactory = BudgetsDataSourceFactory(
           this
        )
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .setPageSize(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
            .build()
        budgetsList = LivePagedListBuilder(
            budgetsDataSourceFactory,
            config
        )
            .build() as LiveData<PagedList<BudgetItemListViewModel>>
        budgetsList.observe(lifecycleOwner!!, Observer {
            list?.clear()
            list?.addAll(it)
            budgetsAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }
            isRefreshing.set(false)


        })

    }




}