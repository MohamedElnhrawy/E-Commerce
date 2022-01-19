package com.gtera.ui.mycars.add


import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Attribute
import com.gtera.data.model.CarAddingSpecs
import com.gtera.data.model.FiltrationOption
import com.gtera.data.model.SpecsItem
import com.gtera.data.model.requests.AddNewCarRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.FilterType
import com.gtera.ui.base.SpinnerInterface
import com.gtera.ui.cardetials.attributes.CarAttributesItemListViewModel
import com.gtera.ui.cars_filter.FilterItemViewModel
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.REQUEST_IMAGE_CAPTURE
import com.gtera.utils.APPConstants.REQUEST_TAKE_PHOTO
import com.gtera.utils.APPConstants.SELECTED_CAR_NAME_AND_MODEL_AND_YEAR
import com.gtera.utils.APPConstants.SELECTOR_BRAND_ID
import com.gtera.utils.APPConstants.SELECTOR_MODEL_ID
import com.gtera.utils.APPConstants.SELECTOR_YEAR
import com.gtera.utils.Utilities
import javax.inject.Inject

class AddMyCarViewModel @Inject constructor() : BaseViewModel<AddMyCarNavigator>(),
    ViewHolderInterface {

    var carBrandId: Int = 0
    var carModelId: Int = 0
    var carModelYear: String = ""
    var count: Int = 0
    var colorSelectedPosition: Int = 0
    var selectedColors: ArrayList<Int> = ArrayList<Int>()
    var carName = ObservableField("")
    var carCount = ObservableField("")
    var descriptionText = ObservableField("")
    var isElectricInjection = ObservableBoolean(false)
    var isPower = ObservableBoolean(false)
    fun onElectricInjectionChecked() {
        isElectricInjection.set(isElectricInjection.get().not())
    }

    fun onPowerChecked() {
        isPower.set(isPower.get().not())
    }

    var motorPowerByHorseText = ObservableField("")
    var motorPowerByHorseTextStatus = ObservableBoolean(false)
    var motorPowerByHorseTextError = ObservableField("")

    var motorCCText = ObservableField("")
    var motorCCTextStatus = ObservableBoolean(false)
    var motorCCTextError = ObservableField("")

    var numberOfCastingText = ObservableField("")
    var numberOfCastingTextStatus = ObservableBoolean(false)
    var numberOfCastingTextError = ObservableField("")

    var numOfDoorsText = ObservableField("")
    var numOfDoorsTextStatus = ObservableBoolean(false)
    var numOfDoorsTextError = ObservableField("")


    var numberOfSeatsText = ObservableField("")
    var numberOfSeatsTextStatus = ObservableBoolean(false)
    var numberOfSeatsTextError = ObservableField("")

    var fuelTankCapacityText = ObservableField("")
    var fuelTankCapacityTextStatus = ObservableBoolean(false)
    var fuelTankCapacityTextError = ObservableField("")

    var maxTorqueText = ObservableField("")
    var maxTorqueTextTextStatus = ObservableBoolean(false)
    var maxTorqueTextTextError = ObservableField("")

    var averageFuelConsumptionText = ObservableField("")
    var averageFuelConsumptionTextStatus = ObservableBoolean(false)
    var averageFuelConsumptionTextError = ObservableField("")


    var maxSpeedText = ObservableField("")
    var maxSpeedTextStatus = ObservableBoolean(false)
    var maxSpeedTextError = ObservableField("")

    var numOfAirbagsText = ObservableField("")
    var numOfAirbagsStatus = ObservableBoolean(false)
    var numOfAirbagsError = ObservableField("")

    var categoryNames = ObservableArrayList<String>()
    var categoryies = ObservableArrayList<SpecsItem>()
    var categorySelectedIndex = ObservableInt(0)

    var categoryiesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < categoryies.size && categorySelectedIndex.get() != position)
                categorySelectedIndex.set(position)
        }
    }
    var transmissionTypesNames = ObservableArrayList<String>()
    var transmissionTypes = ObservableArrayList<SpecsItem>()
    var transmissionTypesSelectedIndex = ObservableInt(0)

    var transmissionTypesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < transmissionTypes.size && transmissionTypesSelectedIndex.get() != position)
                transmissionTypesSelectedIndex.set(position)
        }
    }

    var pullTypesNames = ObservableArrayList<String>()
    var pullTypes = ObservableArrayList<SpecsItem>()
    var pullTypesSelectedIndex = ObservableInt(0)

    var pullTypesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < pullTypes.size && pullTypesSelectedIndex.get() != position)
                pullTypesSelectedIndex.set(position)
        }
    }

    var bodyTypesNames = ObservableArrayList<String>()
    var bodyTypes = ObservableArrayList<SpecsItem>()
    var bodyTypesSelectedIndex = ObservableInt(0)

    var bodyTypesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < bodyTypes.size && bodyTypesSelectedIndex.get() != position)
                bodyTypesSelectedIndex.set(position)
        }
    }


    var steeringWheelTypesNames = ObservableArrayList<String>()
    var steeringWheelTypes = ObservableArrayList<SpecsItem>()
    var steeringWheelTypesSelectedIndex = ObservableInt(0)

    var steeringWheelTypesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < steeringWheelTypes.size && steeringWheelTypesSelectedIndex.get() != position)
                steeringWheelTypesSelectedIndex.set(position)
        }
    }
    var imagesAdapter: BaseAdapter<MyCarAddImageItemViewModel>? = null
    var list: ObservableArrayList<MyCarAddImageItemViewModel> =
        ObservableArrayList<MyCarAddImageItemViewModel>()

    var colorsList: ArrayList<FilterItemViewModel> = ArrayList()
    var colorsListAdapter: BaseAdapter<FilterItemViewModel>? = null
    var attributesGroupListAdapter: BaseAdapter<CarAttributesItemListViewModel>? = null
    protected var attributesGroupList: ArrayList<CarAttributesItemListViewModel> =
        ArrayList<CarAttributesItemListViewModel>()

    var carImagesBse64 = ArrayList<String?>()


    var colorsSpanCount = ObservableInt(1)


    init {
        imagesAdapter = BaseAdapter(list, this)
        initListViews()
    }


    override fun onViewCreated() {
        super.onViewCreated()

        dataExtras?.getString(SELECTED_CAR_NAME_AND_MODEL_AND_YEAR).let { carName.set(it) }
        dataExtras?.getInt(SELECTOR_BRAND_ID).let { carBrandId = it!! }
        dataExtras?.getInt(SELECTOR_MODEL_ID).let { carModelId = it!! }
        dataExtras?.getString(SELECTOR_YEAR).let { carModelYear = it!! }
        carCount.set(
            getStringFromResources(
                R.string.str_my_car_add_photos,
                list.size
            )
        )
        getCarAddSpecs()

    }

    private fun getCarAddSpecs() {
        appRepository.getMyCarsAddingSpecs(

            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<CarAddingSpecs?>?> {

                override fun onSuccess(result: BaseResponse<CarAddingSpecs?>?) {

                    initializeCarCategoryData(result)
                    initializeBodyTypesData(result)
                    initializePullTypesData(result)
                    initializeTransmissionTypesData(result)
                    initializeSteeringWheelsTypesData(result)
                    initializeColors(result)
                    addCarAttributesGroups(result)

                }

                override fun onError(throwable: ErrorDetails?) {

                    showErrorBanner(throwable?.errorMsg)

                }
            })
    }

    private fun initializeCarCategoryData(result: BaseResponse<CarAddingSpecs?>?) {

        categoryies.clear()
        categoryNames.clear()
        categoryNames.add(getStringResource(R.string.str_choose_category_type))

        result?.data?.categories.let {
            it?.let {
                categoryies.addAll(it)
                it.forEach { job ->
                    job?.let {
                        categoryNames.add(it.name)
                    }
                }
            }
        }
    }


    private fun initializeBodyTypesData(result: BaseResponse<CarAddingSpecs?>?) {

        bodyTypes.clear()
        bodyTypesNames.clear()
        bodyTypesNames.add(getStringResource(R.string.str_choose_body_type))

        result?.data?.bodyTypes.let {
            it?.let {
                bodyTypes.addAll(it)
                it.forEach { job ->
                    job?.let {
                        bodyTypesNames.add(it.name)
                    }
                }
            }
        }
    }


    private fun initializePullTypesData(result: BaseResponse<CarAddingSpecs?>?) {

        pullTypes.clear()
        pullTypesNames.clear()
        pullTypesNames.add(getStringResource(R.string.str_choose_pull_type))

        result?.data?.pullTypes.let {
            it?.let {
                pullTypes.addAll(it)
                it.forEach { job ->
                    job?.let {
                        pullTypesNames.add(it.name)
                    }
                }
            }
        }
    }

    private fun initializeColors(result: BaseResponse<CarAddingSpecs?>?) {

        for (specsItem in result?.data?.colors!!) {

            colorsList.add(
                FilterItemViewModel(
                    FiltrationOption(
                        specsItem?.name,
                        specsItem?.id!!,
                        specsItem.value
                    ), FilterType.FILTER_TYPE_HORIZONTAL_COLOR, context
                )
            )

            colorsListAdapter?.notifyDataSetChanged()
        }


    }


    private fun initializeTransmissionTypesData(result: BaseResponse<CarAddingSpecs?>?) {

        transmissionTypes.clear()
        transmissionTypesNames.clear()
        transmissionTypesNames.add(getStringResource(R.string.str_choose_transmission_type))

        result?.data?.transmissionTypes.let {
            it?.let {
                transmissionTypes.addAll(it)
                it.forEach { job ->
                    job?.let {
                        transmissionTypesNames.add(it.name)
                    }
                }
            }
        }
    }


    private fun initializeSteeringWheelsTypesData(result: BaseResponse<CarAddingSpecs?>?) {

        steeringWheelTypes.clear()
        steeringWheelTypesNames.clear()
        steeringWheelTypesNames.add(getStringResource(R.string.str_choose_steering_wheel_type))

        result?.data?.steeringWheelTypes.let {
            it?.let {
                steeringWheelTypes.addAll(it)
                it.forEach { job ->
                    job?.let {
                        steeringWheelTypesNames.add(it.name)
                    }
                }
            }
        }
    }

    override fun onViewRecreated() {
        super.onViewRecreated()

    }

    override fun onViewClicked(position: Int, id: Int) {
        if (id == R.id.iv_item_remove) {
            list.removeAt(position)
            if (list.size == 1) {
                list.removeAt(position)
                imagesAdapter?.notifyDataSetChanged()

                carCount.set(
                    getStringFromResources(
                        R.string.str_my_car_add_photos,
                        list.size
                    )
                )
            } else {
                carCount.set(
                    getStringFromResources(
                        R.string.str_my_car_add_photos,
                        list.size - 1
                    )
                )
            }
            imagesAdapter?.notifyDataSetChanged()

        } else if (position == list.size - 1) {
            showAddPhotosDialog()
        }
    }


    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        var bm: Bitmap? = null
        if (requestCode == REQUEST_IMAGE_CAPTURE ||
            requestCode == APPConstants.REQUEST_TAKE_PHOTO
            && resultCode == Activity.RESULT_OK
        ) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (resultCode == Activity.RESULT_OK && extras != null) {
                    if (list.size > 10) {
                        showErrorBanner(getStringResource(R.string.str_my_car_add_photos_max_size_warning))
                        return
                    }
                    updateImagesList(extras.get("data") as Bitmap)
                    imagesAdapter?.notifyDataSetChanged()

                    carCount.set(
                        getStringFromResources(
                            R.string.str_my_car_add_photos,
                            list.size
                        )
                    )
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {

                if (resultCode == Activity.RESULT_OK && extras != null) {

                    if (extras.containsKey(APPConstants.CLIP_DATA)) {

                        val mArrayUri = extras.getStringArrayList(APPConstants.CLIP_DATA)
                        if (mArrayUri != null) {
                            count = mArrayUri.size
                            if (count > 10 || count + list.size > 10) {
                                showErrorBanner(getStringResource(R.string.str_my_car_add_photos_max_size_warning))
                                return
                            }

                            updateImagesList(mArrayUri)

                            imagesAdapter?.notifyDataSetChanged()
                        }

                        carCount.set(
                            getStringFromResources(
                                R.string.str_my_car_add_photos,
                                list.size - 1
                            )
                        )

                    } else {

                        updateImagesList(extras.get("data").toString())
                        imagesAdapter?.notifyDataSetChanged()

                        carCount.set(
                            getStringFromResources(
                                R.string.str_my_car_add_photos,
                                list.size - 1
                            )
                        )
                    }

                }
            } else super.onViewResult(requestCode, resultCode, extras)


        }
    }


    fun showAddPhotosDialog() {

        var cameraRequestPermissionLauncher =
            (lifecycleOwner as AddMyCarActivity).registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    dispatchCapturePictureIntent(REQUEST_IMAGE_CAPTURE)
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }


        var galleryRequestPermissionLauncher =
            (lifecycleOwner as AddMyCarActivity).registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    dispatchTakePictureIntent(REQUEST_TAKE_PHOTO, true)
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        openTakePhotoBottomSheet(
            cameraRequestPermissionLauncher,
            galleryRequestPermissionLauncher,
            REQUEST_IMAGE_CAPTURE,
            REQUEST_TAKE_PHOTO,
            true
        )

    }


    fun updateImagesList(bitmap: Bitmap) {
        if (list.size > 0)
            list.removeAt(list.size - 1)
        list.add(MyCarAddImageItemViewModel(bitmap, context))
        list.add(MyCarAddImageItemViewModel(true, context))

    }

    fun updateImagesList(uri: String) {
        val uriArrayList = ArrayList<String>()
        uriArrayList.add(uri)
        updateImagesList(uriArrayList)
    }

    fun updateImagesList(uriList: ArrayList<String>) {
        val uriArrayList = ArrayList<String>()
        if (!Utilities.isNullList(uriList)) uriArrayList.addAll(uriList)
        if (!Utilities.isNullList(uriArrayList)) {

            if (list.size > 0)
                list.removeAt(list.size - 1)
            for (i in uriArrayList.indices) {
                list.add(
                    MyCarAddImageItemViewModel(
                        uriArrayList[i],
                        true,
                        context
                    )
                )
            }

            list.add(MyCarAddImageItemViewModel(true, context))
        }
    }


    private fun imagesViewer(position: Int) {
//        val imagesView: ArrayList<MyCarAddImageItemViewModel> =
//            ArrayList<MyCarAddImageItemViewModel>()
//        if (!Utilities.isNullList(list)) imagesView.addAll(list)
//        if ( imagesView.size > 0) imagesView.removeAt(0)
//        StfalconImageViewer.Builder<Any?>(
//           context, imagesView
//        ) { imageView, item ->
//            BindingUtils.setImagePath(
//                imageView,
//                item.imagePath.get(),
//                item.isUriPath.get(),
//                item.imageBitmap.get()
//            )
//        }
//            .withStartPosition(if (isCheckOutPrescription.get()) position - 1 else position)
//            .allowZooming(true)
//            .allowSwipeToDismiss(true)
//            .withBackgroundColorResource(R.color.white)
//            .show()
    }


    private fun initListViews() {


        colorsListAdapter = BaseAdapter(colorsList, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {


                colorsList[colorSelectedPosition].isSelected.set(false)
                colorsList[position].isSelected.set(true)
                selectedColors.clear()
                colorsList[position].filtrationOption?.id?.let { selectedColors.add(it) }
                colorSelectedPosition = position

            }

        })

        attributesGroupListAdapter =
            BaseAdapter(attributesGroupList, object : ViewHolderInterface {

                override fun onViewClicked(position: Int, id: Int) {

                    if (id == R.id.iv_budgets_arrow) {
                        attributesGroupList.get(position).isexpanded.get()?.not()
                    }

                }
            })
    }


    protected fun addCarAttributesGroups(result: BaseResponse<CarAddingSpecs?>?) {

        if (Utilities.isNullList(result?.data?.attributes)) return

        this.attributesGroupList.clear()
        for (attributeCategory in result?.data?.attributes!!) {
            val carAttributesItemListViewModel = CarAttributesItemListViewModel(
                attributeCategory!!, true
            )
            this.attributesGroupList.add(carAttributesItemListViewModel)
        }

        attributesGroupListAdapter?.notifyDataSetChanged()
    }


    private fun getCarImagesBase64(): ArrayList<String?> {
        val imagesBase64 = ArrayList<String?>()
        for (i in 1 until list.size - 1) {
            val viewModel: MyCarAddImageItemViewModel = list[i]
            val bitmap: Bitmap = viewModel.bitmapImage!!
            if (bitmap != null) {
                val base64: String = Utilities.bitmapToBase64(bitmap)!!
                imagesBase64.add(base64)
            }
        }
        return imagesBase64
    }


    fun addNewCar() {


        var addNewCarRequest = AddNewCarRequest()

        addNewCarRequest.name = carName.get()
        addNewCarRequest.manufacture_year =carModelYear.toInt()
        if (carBrandId > 0)
            addNewCarRequest.brand_id = carBrandId
        if (carModelId > 0)
            addNewCarRequest.model_id = carModelId
        // images
        carImagesBse64 = getCarImagesBase64()
        if (carImagesBse64 == null && carImagesBse64.size < 1) {
            showErrorBanner(getStringResource(R.string.str_my_car_add_photos_min_size_warning))
            return
        }

        addNewCarRequest.images = carImagesBse64


        if (categorySelectedIndex.get() > 0) {
            addNewCarRequest.category_id = categoryies.get(categorySelectedIndex.get() - 1).id
        }

        addNewCarRequest.description = descriptionText.get()
        addNewCarRequest.colors = selectedColors
        addNewCarRequest.electric_injection = isElectricInjection.get()
        addNewCarRequest.power = isPower.get()

        addNewCarRequest.motor_power_by_horse = motorPowerByHorseText.get()
        addNewCarRequest.motor_cc = motorCCText.get()


        if (transmissionTypesSelectedIndex.get() > 0) {
            addNewCarRequest.transmission_type_id =
                transmissionTypes.get(transmissionTypesSelectedIndex.get() - 1).id
        }

        if (pullTypesSelectedIndex.get() > 0) {
            addNewCarRequest.pull_type_id = pullTypes.get(pullTypesSelectedIndex.get() - 1).id
        }

        if (bodyTypesSelectedIndex.get() > 0) {
            addNewCarRequest.body_type_id = bodyTypes.get(bodyTypesSelectedIndex.get() - 1).id
        }

        if (steeringWheelTypesSelectedIndex.get() > 0) {
            addNewCarRequest.steering_wheel_type_id =
                steeringWheelTypes.get(steeringWheelTypesSelectedIndex.get() - 1).id
        }

        addNewCarRequest.number_of_casting = numberOfCastingText.get()
        addNewCarRequest.number_of_doors = numOfDoorsText.get()
        addNewCarRequest.number_of_seats = numberOfSeatsText.get()
        addNewCarRequest.fuel_tank_capacity = fuelTankCapacityText.get()
        addNewCarRequest.max_torque = maxTorqueText.get()

        addNewCarRequest.average_fuel_consumbtion = averageFuelConsumptionText.get()
        addNewCarRequest.max_speed = maxSpeedText.get()
        addNewCarRequest.number_of_air_bags = numOfAirbagsText.get()
        addNewCarRequest.max_torque = maxTorqueText.get()
        addNewCarRequest.max_torque = maxTorqueText.get()

        var attributes: ArrayList<Attribute> = ArrayList<Attribute>()

        // attributes
        for (attributeGroupItem in attributesGroupList!!) {

            for (attributeItem in attributeGroupItem?.attributesList!!) {
                var attribute = Attribute()
                if (attributeItem?.isValue.get()!! && attributeItem?.isEnabled.get()!!) {

                    if (attributeItem.attributeValue.get() != null && !attributeItem.attributeValue.get()
                            ?.isEmpty()!!
                    ) {
                        attribute.id = attributeItem.attribute?.id
                        attribute.value = attributeItem.attributeValue.get()
                        attributes.add(attribute)
                    }
                } else {

                    if (attributeItem.isSelected.get()!!) {
                        attribute.id = attributeItem.attribute?.id
                        attribute.value = "1"
                        attributes.add(attribute)
                    }

                }
            }

        }

        attributes.let { addNewCarRequest.attributes = it }

        isLoading.set(true)
        appRepository.addNewMyCar(
            addNewCarRequest,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<Any?>?> {
                override fun onSuccess(result: BaseResponse<Any?>?) {
                    isLoading.set(false)
                    showSuccessBanner(getStringResource(R.string.str_your_car_added_successfully))
                    openView(AppScreenRoute.MY_CARS_LIST, null)

                }

                override fun onError(throwable: ErrorDetails?) {
                    isLoading.set(false)
                    showErrorBanner(throwable?.errorMsg)
                }
            })


    }
}