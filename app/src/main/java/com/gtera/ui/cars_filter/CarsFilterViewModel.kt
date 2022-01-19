package com.gtera.ui.cars_filter

import android.app.Activity
import android.os.Bundle
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.FiltrationCriteria
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.FilterType
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.FILTER_OPTION_RESULT
import com.gtera.utils.Utilities
import javax.inject.Inject


class CarsFilterViewModel @Inject constructor() : BaseViewModel<CarsFilterNavigator>() {


    var filterMap: HashMap<String, String> = HashMap()
    var searchListener: SearchActionListener = object : SearchActionListener {
        override fun preformSearch(searchText: String?) {
            navigator!!.hideKeyboard()

        }
    }

    var isNewCarFilter: Boolean? = null

    lateinit var filtersAdapter: BaseAdapter<FilterCriteriaViewModel>
    lateinit var filterList: ArrayList<FilterCriteriaViewModel>


    init {
        initFilterView()
    }

    private fun initFilterView() {

        filterList = ArrayList()
        filtersAdapter = BaseAdapter(filterList, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {

            }
        })
    }

    override fun onViewCreated() {
        super.onViewCreated()

        isNewCarFilter = dataExtras?.getBoolean(APPConstants.EXTRAS_KEY_IS_NEW_CARS)
        getFilterOptions();

    }

    private fun getFilterOptions() {
        filterList.clear()
        showLoading(false)
        appRepository.getFiltrationCriteria(
            isNewCarFilter,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<List<FiltrationCriteria?>?>?> {
                override fun onSuccess(result: BaseResponse<List<FiltrationCriteria?>?>?) {
                    hideLoading()
                    val list: List<FiltrationCriteria?> = result?.data!!
                    if (!Utilities.isNullList(list))
                        for (option in list) {

                            filterList.add(FilterCriteriaViewModel(option, object : ViewHolderInterface {
                                override fun onViewClicked(position: Int, id: Int) {

                                }
                            }, context, resourceProvider))
                        }

                    filtersAdapter.notifyDataSetChanged()
                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getFilterOptions()
                            }
                        })
                }

            }
        )
    }



    fun applyFilter() {

        for (filterItem in filterList) {

            when (filterItem.filterCriteriaViewType) {

                FilterType.FILTER_TYPE_HORIZONTAL_COLOR,
                FilterType.FILTER_TYPE_HORIZONTAL,
                FilterType.FILTER_TYPE_ATTRIBUTES,
                FilterType.FILTER_TYPE_GRID -> {

                    if (filterItem.selectedListItems.size > 0)
                        filterMap.put(
                            filterItem.filterCriteria?.key!!,
                            filterItem.selectedListItems.joinToString(separator = ",")
                        )


                }
                FilterType.FILTER_TYPE_DROWN_DOWN -> {


                    if (filterItem.selectedListItems.size > 0)
                        filterMap.put(
                            filterItem.filterCriteria?.key!!,
                            filterItem.selectedListItems.get(0).toString()
                        )
                }

                FilterType.FILTER_TYPE_RANG -> {

                    if (!filterItem.rangMin.get()?.isEmpty()!! && !filterItem.rangMax.get()
                            ?.isEmpty()!!
                    )
                        filterMap.put(
                            filterItem.filterCriteria?.key!!,
                            filterItem.rangMin.get()!!
                        ) + "," + filterItem.rangMax.get()

                }

                FilterType.FILTER_TYPE_MIN_MAX -> {

                    if (!filterItem.minMaxStart.get()?.isEmpty()!! && !filterItem.minMaxEnd.get()
                            ?.isEmpty()!!
                    )
                        filterMap.put(
                            filterItem.filterCriteria?.key!!,
                            filterItem.minMaxStart.get() + "," + filterItem.minMaxEnd.get()
                        )
                }
            }

        }
        setResult()
    }

    fun resetFilter() {

        getFilterOptions()
    }


    protected fun setResult() {

        val extras = Bundle()
        extras.putSerializable(FILTER_OPTION_RESULT, filterMap)
        setActivityResult(Activity.RESULT_OK, extras)
    }
}