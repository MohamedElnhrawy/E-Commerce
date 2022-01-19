package com.gtera.ui.ordernow

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.*
import com.gtera.data.model.requests.OrderNowDataRequest
import com.gtera.data.model.requests.OrderNowRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.FilterType
import com.gtera.ui.base.SpinnerInterface
import com.gtera.ui.cars_filter.FilterItemViewModel
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.mycars.add.MyCarAddImageItemViewModel
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.CASH
import com.gtera.utils.APPConstants.ORDER_NOW_CAR
import com.gtera.utils.APPConstants.PREMIUM
import com.gtera.utils.Utilities
import javax.inject.Inject

class OrderNowViewModel @Inject constructor() : BaseViewModel<OrderNowNavigator>(),
    ViewHolderInterface {


    var colorsList: ArrayList<FilterItemViewModel> = ArrayList()
    var colorsListAdapter: BaseAdapter<FilterItemViewModel>? = null
    var selectedColor = 0
    var selectedColorPosition = 0
    var colorsSpanCount = ObservableInt(1)
    var deliveryPlacesNames = ObservableArrayList<String>()
    var deliveryPlacesSelectedIndex = ObservableInt(0)
    var isCashOrder = ObservableBoolean(true)
    var hasCarLisence = ObservableBoolean(true)
    var hasCarInsurance = ObservableBoolean(true)
    var count: Int = 0
    var carImage = ObservableField("")
    var carName = ObservableField("")
    var carPrice = ObservableField("")
    var carType = ObservableField("")
    var monthlyPayment = ObservableField("")
    var orderedCar: Car? = null
    var orderRequest: OrderNowRequest? = null
    var deliveryPlacesSelectionStatus = ObservableBoolean(false)
    var branchesSelectionStatus = ObservableBoolean(false)
    var advancedPaymentStatus = ObservableBoolean(false)
    var colorSelectionStatus = ObservableBoolean(false)
    var attorneyPersonsNames = ArrayList<String>()

    var isRefreshing = ObservableBoolean(false)

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var deliveryPlacesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < deliveryPlacesNames.size && deliveryPlacesSelectedIndex.get() != position)
                deliveryPlacesSelectedIndex.set(position)
        }
    }

    protected var attorneyNamesList: ArrayList<AttorneyPersonItem> = ArrayList()
    protected var branchesList: ArrayList<BranchItem> = ArrayList()
    var branchesNames = ObservableArrayList<String>()
    var branchesSelectedIndex = ObservableInt(0)

    var branchesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < branchesList.size && branchesSelectedIndex.get() != position)
                branchesSelectedIndex.set(position)
            updatePowerAttorneyNames(position)

        }
    }

    private fun updatePowerAttorneyNames(position: Int) {


    }

    var advancedPaymentText = ObservableField("")
    var advancedPaymentTextStatus = ObservableBoolean(false)
    var advancedPaymentTextError = ObservableField("")


    var powerAttorneyAdapter: BaseAdapter<PowerAttorneyItem>? = null
    private val powerAttorneyList =
        java.util.ArrayList<PowerAttorneyItem>()

    var imagesAdapter: BaseAdapter<MyCarAddImageItemViewModel>? = null
    var imagesList: ObservableArrayList<MyCarAddImageItemViewModel> =
        ObservableArrayList<MyCarAddImageItemViewModel>()

    init {
        colorsListAdapter = BaseAdapter(colorsList, object : ViewHolderInterface {
            override fun onViewClicked(position: Int, id: Int) {


                colorsList[selectedColorPosition].isSelected.set(false)
                colorsList[position].isSelected.set(true)
                selectedColorPosition = position
                selectedColor = colorsList[position].filtrationOption?.id!!
            }
        })
        imagesAdapter = BaseAdapter(imagesList, this)
        powerAttorneyAdapter = BaseAdapter(powerAttorneyList, this)

    }

    override fun onViewCreated() {
        super.onViewCreated()


        initializeOrderNowData()

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            initializeOrderNowData()
        }
    }

    private fun initializeOrderNowData() {

        orderedCar = dataExtras?.getSerializable(ORDER_NOW_CAR) as Car?
        carType.set(dataExtras?.getString(APPConstants.ORDER_NOW_CAR_TYPE))
        monthlyPayment.set(dataExtras?.getString(APPConstants.ORDER_NOW_CAR_MONTHLY_PAYMENT))

        branchesNames.add(resourceProvider.getString(R.string.str_car_insurance_branch))

        carImage.set(orderedCar?.images?.get(0))
        carName.set(orderedCar?.brand?.name.let { if (it != null) it else "" } + orderedCar?.name.let { if (it != null) it else "" })
        if (carType.get().equals(APPConstants.SLIDER_TYPE_OFFER))
            carPrice.set(monthlyPayment.get())
        else
            orderedCar?.priceFrom.let {
                carPrice.set(
                    resourceProvider.getString(
                        R.string.str_egp, orderedCar?.priceFrom
                    )
                )
            }

        deliveryPlacesNames.add(resourceProvider.getString(R.string.str_order_now_delivery_place))
        deliveryPlacesNames.addAll(context.resources.getStringArray(R.array.str_order_now_delivery_places))

        orderedCar?.colors?.let {
            it.forEach {


                colorsList.add(
                    FilterItemViewModel(
                        FiltrationOption(
                            it?.name,
                            it?.id?.toInt()!!,
                            it.code
                        ), FilterType.FILTER_TYPE_HORIZONTAL_COLOR, context
                    )
                )
            }
            colorsListAdapter?.notifyDataSetChanged()
        }

        showLoading(false)
        appRepository.getOrderNowData(OrderNowDataRequest(
            arrayListOf(
                APPConstants.ORDER_NOW_ATTORNEY_PERSON,
                APPConstants.ORDER_NOW_BRANCH
            )
        ),
            lifecycleOwner, object : APICommunicatorListener<BaseResponse<OrderNowViewData?>?> {
                override fun onSuccess(result: BaseResponse<OrderNowViewData?>?) {

                    attorneyNamesList =
                        (result?.data?.attorneyPersons as ArrayList<AttorneyPersonItem>?)!!
                    attorneyNamesList.forEachIndexed { index, it ->
                        powerAttorneyList.add(
                            PowerAttorneyItem((index + 1).toString() + "- " + it.name!!)
                        )
                        attorneyPersonsNames.add(it.name!!)
                    }
                    branchesList = (result?.data?.branches as ArrayList<BranchItem>?)!!
                    branchesList.forEach { branchesNames.add(it.name) }
                    hideLoading()
                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    showErrorBanner(throwable?.errorMsg)

                }

            }
        )
    }


    override fun onViewRecreated() {
        super.onViewRecreated()

    }

    override fun onViewClicked(position: Int, id: Int) {

    }


    fun showAddPhotosDialog() {

        var cameraRequestPermissionLauncher =
            (lifecycleOwner as OrderNowFragment).registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    dispatchCapturePictureIntent(APPConstants.REQUEST_IMAGE_CAPTURE)
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }


        var galleryRequestPermissionLauncher =
            (lifecycleOwner as OrderNowFragment).registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    dispatchTakePictureIntent(APPConstants.REQUEST_TAKE_PHOTO, true)
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
            APPConstants.REQUEST_IMAGE_CAPTURE,
            APPConstants.REQUEST_TAKE_PHOTO,
            true
        )

    }


    fun updateImagesList(bitmap: Bitmap) {
        if (imagesList.size > 0)
            imagesList.removeAt(imagesList.size - 1)
        imagesList.add(MyCarAddImageItemViewModel(bitmap, context))
        imagesList.add(MyCarAddImageItemViewModel(true, context))

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

            if (imagesList.size > 0)
                imagesList.removeAt(imagesList.size - 1)
            for (i in uriArrayList.indices) {
                imagesList.add(
                    MyCarAddImageItemViewModel(
                        uriArrayList[i],
                        true,
                        context
                    )
                )
            }

            imagesList.add(MyCarAddImageItemViewModel(true, context))
        }
    }

    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        var bm: Bitmap? = null
        if (requestCode == APPConstants.REQUEST_IMAGE_CAPTURE ||
            requestCode == APPConstants.REQUEST_TAKE_PHOTO
            && resultCode == Activity.RESULT_OK
        ) {

            if (requestCode == APPConstants.REQUEST_IMAGE_CAPTURE) {
                if (resultCode == Activity.RESULT_OK && extras != null) {
                    if (imagesList.size > 10) {
                        showErrorBanner(getStringResource(R.string.str_my_car_add_photos_max_size_warning))
                        return
                    }
                    updateImagesList(extras.get("data") as Bitmap)
                    imagesAdapter?.notifyDataSetChanged()

                }
            } else if (requestCode == APPConstants.REQUEST_TAKE_PHOTO) {

                if (resultCode == Activity.RESULT_OK && extras != null) {

                    if (extras.containsKey(APPConstants.CLIP_DATA)) {

                        val mArrayUri = extras.getStringArrayList(APPConstants.CLIP_DATA)
                        if (mArrayUri != null) {
                            count = mArrayUri.size
                            if (count > 10 || count + imagesList.size > 5) {
                                showErrorBanner(getStringResource(R.string.str_order_now_photos_max_size_warning))
                                return
                            }

                            updateImagesList(mArrayUri)

                            imagesAdapter?.notifyDataSetChanged()
                        }


                    } else {

                        updateImagesList(extras?.get("data").toString())
                        imagesAdapter?.notifyDataSetChanged()

                    }

                }
            } else super.onViewResult(requestCode, resultCode, extras)


        }
    }


    private fun getCarImagesBase64(): ArrayList<String?> {
        val imagesBase64 = ArrayList<String?>()
        for (i in 0 until imagesList.size - 1) {
            val viewModel: MyCarAddImageItemViewModel = imagesList[i]
            val bitmap: Bitmap = viewModel.bitmapImage!!
            if (bitmap != null) {
                val base64: String = Utilities.bitmapToBase64(bitmap)!!
                imagesBase64.add(base64)
            }
        }
        return imagesBase64
    }

    fun orderCar() {

        orderRequest = OrderNowRequest()
        orderRequest?.carId = orderedCar?.id
        orderRequest?.branch_id = branchesList[branchesSelectedIndex.get() - 1].id
        orderRequest?.advancePayment = advancedPaymentText.get()?.toDouble()
        orderRequest?.paymentType = if (isCashOrder.get()) CASH else PREMIUM
        orderRequest?.needCarLicense = hasCarLisence.get()
        orderRequest?.deliveryPlace = deliveryPlacesNames[deliveryPlacesSelectedIndex.get() - 1]
        orderRequest?.deliveryPlace = deliveryPlacesNames[deliveryPlacesSelectedIndex.get() - 1]
        orderRequest?.powerOfAttorneyImages = getCarImagesBase64()
        orderRequest?.colorId = selectedColor
        orderRequest?.attorneyPersons = attorneyPersonsNames

        showLoading(true)
        appRepository.orderCar(
            orderRequest!!,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<Any?>?> {
                override fun onSuccess(result: BaseResponse<Any?>?) {

                    hideLoading()

                }

                override fun onError(throwable: ErrorDetails?) {
                    hideLoading()
                    showErrorBanner(throwable?.errorMsg)

                }
            })
    }
}