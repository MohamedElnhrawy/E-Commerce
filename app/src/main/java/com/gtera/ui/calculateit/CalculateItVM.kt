package com.gtera.ui.calculateit

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Car
import com.gtera.data.model.CreditCard
import com.gtera.data.model.JobTitle
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.brands.BrandsDataSourceFactory
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.BrandItemViewModel
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.*
import javax.inject.Inject

class CalculateItVM @Inject constructor() : BaseViewModel<CalculateItNavigator>(),
    ViewHolderInterface {

    //Credit Cards
    override fun onViewClicked(position: Int, id: Int) {

        if (position >= creditCards.size) {
            return
        } else {
            creditCards.removeAt(position)
            creditCardsList.removeAt(position)
            cardsAdapter.notifyDataSetChanged()
        }
    }

    var attempRequest = 0
    var creditCards = ArrayList<CreditCard>()
    var creditCardsList = ArrayList<CreditCardItemVM>()
    var cardsAdapter = BaseAdapter<CreditCardItemVM>(creditCardsList, this)
    var carsVerticalOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL


    //
    var totalInstallments = ObservableField("")
    var advancePayment = ObservableField("")
    var monthlySalary = ObservableField("")
    var maxInstallmentYears = ObservableInt(0)
    var jobTitles = ObservableArrayList<String>()
    var jobTitlesSelectedIndex = ObservableInt(0)
    var JobTitlesList = ObservableArrayList<JobTitle>()

    var installmentYears = ObservableArrayList<String>()
    var installmentYearsSelectedIndex = ObservableInt(0)

    // brands
    lateinit var brandsDataSourceFactory: BrandsDataSourceFactory
    var isAllSelected = ObservableBoolean(false)
    var selectedBrands = ObservableArrayList<Int>()

    var brandsOrientation: ListOrientation? = ListOrientation.ORIENTATION_HORIZONTAL

    var brandsAdapter: BasePagedListAdapter<BrandItemViewModel>? = null
    var spanCount = ObservableInt(3)

    lateinit var brandsList: LiveData<PagedList<BrandItemViewModel>>
    private var list: PagedList<BrandItemViewModel>? = null

    init {

        brandsAdapter = BasePagedListAdapter(object : ViewHolderInterface {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onViewClicked(position: Int, id: Int) {

                if (selectedBrands.contains(brandsList.value!![position]?.brand?.id)) {

                    selectedBrands.remove(brandsList.value!![position]?.brand?.id)
                    brandsList.value!![position]?.isSelected?.set(false)
                } else {
                    selectedBrands.add(brandsList.value!![position]?.brand?.id!!)
                    brandsList.value!![position]?.isSelected?.set(true)
                }

            }
        })

    }

    override fun onViewCreated() {
        super.onViewCreated()
        getJobTitles()
        initInstallmentsYears()

        jobTitlesSelectedIndex.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (jobTitlesSelectedIndex.get() > 0)
                    initInstallmentsYears()
            }

        })
    }


    private fun initBrandsDataSource() {

        brandsDataSourceFactory = BrandsDataSourceFactory(
            this,
            ObservableField(""),
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

        brandsList.observe(lifecycleOwner!!, Observer {


            list?.clear()
            list?.addAll(it)
            brandsAdapter?.submitList(it)
            list?.size?.let { it1 -> hasData(it1) }


        })

    }

    private fun getJobTitles() {
        appRepository.getJobTitles(
            1,
            100,
            null,
            null,
            null,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<JobTitle?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<JobTitle?>?>?) {


                    JobTitlesList.clear()
                    jobTitles.clear()
                    jobTitles.add(getStringResource(R.string.str_choose_job))

                    result?.data.let {
                        it?.let {
                            JobTitlesList.addAll(it)
                            it.forEach { job ->
                                job?.let {
                                    jobTitles.add(it.name)
                                }
                            }
                        }
                    }
                    jobTitlesSelectedIndex.set(0)
                    initBrandsDataSource()


                }

                override fun onError(throwable: ErrorDetails?) {
                    if (throwable?.statusCode != STATUS_CODE_UNAUTHORIZED_ERROR ||
                        throwable.statusCode != STATUS_CODE_SERVER_ERROR ||
                        throwable.statusCode != STATUS_CODE_NETWORK_ERROR ||
                        throwable.statusCode != STATUS_CODE_TIMEOUT_ERROR
                    ) {
                        attempRequest++
                        if (attempRequest > 2) {
                            showErrorBanner(throwable?.errorMsg)
                            return
                        } else
                            getJobTitles()
                    }else
                        showErrorBanner(throwable?.errorMsg)
                }


            })


    }

    private fun initInstallmentsYears() {
        if (jobTitlesSelectedIndex.get() == 0 || JobTitlesList.size == 0 || JobTitlesList.size < (jobTitlesSelectedIndex.get() - 1))
            return

        maxInstallmentYears.set(JobTitlesList[jobTitlesSelectedIndex.get() - 1].maxInstallmentYears)
        installmentYears.clear()
        installmentYears.add(getStringResource(R.string.str_choose_installment_years))
        installmentYearsSelectedIndex.set(0)
        for (i in 1..maxInstallmentYears.get()) {
            installmentYears.add(i.toString())
        }
    }

    fun selectAllBrands() {
        isAllSelected.set(isAllSelected.get().not())
        brandsList.value?.forEach { it ->
            it.isSelected.set(isAllSelected.get())
            if (isAllSelected.get())
                selectedBrands.add(it.brand?.id)
            else
                selectedBrands.clear()
        }
    }

    fun validateData(): Boolean {
        if (jobTitlesSelectedIndex.get() <= 0) {
            showErrorBanner(getStringResource(R.string.str_please_choose_your_job))
            return false
        } else if (advancePayment.get().isNullOrEmpty() || advancePayment.get()!!.toDouble() < 0) {
            showErrorBanner(getStringResource(R.string.str_please_insert_advance_payment))
            return false
        } else if (installmentYearsSelectedIndex.get() <= 0) {
            showErrorBanner(getStringResource(R.string.str_please_insert_installment_years))
            return false
        } else if (monthlySalary.get().isNullOrEmpty() || monthlySalary.get()!!.toDouble() < 0) {
            showErrorBanner(getStringResource(R.string.str_please_insert_monthly_salary))
            return false
        }
//        else if (selectedBrands.size <= 0){
//            showErrorBanner(getStringResource(R.string.str_please_brand))
//            return false
//        }

        return true
    }

    fun calculateItClicked() {
        if (validateData())
            calculateIt(prepareBody())
    }

    fun calculateIt(body: HashMap<String, Any>) {
        isLoading.set(true)
        appRepository.calculateIt(
            body,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?> {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {
                    isLoading.set(false)
                    val extra = Bundle()
                    extra.putSerializable(APPConstants.EXTRAS_KEY_CARS, result?.data)
                    openView(AppScreenRoute.CALCULATE_IT_RESULTS, extra)

                }

                override fun onError(throwable: ErrorDetails?) {
                    isLoading.set(false)

                    showErrorBanner(throwable?.errorMsg)
                }


            })
    }

    fun prepareBody(): HashMap<String, Any> {
        val body = HashMap<String, Any>()
        JobTitlesList[jobTitlesSelectedIndex.get() - 1].id?.let { body.put("job_title_id", it) }
        advancePayment.get()?.toDouble()?.let { body.put("advance_payment", it) }
        monthlySalary.get()?.toDouble()?.let { body.put("salary", it) }
        body.put("installment_years", installmentYears[installmentYearsSelectedIndex.get() ].toInt())
        if (creditCardsList.size > 0)
            body.put("credit_cards", getCreditsCards())
        if (!totalInstallments.get().isNullOrEmpty())
            body.put("installments", totalInstallments.get().toString().toDouble())
        if (selectedBrands.size > 0)
            body.put("brand_id", prepareSelectedBrandsId())
        return body
    }

    fun onAddCreditCardClicked() {
        var newCard: CreditCard? = null
        if (creditCardsList.size == 0) {
            newCard = CreditCard(1.toString(), null, null)
        } else {
            if (!checkAllValidCards())
                showErrorBanner(getStringResource(R.string.invalid_card_data))
            else
                newCard = CreditCard((creditCardsList.size + 1).toString(), null, null)
        }
        newCard?.let {
            creditCards.add(it)
            creditCardsList.add(CreditCardItemVM(it, resourceProvider))
            cardsAdapter.notifyDataSetChanged()
        }
    }

    fun checkAllValidCards(): Boolean {
        for (card in creditCardsList) {
            if (!card.isValidCard())
                return false
        }

        return true
    }

    fun getCreditsCards(): ArrayList<CreditCard> {
        val list = ArrayList<CreditCard>()
        creditCardsList.forEach {
            list.add(it.getCard())
        }

        return list
    }

    fun prepareSelectedBrandsId(): String {
        var ids = ""
        for (position in 0 until selectedBrands.size) {
            ids += if (position == selectedBrands.size - 1) {
                selectedBrands[position].toString()
            } else {
                selectedBrands[position].toString() + ","

            }
        }
        return ids
    }
}