package com.gtera.ui.cardetials

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.*
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.util.Log
import androidx.core.content.FileProvider
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.AttributeGroup
import com.gtera.data.model.Car
import com.gtera.data.model.Compare
import com.gtera.data.model.Offer
import com.gtera.data.model.requests.AddRemoveToCompareRequest
import com.gtera.data.model.requests.FavoritesRequest
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.CarMediaType
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.cardetials.attributes.CarAttributesItemListViewModel
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.home.viewmodels.CarImageItemViewModel
import com.gtera.ui.utils.AppScreenRoute
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.ORDER_NOW_CAR
import com.gtera.utils.APPConstants.ORDER_NOW_CAR_MONTHLY_PAYMENT
import com.gtera.utils.APPConstants.ORDER_NOW_CAR_NAME
import com.gtera.utils.APPConstants.ORDER_NOW_CAR_TYPE
import com.gtera.utils.APPConstants.REQUEST_CODE_FAVORITE
import com.gtera.utils.APPConstants.REQUEST_CODE_STORAGE_PERMISSION
import com.gtera.utils.Logger
import com.gtera.utils.Utilities
import java.io.File
import javax.inject.Inject


open class CarDetailsViewModel @Inject constructor() : BaseViewModel<CarDetailsNavigator>() {

    init {
        initCarImagesView()
        initCarAttributesGroupsList()
    }
    var downloadReference: Long = 0
    lateinit var downloadManager:DownloadManager
    val completBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            isLoading.set(false)

            val action = intent?.action
            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                val query =  DownloadManager.Query()
                query.setFilterById(downloadReference)
                val c = downloadManager.query(query)
                if(c.moveToFirst()) {
                    var columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if(DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                        var uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        catalogueFilepath.set(uriString)

                        if (uriString.substring(0, 7).trim() == "file://".trim()) {
                            uriString =  uriString.substring(7)
                            catalogueFilepath.set(uriString)
                        }

                        Log.e("tt",uriString)
                    }
                }
            }
        }

        }



    var carType = ObservableField("")
    var discountTag = ObservableField("")
    var car: Car? = null
    var carId: Int? = null
    var offerId: Int? = null
    var carImage = ObservableField("")
    var carName = ObservableField("  ")
    var carPrice = ObservableField("")
    var advanceCoast = ObservableField("")
    var monthlyPayment = ObservableField("")

    var carSpecs = ObservableField("")
    var carliters = ObservableField("")
    var carKM = ObservableField("")
    var carNumSeats = ObservableField("")
    var carNumDoors = ObservableField("")
    var catalogueName = ObservableField("")
    var catalogueFilepath = ObservableField("")
    var isFavoriteCar = ObservableField(false)
    var hasCatalogue = ObservableField(false)

    var addCarToCompare =
        object : ClickListener {
            override fun onViewClicked() {
                addToCompare()
            }
        }

    private fun addToCompare() {
        var carsIds = ArrayList<Int>()
        carsIds.add(carId!!)
        var addToCompareRequest = AddRemoveToCompareRequest()
        addToCompareRequest.ids = carsIds
        appRepository.addCarCompare(
            addToCompareRequest,
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<Compare?>?> {

                override fun onSuccess(result: BaseResponse<Compare?>?) {

                    showSuccessBanner(getStringResource(R.string.str_car_added_to_compare_successfully))
                }

                override fun onError(throwable: ErrorDetails?) {
                    showErrorBanner(throwable?.errorMsg)
                }
            })
    }

    var attributesGroupListAdapter: BaseAdapter<CarAttributesItemListViewModel>? = null

    var isRefreshing = ObservableBoolean(false)


    var carImagesOrientation: ListOrientation? = ListOrientation.ORIENTATION_HORIZONTAL

    var carImagesAdapter: BaseAdapter<CarImageItemViewModel>? = null

    // adapter's lists
    protected var carImagesList: ArrayList<CarImageItemViewModel> =
        ArrayList<CarImageItemViewModel>()
    protected var attributesGroupList: ArrayList<CarAttributesItemListViewModel> =
        ArrayList<CarAttributesItemListViewModel>()


    override fun onViewCreated() {
        super.onViewCreated()
        setToolbarSecondaryActionEnabled(true)
        setToolbarSecondaryActionField(R.drawable.ic_compare, addCarToCompare)
        carId = dataExtras?.getInt(APPConstants.EXTRAS_KEY_CAR_ID)
        if (dataExtras?.containsKey(APPConstants.EXTRAS_KEY_CAR_TYPE)!!) {
            carType.set(dataExtras?.getString(APPConstants.EXTRAS_KEY_CAR_TYPE))
            when (carType.get()) {
                APPConstants.SLIDER_TYPE_CAR -> {
                    getCar(carId!!)
                }
                APPConstants.SLIDER_TYPE_OFFER -> {
                    offerId = dataExtras?.getInt(APPConstants.EXTRAS_KEY_OFFER_ID)
                    getOfferById(offerId)
                }
            }
        } else
            getCar(carId!!)


        navigator!!.context()?.registerReceiver(
            completBroadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }


    override fun onViewDestroyed() {
        super.onViewDestroyed()
        navigator!!.context()?.unregisterReceiver(completBroadcastReceiver)
    }

    private fun getOfferById(id: Int?) {
        id?.let {
            showLoading(true)
            appRepository.getOfferById(
                it,
                lifecycleOwner,
                object : APICommunicatorListener<BaseResponse<Offer?>> {
                    override fun onSuccess(result: BaseResponse<Offer?>) {
                        hideLoading()

                        monthlyPayment.set(
                            resourceProvider.getString(
                                R.string.str_egp,
                                result.data?.monthlyPayment
                            )
                        )
                        advanceCoast.set(
                            resourceProvider.getString(
                                R.string.str_advance_of
                            ) + resourceProvider.getString(
                                R.string.str_egp, result.data?.advance
                            )
                        )


                        result.data?.let {
                            discountTag.set(it.discount)
                            it.car?.let { it1 -> viewCar(it1) }
                        }
                        result.data?.let { car = it.car }
                        isRefreshing.set(false)
                    }

                    override fun onError(throwable: ErrorDetails?) {
                        hideLoading()
                        isRefreshing.set(false)
                        showErrorView(
                            throwable?.statusCode!!,
                            throwable.errorMsg,
                            object : ClickListener {
                                override fun onViewClicked() {
                                    getOfferById(id)
                                }
                            })
                    }

                })
        }
    }

    private fun getCar(carId: Int) {

        appRepository.getCar(
            carId,
            lifecycleOwner,
            object :
                APICommunicatorListener<BaseResponse<Car?>?> {
                override fun onSuccess(result: BaseResponse<Car?>?) {
                    hideLoading()

                    result?.data?.let { viewCar(it) }
                    result?.data?.let { car = it }
                    isRefreshing.set(false)
                }

                override fun onError(throwable: ErrorDetails?) {
                    isRefreshing.set(false)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getCar(carId)
                            }
                        })
                }


            })
    }

    private fun viewCar(car: Car) {

        carImage.set(car.images?.get(0))
        car.images?.let { addCarImages(it) }
        car.attributes?.let { addCarAttributesGroups(it) }
        carName.set(car.name.let { if (it != null) it + " | " else "" } + car.brand?.name.let { if (it != null) it + " | " else "" } + car.manufactureYear.let { if (it != null) it else "" })
        if (carType.get().equals(APPConstants.SLIDER_TYPE_OFFER))
            carPrice.set(monthlyPayment.get())
        else
            car.priceFrom.let {
                carPrice.set(
                    resourceProvider.getString(
                        R.string.str_egp, car.priceFrom
                    )
                )
            }
        car.acceleration.let {
            carKM.set(
                resourceProvider.getString(
                    R.string.str_KM,
                    car.acceleration
                )
            )
        }
        car.averageFuelConsumbtion.let {
            carliters.set(
                resourceProvider.getString(
                    R.string.str_Liter, car.averageFuelConsumbtion
                )
            )
        }
        car.numberOfSeats.let {
            carNumSeats.set(
                resourceProvider.getString(
                    R.string.str_seats,
                    car.numberOfSeats.toString()
                )
            )
        }
        car.numberOfDoors.let {
            carNumDoors.set(
                resourceProvider.getString(
                    R.string.str_doors,
                    car.numberOfDoors.toString()
                )
            )
        }
        isFavoriteCar.set(car.isFavorite)

        carSpecs.set(car.description)

        car.catalogue?.let {
            hasCatalogue.set(true)
            catalogueName.set(it.name)

        }

    }


    protected fun initCarImagesView() {
        carImagesAdapter = BaseAdapter(carImagesList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {


            }
        })
    }


    private fun initCarAttributesGroupsList() {
        attributesGroupListAdapter =
            BaseAdapter(attributesGroupList, object : ViewHolderInterface {


                override fun onViewClicked(position: Int, id: Int) {

                    if (id == R.id.iv_budgets_arrow) {
                        attributesGroupList.get(position).isexpanded.get()?.not()
                    }

                }
            })
    }

    protected fun addCarImages(List: List<String?>) {
        if (Utilities.isNullList(List)) return

        this.carImagesList.clear()
        car?.videoUrl?.let {
            this.carImagesList.add(
                CarImageItemViewModel(
                    car?.videoUrl,
                    CarMediaType.CAR_VIDEO
                )
            )
        }
        for (image in List) {
            val carIImagetemViewModel = CarImageItemViewModel(
                image, CarMediaType.CAR_IMAGE
            )
            this.carImagesList.add(carIImagetemViewModel)
        }
        carImagesAdapter?.updateList(carImagesList)
        carImagesAdapter?.notifyDataSetChanged()
    }


    protected fun addCarAttributesGroups(List: List<AttributeGroup?>) {
        if (Utilities.isNullList(List)) return

        this.attributesGroupList.clear()

        for (attributeCategory in List) {
            val carAttributesItemListViewModel = CarAttributesItemListViewModel(
                attributeCategory!!, false
            )
            this.attributesGroupList.add(carAttributesItemListViewModel)
        }
        attributesGroupListAdapter?.updateList(attributesGroupList)
        attributesGroupListAdapter?.notifyDataSetChanged()
    }


    fun onFavoriteClick() {

        if (appRepository.isUserLoggedIn()) {
            car?.isFavorite = isFavoriteCar.get()?.not()!!
            isFavoriteCar.set(isFavoriteCar.get()?.not())
        }

        addOrRemoveFavorite(
            FavoritesRequest(
                arrayListOf(car?.id!!),
                APPConstants.FAVORITE_CAR_TYPE
            ), getGoToSignInConfirmationNavigator()!!, isFavoriteCar.get()!!
        )


    }

    fun onShareClicked() {
        launchShareIntent(car?.name, car?.shareLink)
    }



    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        super.onViewResult(requestCode, resultCode, extras)
        if (requestCode == REQUEST_CODE_FAVORITE) {
            if (resultCode == Activity.RESULT_OK && extras != null) {
                onFavoriteClick()
            }
        } else if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!!) {
                downloadPDF()
            }
        }
    }

    fun orderNow() {


        val extras = Bundle()
        extras?.putSerializable(
            ORDER_NOW_CAR, car
        )

        extras?.putString(
            ORDER_NOW_CAR_TYPE, carType.get()
        )

        extras?.putString(
            ORDER_NOW_CAR_NAME,
            car?.name.let { if (it != null) it else "" } + car?.brand?.name.let { if (it != null) it else "" }
        )
        extras?.putString(
            ORDER_NOW_CAR_MONTHLY_PAYMENT, monthlyPayment.get()
        )

        openView(AppScreenRoute.ORDER_NOW, extras)
    }


    fun checkStoragePermissions() {
        return if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!!) {
            downloadPDF()
        } else {

            requestPermissionsSafely(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), APPConstants.REQUEST_CODE_STORAGE_PERMISSION
            )

        }
    }



    @SuppressLint("MissingSuperCall")
    override fun onRequestResultsReady(
        requestCode: Int,
        permissions: Array<String?>?,
        grantResults: IntArray?
    ) {
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults!!.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!!) {
                    downloadPDF()
                }
            }
        }
    }

    fun startDownloadPDF() {
        if (catalogueFilepath.get().isNullOrEmpty())
            checkStoragePermissions()
        else
            openDownloadesFile()
    }

    fun openDownloadesFile() {
        val file =  File(catalogueFilepath.get())
        val path = navigator!!.context()?.let { FileProvider.getUriForFile(it,it.packageName+ ".provider",file) }
        val pdfOpenintent = Intent(Intent.ACTION_VIEW)
        pdfOpenintent.setDataAndType(path, "application/pdf")
        pdfOpenintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            navigator!!.context()?.startActivity(pdfOpenintent)
        } catch (e: ActivityNotFoundException) {
            Logger.instance()?.e("error", e.stackTrace, false)
            Logger.instance()?.e("error", e.localizedMessage, false)
        }
    }

    fun downloadPDF() {

        isLoading.set(true)
        hasCatalogue.get()?.let {
            if (it) {
                val reference = car?.catalogue?.url?.let { it1 -> startDownload(it1) }
            }
        }
    }


    fun startDownload(url: String): Long {
        // Create request for android download manager
         downloadManager = (navigator!!.context()?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?)!!
        val request = DownloadManager.Request(Uri.parse(url))
        //Setting title of request
        request.setTitle(getStringFromResources(R.string.str_downloading))
        //Setting description of request
        request.setDescription(getStringFromResources(R.string.str_downloading) + url.substringAfterLast("/"))
        //Set the local destination for the downloaded file to a path
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, url.substringAfterLast("/"))
            .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresCharging(false)// Set if charging is required to begin the download
        }
        request.setAllowedOverMetered(true)// Set if download is allowed on Mobile network
        request.setAllowedOverRoaming(true)// Set if download is allowed on roaming network
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setMimeType("application/pdf")
        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request)
        return downloadReference
    }




}

