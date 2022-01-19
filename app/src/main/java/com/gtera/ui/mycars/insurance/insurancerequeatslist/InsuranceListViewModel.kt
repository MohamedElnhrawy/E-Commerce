package com.gtera.ui.mycars.insurance.insurancerequeatslist

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.CarInsuranceRenewal
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.ui.utils.AppScreenRoute.CAR_RENEWAL_INSURANCE
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.util.*
import javax.inject.Inject

class InsuranceListViewModel @Inject constructor() : BaseViewModel<InsuranceListNavigator>(),
    ViewHolderInterface {


    var isRefreshing = ObservableBoolean(false)
    var insuranceAdapter: BaseAdapter<CarInsuranceListItemViewModel>? = null
    private val insuranceList =
        ArrayList<CarInsuranceListItemViewModel>()

    init {
        insuranceAdapter = BaseAdapter(insuranceList, this)
    }


    override fun onViewCreated() {
        super.onViewCreated()

        getInsuranceRequsts()

    }

    private fun getInsuranceRequsts() {

        showLoading(false)
        isRefreshing.set(true)
        appRepository.getInsuranceRequests(
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<List<CarInsuranceRenewal?>?>?> {
                override fun onSuccess(result: BaseResponse<List<CarInsuranceRenewal?>?>?) {
                    hideLoading()
                    addInsuranceRequests(result?.data!!)
                    hasData(result?.data?.size!!)
                    isRefreshing.set(false)

                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    isRefreshing.set(false)
                    showErrorBanner(throwable?.errorMsg)
                }

            })
    }

    override fun onViewRecreated() {
        super.onViewRecreated()
        getInsuranceRequsts()

    }

    override fun onViewClicked(position: Int, id: Int) {

        val extras = Bundle()
        extras.putSerializable(
            APPConstants.EXTRAS_KEY_INSURANCE_REQUEST,
            insuranceList.get(position).insuranceItem
        )

        openView(AppScreenRoute.CAR_RENEWAL_INSURANCE_DETAILS, extras)
    }

    protected fun addInsuranceRequests(List: List<CarInsuranceRenewal?>) {

        if (Utilities.isNullList(List)) return

        this.insuranceList.clear()
        for (insurance in List) {

            val offersItemViewModel = CarInsuranceListItemViewModel(
                insurance!!, resourceProvider
            )
            this.insuranceList.add(offersItemViewModel)
        }

//        insuranceAdapter?.updateList(insuranceList)
        insuranceAdapter?.notifyDataSetChanged()
    }

    fun onNewInsuranceRequest() {
        openView(CAR_RENEWAL_INSURANCE, null)
    }
}