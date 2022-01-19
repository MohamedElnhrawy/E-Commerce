package com.gtera.ui.ordernow.details

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.model.Order
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.mycars.add.MyCarAddImageItemViewModel
import com.gtera.ui.ordernow.PowerAttorneyItem
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.CASH
import com.gtera.utils.Utilities
import javax.inject.Inject

class OrderDetailsViewModel @Inject constructor() : BaseViewModel<OrdeDetailsNavigator>(),
    ViewHolderInterface {

    var count: Int = 0
    var carImage = ObservableField("")
    var carColorName = ObservableField("")
    var carBranchName = ObservableField("")
    var carAdvancedPaymentAmount = ObservableField("")
    var carDeliveryPlace = ObservableField("")
    var hasCarLicense = ObservableBoolean(false)
    var hasCarInsurance = ObservableBoolean(false)
    var isCashOrder = ObservableBoolean(false)
    var carName = ObservableField("")
    var carPrice = ObservableField("")
    var order: Order? = null

    override fun onViewCreated() {
        super.onViewCreated()

        order = dataExtras?.getSerializable(APPConstants.ORDER_NOW_CAR_ORDER) as Order?
        setUpOrderData(order!!)

    }

    fun setUpOrderData(order: Order) {

        carImage.set(order.car?.images?.get(0))
        carBranchName.set(order.branch?.name)
        hasCarLicense.set(order.needCarLicense!!)
        hasCarInsurance.set(order.needCarInsurance!!)
        isCashOrder.set(order.paymentType?.equals(CASH)!!)
        carPrice.set(resourceProvider.getString(R.string.str_egp, order.car?.priceFrom))
        carName.set(order.car?.name)
        carColorName.set(order.color?.name)
        carAdvancedPaymentAmount.set(order.advancePayment.toString())
        carDeliveryPlace.set(order.deliveryPlace)
        updateImagesList(order.powerOfAttorneyImage as ArrayList<String>)
        order.powerOfAttorneyNames?.forEach {
            list.add(PowerAttorneyItem(it!!))
        }

    }

    var powerAttorneyAdapter: BaseAdapter<PowerAttorneyItem>? = null
    private val list =
        java.util.ArrayList<PowerAttorneyItem>()

    var imagesAdapter: BaseAdapter<MyCarAddImageItemViewModel>? = null
    var imagesList: ObservableArrayList<MyCarAddImageItemViewModel> =
        ObservableArrayList<MyCarAddImageItemViewModel>()

    override fun onViewRecreated() {
        super.onViewRecreated()

    }

    override fun onViewClicked(position: Int, id: Int) {
        TODO("Not yet implemented")
    }

    fun updateImagesList(uriList: ArrayList<String>) {
        val uriArrayList = ArrayList<String>()
        if (!Utilities.isNullList(uriList)) uriArrayList.addAll(uriList)
        if (!Utilities.isNullList(uriArrayList)) {


            for (i in uriArrayList.indices) {
                imagesList.add(
                    MyCarAddImageItemViewModel(
                        uriArrayList[i]
                    )
                )
            }

        }
    }

}