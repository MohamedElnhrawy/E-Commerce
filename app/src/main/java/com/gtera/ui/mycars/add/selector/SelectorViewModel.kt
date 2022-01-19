package com.gtera.ui.mycars.add.selector

import android.os.Bundle
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Brand
import com.gtera.data.model.Model
import com.gtera.data.model.SelectorOption
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.SearchActionListener
import com.gtera.ui.base.SelectorType
import com.gtera.ui.brands.BrandsDataSourceFactory
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.BrandItemViewModel
import com.gtera.ui.mycars.add.AddMyCarActivity
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SelectorViewModel @Inject constructor() : BaseViewModel<SelectorNavigator>() {

    var prevSelected: Int = 0
    private var selectorType: SelectorType? = null
    var brand: Brand? = null
    var brandName: String? = null
    var model: SelectorOption? = null
    var modelName: String? = null
    var year: String? = null
    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)


    var selectorList: ArrayList<SelectorItemViewModel> =
        ArrayList<SelectorItemViewModel>()
    var selectorAdapter: BaseAdapter<SelectorItemViewModel>? =
        BaseAdapter<SelectorItemViewModel>(selectorList, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {

                selectorList[prevSelected].isSelected.set(false)
                selectorList[position].isSelected.set(true)
                prevSelected = position
            }
        })

    var isHorizontalGrid = ObservableField(true)
    var isSearchViable = ObservableField(true)
    var isSelector = ObservableField(false)
    var isOtherViable = ObservableField(true)
    var selectorSpanCount = ObservableField(3)
    var searchHint: ObservableField<String> = ObservableField("")
    var searchText: ObservableField<String> = ObservableField("")
    var searchTitle: ObservableField<String> = ObservableField("")
    var selectorOtherText: ObservableField<String> = ObservableField("")
    var selectorOtherError: ObservableField<String> = ObservableField("")
    var selectorOtherStatus: ObservableField<Boolean> = ObservableField(false)
    var otherHeaderTxt: ObservableField<String> = ObservableField("")

    var onBrandsClick: ViewHolderInterface = object : ViewHolderInterface {
        override fun onViewClicked(position: Int, id: Int) {


            brandsList?.value!![prevSelected]?.isSelected?.set(false)
            brandsList?.value!![position]?.isSelected?.set(true)
            prevSelected = position


        }
    }


    var brandsList: LiveData<PagedList<BrandItemViewModel>>? = null
    var brandsAdapter: BasePagedListAdapter<BrandItemViewModel>? =
        BasePagedListAdapter<BrandItemViewModel>(onBrandsClick)
    private var list: PagedList<BrandItemViewModel>? = null
    var searchListener: SearchActionListener = object : SearchActionListener {
        override fun preformSearch(searchText: String?) {

            navigator!!.hideKeyboard()
            if (brandsDataSourceFactory.brandsLiveDataSource.value == null)
                initDataSource()
            else
                brandsDataSourceFactory.brandsLiveDataSource.value!!.invalidate()
        }
    }

    lateinit var brandsDataSourceFactory: BrandsDataSourceFactory


    init {
        initView()
    }

    override fun onViewCreated() {

        selectorType = dataExtras?.getSerializable(APPConstants.SELECTOR_TYPE) as SelectorType?
        searchTitle.set(dataExtras?.getString(APPConstants.SELECTOR_HEADER_TEXT, ""))
        searchHint.set(dataExtras?.getString(APPConstants.SELECTOR_SEARCH_HINT, ""))
        otherHeaderTxt.set(dataExtras?.getString(APPConstants.SELECTOR_OTHER_TEXT, ""))
        brand = dataExtras?.getSerializable(APPConstants.SELECTOR_BRAND) as Brand?
        brandName = dataExtras?.getString(APPConstants.SELECTOR_BRAND_BRAND_NAME)
        model = dataExtras?.getSerializable(APPConstants.SELECTOR_MODEL) as SelectorOption?
        modelName = dataExtras?.getString(APPConstants.SELECTOR_MODEL_NAME)

        super.onViewCreated()

        initDataSource()

    }

    override fun onViewRecreated() {
        super.onViewRecreated()

        brandsDataSourceFactory = BrandsDataSourceFactory(
            this,
            searchText, false
        )

        initDataSource()
    }


    private fun initDataSource() {

        when (selectorType) {

            SelectorType.BRAND -> {

                isSelector.set(false)
                brandsDataSourceFactory = BrandsDataSourceFactory(
                    this,
                    searchText,
                    true
                )
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(true)
                    .setInitialLoadSizeHint(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
                    .setPageSize(APPConstants.CONSTANT_PAGING_PER_PAGE_NUM)
                    .build()

                brandsList = LivePagedListBuilder(
                    brandsDataSourceFactory,
                    config
                ).build() as LiveData<PagedList<BrandItemViewModel>>

                brandsList?.observe(lifecycleOwner!!, Observer {
                    list?.clear()
                    list?.addAll(it)
                    brandsAdapter?.submitList(it)
                    list?.size?.let { it1 -> hasData(it1) }

                    if(list!=null)
                    when(list?.size!!){
                        0,1,2,3 ->  selectorSpanCount.set(1)
                        4,5,6->  selectorSpanCount.set(2)
                        else -> selectorSpanCount.set(3)

                    }
                })
            }

            SelectorType.MODEL -> {
                isSelector.set(true)
                selectorSpanCount.set(3)
                getModels()

            }
            SelectorType.YEAR -> {
                isSearchViable.set(false)
                isSelector.set(true)
                selectorSpanCount.set(5)
                getYears()

            }

        }


    }

    fun onBtnContinue() {

        selectorOtherError.set("")
        selectorOtherStatus.set(false)

        when (selectorType) {

            SelectorType.BRAND -> {


                if (brandsList?.value!![prevSelected]?.isSelected?.get()!!) {

                    onContinueBrandSelector(
                        brandsList?.value!![prevSelected]?.brand,
                        null
                    )
                } else {
                    showErrorBanner(resourceProvider.getString(R.string.str_selector_brand_validation))
                }

            }

            SelectorType.MODEL -> {

                if (selectorList[prevSelected].isSelected.get()) {

                    onContinueModelSelector(selectorList[prevSelected].let {
                        it.selectorOption.let { it } ?: null
                    }, null)

                } else {
                    showErrorBanner(resourceProvider.getString(R.string.str_selector_model_validation))
                }

            }

            SelectorType.YEAR -> {

                if (selectorList[prevSelected].isSelected.get()) {
                    onYearContinueSelector(selectorList[prevSelected].selectorOption?.name!!)

                } else {
                    showErrorBanner(resourceProvider.getString(R.string.str_selector_year_validation))
                }

            }
        }


    }

    private fun onYearContinueSelector(year: String) {
        val extras = Bundle()
        extras.putSerializable(APPConstants.SELECTOR_TYPE, SelectorType.YEAR)
        extras.putString(
            APPConstants.SELECTED_CAR_NAME_AND_MODEL_AND_YEAR,
            brand?.let { it.name.let { it ?: "" } } + brandName.let {
                it ?: ""
            } + " " + model?.let { it.name.let { it ?: "" } } + modelName.let {
                it ?: ""
            } + " " + year
        )

        extras.putString(APPConstants.SELECTOR_YEAR, year)




        if (modelName != null) {

            extras.putSerializable(
                APPConstants.SELECTOR_MODEL_NAME,
                modelName
            )

        } else {
            extras.putInt(
                APPConstants.SELECTOR_MODEL_ID,
                model?.id!!
            )
        }


        if (brandName != null) {

            extras.putString(
                APPConstants.SELECTOR_BRAND_BRAND_NAME,
                brandName
            )

        } else {
            extras.putSerializable(
                APPConstants.SELECTOR_BRAND_ID, brand?.id

            )
        }


        openActivity(AddMyCarActivity::class.java, extras)
    }

    private fun onContinueModelSelector(model: SelectorOption?, modelName: String?) {
        val extras = Bundle()
        extras.putSerializable(APPConstants.SELECTOR_TYPE, SelectorType.YEAR)

        if (modelName != null) {

            extras.putSerializable(
                APPConstants.SELECTOR_MODEL_NAME,
                modelName
            )
        } else {
            extras.putSerializable(
                APPConstants.SELECTOR_MODEL,
                model
            )
        }


        if (brandName != null) {

            extras.putString(
                APPConstants.SELECTOR_BRAND_BRAND_NAME,
                brandName
            )

        } else {
            extras.putSerializable(
                APPConstants.SELECTOR_BRAND, brand

            )
        }


        extras.putSerializable(
            APPConstants.SELECTOR_HEADER_TEXT,
            resourceProvider.getString(R.string.str_add_my_car_year)
        )
        extras.putSerializable(
            APPConstants.SELECTOR_OTHER_TEXT, resourceProvider.getString(
                R.string.str_add_my_car_other,
                resourceProvider.getString(R.string.str_add_my_car_year)
            )
        )

        openActivity(SelectorActivity::class.java, extras)
    }

    private fun onContinueBrandSelector(brand: Brand?, brandName: String?) {
        val extras = Bundle()
        extras.putSerializable(APPConstants.SELECTOR_TYPE, SelectorType.MODEL)
        if (brandName != null) {

            extras.putString(
                APPConstants.SELECTOR_BRAND_BRAND_NAME,
                brandName
            )

        } else {
            extras.putSerializable(
                APPConstants.SELECTOR_BRAND, brand

            )
        }


        extras.putSerializable(
            APPConstants.SELECTOR_HEADER_TEXT,
            resourceProvider.getString(R.string.str_add_my_car_model)
        )
        extras.putSerializable(
            APPConstants.SELECTOR_OTHER_TEXT, resourceProvider.getString(
                R.string.str_add_my_car_other,
                resourceProvider.getString(R.string.str_add_my_car_model)
            )
        )

        openActivity(SelectorActivity::class.java, extras)
    }

    fun onBtnBack() {

        goBack()
    }


    private fun getModels() {

        showLoading(false)
        appRepository.getModels(
            brand?.id,
            15,
            null,
            null,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Model?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Model?>?>?) {
                    hideLoading()

                    val list: List<Model?> = result?.data!!
                    if (!Utilities.isNullList(list)) {
                        hasData(list.size)

                        when(list?.size!!){
                            0,1,2,3 ->  selectorSpanCount.set(1)
                            4,5,6->  selectorSpanCount.set(2)
                            else -> selectorSpanCount.set(3)

                        }
                        for (model in list) {

                            selectorList.add(
                                SelectorItemViewModel(
                                    SelectorOption(
                                        model?.name,
                                        model?.id!!,
                                        model.image
                                    ), selectorType,
                                    context
                                )
                            )
                        }
//                        selectorAdapter?.notifyDataSetChanged()
                    } else
                        hasData(0)
                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getModels()
                            }
                        })
                }


            })
    }

    private fun getYears() {

        val years = APPConstants.SELECTOR_MIN_YEAR..currentYear
        for (year in years.reversed()) {

            selectorList.add(
                SelectorItemViewModel(
                    SelectorOption(
                        year.toString(),
                        year,
                        ""
                    ), selectorType,
                    context
                )
            )

        }

    }

    protected fun initView() {




        when (selectorType) {

            SelectorType.BRAND -> {

                isSelector.set(false)

                brandsAdapter = BasePagedListAdapter(object : ViewHolderInterface {
                    override fun onViewClicked(position: Int, id: Int) {

                        brandsList?.value!![prevSelected]?.isSelected?.set(false)
                        brandsList?.value!![position]?.isSelected?.set(true)
                        prevSelected = position

                        var handler = Handler()
                        handler.postDelayed(Runnable {

                            val extras = Bundle()
                            extras.putInt(
                                APPConstants.EXTRAS_KEY_BRAND_ID,
                                brandsList?.value!![position]?.brandId?.get()!!
                            )
                            extras.putSerializable(
                                APPConstants.EXTRAS_KEY_BRAND,
                                brandsList?.value!![position]?.brand!!
                            )


                            openView(AppScreenRoute.BRAND_MODELS, extras)

                        }, 500)


                    }
                })
            }

            SelectorType.MODEL, SelectorType.YEAR -> {

                isSelector.set(true)

                selectorAdapter = BaseAdapter(selectorList, object : ViewHolderInterface {


                    override fun onViewClicked(position: Int, id: Int) {


                        val extras = Bundle()


                        openView(AppScreenRoute.MODEL_CARS, extras)
                    }
                })
            }

        }


    }


    fun onAddOtherSelection() {

        selectorOtherError.set("")
        selectorOtherStatus.set(false)

        when (selectorType) {

            SelectorType.BRAND -> {

                if (!selectorOtherText.get()?.isEmpty()!!) {

                    onContinueBrandSelector(
                        null,
                        selectorOtherText.get()
                    )
                } else {
                    selectorOtherError.set(resourceProvider.getString(R.string.str_selector_brand_write_validation))
                    selectorOtherStatus.set(false)
                }

            }

            SelectorType.MODEL -> {

                if (!selectorOtherText.get()?.isEmpty()!!) {

                    onContinueModelSelector(
                        null
                        , selectorOtherText.get()
                    )

                } else {
                    selectorOtherError.set(resourceProvider.getString(R.string.str_selector_model_write_validation))
                    selectorOtherStatus.set(false)
                }

            }

            SelectorType.YEAR -> {

                if (!selectorOtherText.get()?.isEmpty()!!) {
                    if (year?.toInt() != null && year?.toInt()!! <= currentYear && year?.toInt()!! >= 1900) {

                        onYearContinueSelector(selectorOtherText.get()!!)
                    } else {

                        selectorOtherError.set(resourceProvider.getString(R.string.str_selector_year_write_validation_error))
                        selectorOtherStatus.set(false)
                    }


                } else {
                    selectorOtherError.set(resourceProvider.getString(R.string.str_selector_year_write_validation))
                    selectorOtherStatus.set(false)
                }

            }
        }


    }
}