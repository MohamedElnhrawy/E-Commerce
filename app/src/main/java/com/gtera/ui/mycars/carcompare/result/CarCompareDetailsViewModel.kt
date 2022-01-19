package com.gtera.ui.mycars.carcompare.result

import androidx.databinding.ObservableBoolean
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.model.Attribute
import com.gtera.data.model.AttributeGroup
import com.gtera.data.model.Car
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import javax.inject.Inject

class CarCompareDetailsViewModel @Inject constructor() :
    BaseViewModel<CarCompareDetailsNavigator>() {

    init {
        initCarsCompareView()
    }

    var isRefreshing = ObservableBoolean(false)
    var carsToCompare = ArrayList<Car>()

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var carsCompareVerticalOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL

    var carsCompareAdapter: BaseAdapter<CarCompareDetailsItemListViewModel>? = null

    // adapter's lists
    protected var carsCompareList: ArrayList<CarCompareDetailsItemListViewModel> =
        ArrayList<CarCompareDetailsItemListViewModel>()


    override fun onViewCreated() {
        super.onViewCreated()

        carsToCompare =
            dataExtras?.getSerializable(APPConstants.EXTRAS_KEY_CAR_COMPARE_LIST) as ArrayList<Car>

        doCompareAndCreateRows(carsToCompare)

    }


    protected fun initCarsCompareView() {

        carsCompareAdapter = BaseAdapter(carsCompareList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {
            }
        })

    }

    protected fun doCompareAndCreateRows(carsToCompare: ArrayList<Car>) {

        if (Utilities.isNullList(carsToCompare)) return

        this.carsCompareList.clear()
        var allCarsAttributesGroup = LinkedHashSet<AttributeGroup>()
        var allCarsAttributes = LinkedHashSet<Attribute>()

        CompareMainCarsFeatures(carsToCompare, allCarsAttributesGroup)


        var attributeGroup: AttributeGroup? = null
        var attributeList = ArrayList<Attribute>()

        // get each group in allcarsAttributes and find matching with cars and get all attributes inside matching group
        for (attributesGroup in allCarsAttributesGroup) {


            // grouping all attributes inside each group
            for (car in carsToCompare) {

                attributeGroup = car.attributes?.filter { attr ->
                    attr.name?.equals(
                        attributesGroup.name
                    )!!
                }?.single()!!

                allCarsAttributes.addAll(attributeGroup.attributes!!)
            }


            // find matching attribute value with cars attributes
            for (attribute in allCarsAttributes) {


                for (car in carsToCompare) {


                    // get matched car group if found
                    attributeGroup = car.attributes?.filter { attr ->
                        attr.name?.equals(
                            attributesGroup.name
                        )!!
                    }?.single()!!


                    // get matched car attribute if found
                    val carAttribute = attributeGroup.attributes?.filter { attr ->
                        attr.name.equals(attribute.name)
                    }?.let { if (it.isNotEmpty()) it.single() else Attribute(0,"", "", "") }

                    // collect values in all cars in one list to draw it
                    attributeList.add(carAttribute ?: Attribute(0,"", "", ""))

                }

                carsCompareList.add(CarCompareDetailsItemListViewModel(attributeList.get(0).name))
                carsCompareList.add(CarCompareDetailsItemListViewModel(attributeList, context))
                attributeList = ArrayList<Attribute>()
                allCarsAttributes = LinkedHashSet<Attribute>()

            }


        }

        carsCompareAdapter?.updateList(carsCompareList)
        carsCompareAdapter?.notifyDataSetChanged()
    }

    private fun CompareMainCarsFeatures(
        carsToCompare: ArrayList<Car>,
        allCarsAttributesGroup: LinkedHashSet<AttributeGroup>
    ) {
        val carsCompareImages = ArrayList<String>()
        val carsCompareNames = ArrayList<String>()
        val carsCompareFromPrices = ArrayList<Attribute>()
        val carsCompareToPrices = ArrayList<Attribute>()
        val carsCompareNumOfEngine = ArrayList<Attribute>()
        val carsCompareFuelTankCapacity = ArrayList<Attribute>()
        val carsCompareAcceleration = ArrayList<Attribute>()
        val carsCompareNumberOfSeats = ArrayList<Attribute>()
        val carsCompareNumberOfDoors = ArrayList<Attribute>()
        val carsCompareMaxTorque = ArrayList<Attribute>()
        val carsCompareAverageFuelConsumption = ArrayList<Attribute>()
        val carsCompareMaxPowerByHorse = ArrayList<Attribute>()
        val carsCompareBodyType = ArrayList<Attribute>()
        val carsCompareStearingWheelType = ArrayList<Attribute>()
        val carsComparepullType = ArrayList<Attribute>()
        val carsCompareElectricInjection = ArrayList<Attribute>()
        val carsComparePower = ArrayList<Attribute>()
        val carsCompareMonthlyPayment = ArrayList<Attribute>()
        val carsCompareManfactureYear = ArrayList<Attribute>()

        for (car in carsToCompare) {

            car.attributes?.let { allCarsAttributesGroup.addAll(it!!) }

            carsCompareImages.add(car.images?.get(0)!!)
            carsCompareNames.add( car.brand.let { if (it != null) it.name.let {  it?: "" } else "" } + " " + car.name.let { if (it!= null) it else "" }  + " " + car.manufactureYear.let { if (it!= null) it else "" } )


            carsCompareFromPrices.add(Attribute(0,"", car.priceFrom?.let { it?:"" }))
            carsCompareToPrices.add(Attribute(0,"", car.priceTo?.let { it?:"" }))
            carsCompareNumOfEngine.add(Attribute(0,"", car.numberOfEngineGears?.let { it?:"" }))
            carsCompareFuelTankCapacity.add(Attribute(0,"", car.fuelTankCapacity?.let { it?:"" }))
            carsCompareAcceleration.add(Attribute(0,"", car.acceleration?.let { it?:"" }))
            carsCompareNumberOfSeats.add(Attribute(0,"", car.numberOfSeats?.let { it?:"" }))
            carsCompareNumberOfDoors.add(Attribute(0,"", car.numberOfDoors?.let { it?:"" }))
            carsCompareMaxTorque.add(Attribute(0,"", car.maxTorque?.let { it?:"" }))
            carsCompareAverageFuelConsumption.add(
                Attribute(
                    0,
                    "",
                    car.averageFuelConsumbtion?.let { it?:"" })
            )
            carsCompareMaxPowerByHorse.add(
                Attribute(
                    0,
                    "",
                    car.motorPowerByHorse?.let { it?:"" })
            )
            carsCompareBodyType.add(Attribute(0,"", car.bodyType?.name?.let { it?:"" }))
            carsCompareStearingWheelType.add(Attribute(0,"", car.steeringWheelType?.name?.let { it?:"" }))
            carsComparepullType.add(Attribute(0,"", car.pullType?.name?.let { it?:"" }))
            carsCompareElectricInjection.add(
                Attribute(
                    0,
                    "",
                    if (car.electricInjection!!) getStringResource(R.string.str_yes) else getStringResource(
                        R.string.str_no
                    )
                )
            )
            carsComparePower.add(
                Attribute(
                    0,
                    "",
                    if (car.power!!) getStringResource(R.string.str_yes) else getStringResource(R.string.str_no)
                )
            )
            carsCompareMonthlyPayment.add(Attribute(0,"", car.monthlyPayment?.let { it?:"" }))
            carsCompareManfactureYear.add(Attribute(0,"", car.manufactureYear?.let { it?:"" }))

        }


        carsCompareList.add(
            CarCompareDetailsItemListViewModel(
                carsCompareNames,
                carsCompareImages,
                context
            )
        )
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_starting_price)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareFromPrices, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_max_price)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareToPrices, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_num_of_engine_gears)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareNumOfEngine, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_fuel_tank_capacity)))
        carsCompareList.add(
            CarCompareDetailsItemListViewModel(
                carsCompareFuelTankCapacity,
                context
            )
        )
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_num_of_seats)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareNumberOfSeats, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_num_of_doors)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareNumberOfDoors, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_max_torque)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareMaxTorque, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_average_fuel_consumbtion)))
        carsCompareList.add(
            CarCompareDetailsItemListViewModel(
                carsCompareAverageFuelConsumption,
                context
            )
        )
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_max_power_by_horse)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareMaxPowerByHorse, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_body_type)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareBodyType, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_steering_wheel_type)))
        carsCompareList.add(
            CarCompareDetailsItemListViewModel(
                carsCompareStearingWheelType,
                context
            )
        )
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_pull_type)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsComparepullType, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_electronic_injection)))
        carsCompareList.add(
            CarCompareDetailsItemListViewModel(
                carsCompareElectricInjection,
                context
            )
        )
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_Power)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsComparePower, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_monthly_payment)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareMonthlyPayment, context))
        carsCompareList.add(CarCompareDetailsItemListViewModel(getStringResource(R.string.str_car_compare_manufacture_year)))
        carsCompareList.add(CarCompareDetailsItemListViewModel(carsCompareManfactureYear, context))
    }


}