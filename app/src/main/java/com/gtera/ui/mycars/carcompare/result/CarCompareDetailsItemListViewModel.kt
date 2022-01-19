package com.gtera.ui.mycars.carcompare.result

import android.content.Context
import com.gtera.R
import com.gtera.data.model.Attribute
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.BaseItemViewModel
import com.gtera.ui.base.CarCompareItemType
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import javax.inject.Inject

class CarCompareDetailsItemListViewModel @Inject constructor() : BaseItemViewModel() {


    override val layoutId: Int
        get() = R.layout.cars_compare_details_item_layout

    init {
        initCarsCompareView()
    }


    var carsCompareItemOrientation: ListOrientation? = ListOrientation.ORIENTATION_HORIZONTAL

    var carsCompareItemAdapter: BaseAdapter<CarCompareDetailsItemViewModel>? = null

    // adapter's lists
    protected var carsCompareItemList: ArrayList<CarCompareDetailsItemViewModel> =
        ArrayList<CarCompareDetailsItemViewModel>()


    override fun hashKey(): Any {
        TODO("Not yet implemented")
    }

    var attributes = ArrayList<Attribute>()
    var carNames = ArrayList<String>()
    var carImages = ArrayList<String>()
    var attributeName: String? = null


    constructor(attributeName: String?) : this() {
        this.attributeName = attributeName!!
        createCompareItemsRows(attributeName)
    }

    constructor(attributes: ArrayList<Attribute>?, context: Context) : this() {
        this.attributes = attributes!!
        createCompareItemsRows(attributes, context)
    }


    constructor(
        carNames: ArrayList<String>?,
        carImages: ArrayList<String>?,
        context: Context
    ) : this() {
        this.carNames = carNames!!
        this.carImages = carImages!!
        createCompareItemsRows(carImages, carNames, context)
    }

    protected fun initCarsCompareView() {

        carsCompareItemAdapter = BaseAdapter(carsCompareItemList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {
            }
        })


    }


    protected fun createCompareItemsRows(
        carsToCompareImages: ArrayList<String>,
        carsToCompareNames: ArrayList<String>,
        context: Context
    ) {
        for (i in 0 until carsToCompareImages.size) {
            var carCompareDetailsItemViewModel =
                CarCompareDetailsItemViewModel(
                    carsToCompareImages.get(i),
                    carsToCompareNames.get(i),
                    CarCompareItemType.IMAGE,
                    context,
                    carsToCompareImages.size == 2
                )
            carsCompareItemList.add(carCompareDetailsItemViewModel)

        }
        carsCompareItemAdapter?.updateList(carsCompareItemList)
        carsCompareItemAdapter?.notifyDataSetChanged()
    }


    protected fun createCompareItemsRows(
        attributes: ArrayList<Attribute>,
        context: Context
    ) {


        for (i in 0 until attributes.size) {

            var carCompareDetailsItemViewModel =
                CarCompareDetailsItemViewModel(
                    attributes.get(i)?.value ?: "",
                    CarCompareItemType.VALUE,
                    context,
                    attributes.size == 2
                )
            carsCompareItemList.add(carCompareDetailsItemViewModel)
        }

        carsCompareItemAdapter?.updateList(carsCompareItemList)
        carsCompareItemAdapter?.notifyDataSetChanged()
    }


    protected fun createCompareItemsRows(
        attributeName: String?
    ) {

        var carCompareDetailsItemViewModel =
            CarCompareDetailsItemViewModel(
                attributeName!!,
                CarCompareItemType.NAME
            )
        carsCompareItemList.add(carCompareDetailsItemViewModel)

        carsCompareItemAdapter?.updateList(carsCompareItemList)
        carsCompareItemAdapter?.notifyDataSetChanged()

    }
}