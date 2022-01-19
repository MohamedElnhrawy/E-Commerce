package com.gtera.ui.mycars.insurance.carinsurancerenewal

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Branche
import com.gtera.data.model.requests.CarRenewalRequest
import com.gtera.data.model.requests.FavoriteResponse
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.base.SpinnerInterface
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.dialog.confirmation.ConfirmationDialogNavigator
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RequestCarInsuranceViewModel @Inject constructor() :
    BaseViewModel<RequestCarInsuranceNavigator>(),
    ViewHolderInterface {


    private var licenseFrontBase64: String? = null
    private var licenseBackBase64: String? = null
    var licenseFrontImageAdded = ObservableField<Boolean>(false)
    var licenseBackImageAdded = ObservableField<Boolean>(false)

    var name = ObservableField("")
    var chassisNumber = ObservableField("")
    var mobile = ObservableField("")

    var nameStatus = ObservableBoolean(false)
    var chassisNumberStatus = ObservableBoolean(false)
    var mobileStatus = ObservableBoolean(false)
    var branchesSelectionStatus = ObservableBoolean(false)

    var nameError = ObservableField("")
    var chassisNumberError = ObservableField("")
    var mobileError = ObservableField("")
    var branchesError = ObservableField("")

    protected var branchesList: ArrayList<Branche> = ArrayList()
    var branchesNames = ObservableArrayList<String>()
    protected var branchesSelectedPos = 0
    var branchesSelectedIndex = ObservableInt(0)

    var branchesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < branchesList.size && branchesSelectedPos != position)
                branchesSelectedPos = position
            branchesSelectedIndex.set(position)
        }
    }


    override fun onViewCreated() {
        super.onViewCreated()

        getBranches()


    }


    protected fun validInputFields(): Boolean {

        nameStatus.set(false)
        chassisNumberStatus.set(false)
        mobileStatus.set(false)

        val successMsg = getStringResource(R.string.looks_great)
        var isValid = true

        if (TextUtils.isEmpty(name.get()) ||
            Objects.requireNonNull<String>(
                name.get()
            ).length < 6 ||
            Objects.requireNonNull(name.get())?.let { Utilities.isValidPersonName(it).not() }!!
        ) {
            nameError.set(
                resourceProvider.getString(

                    if (TextUtils.isEmpty(name.get())) R.string.field_is_required else if (Objects.requireNonNull<String>(
                            name.get()
                        ).length < 6
                    ) R.string.str_car_insurance_name_length_short else R.string.str_car_insurance_name_special_characters,
                    getStringResource(R.string.str_car_insurance_name)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(nameError.get())) {
            nameStatus.set(true)
            nameError.set(
                successMsg
            )
        }

        if (TextUtils.isEmpty(mobile.get()) ||
            Objects.requireNonNull(mobile.get())?.let { Utilities.isValidPhoneNumber(it).not() }!!
        ) {
            mobileError.set(
                resourceProvider.getString(
                    if (TextUtils.isEmpty(mobile.get())) R.string.field_is_required else R.string.str_edit_account_phone_number_validation,
                    getStringResource(R.string.str_account_details_phone_number)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(mobileError.get())) {
            mobileStatus.set(true)
            mobileError.set(
                successMsg
            )
        }

        if (TextUtils.isEmpty(chassisNumber.get()) ||
            Objects.requireNonNull(chassisNumber.get())
                ?.let { Utilities.isValidChassisNumber(it).not() }!!
        ) {
            chassisNumberError.set(
                resourceProvider.getString(

                    if (TextUtils.isEmpty(chassisNumber.get())) R.string.field_is_required else if (Objects.requireNonNull<String>(
                            chassisNumber.get()
                        ).length < 14 
                    ) R.string.str_car_insurance_chassis_number_lengh_short else R.string.str_car_insurance_chassis_number_special_characters,
                    getStringResource(R.string.str_car_insurance_chassis_number)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(chassisNumberError.get())) {
            chassisNumberStatus.set(true)
            chassisNumberError.set(
                successMsg
            )
        }




        if (branchesSelectedIndex.get() <= 0) {
            branchesSelectionStatus.set(true)
            branchesError.set(getStringResource(R.string.str_car_insurance_branch_validation))
            isValid = false
        } else {
            branchesSelectionStatus.set(false)
            branchesError.set("")
        }

        if (licenseFrontBase64 == null || licenseFrontBase64!!.isEmpty() || licenseBackBase64 == null || licenseBackBase64!!.isEmpty()) {
            isValid = false
            showErrorBanner(getStringResource(R.string.str_inurance_request_license_image_validation))
        }


        return isValid
    }

    fun requestInsuranceBtnClick() {

        if (isLoading.get()) return

        if (validInputFields()) {


            isLoading.set(true)

            val carRenewalRequest = CarRenewalRequest(
                Objects.requireNonNull(name.get()!!),
                Objects.requireNonNull(mobile.get()),
                Objects.requireNonNull(chassisNumber.get()),
                licenseFrontBase64,
                licenseBackBase64,
                null,
                null,
                null,
                null,
                Objects.requireNonNull(branchesList.get(branchesSelectedPos - 1).id)
            )



            appRepository.requestCarRenewalInsurance(carRenewalRequest, lifecycleOwner,
                object : APICommunicatorListener<FavoriteResponse?> {

                    override fun onSuccess(result: FavoriteResponse?) {

                        isLoading.set(false)
                        showInsuranceRequestConfirmationDialog(
                            getDrawableResources(R.mipmap.ic_insurance_request_done),
                            true,
                            getShowInsuranceDialogConfirmationNavigator()!!,
                            getDrawableResources(R.drawable.rounded_top_corners_red)
                        )

                    }

                    override fun onError(throwable: ErrorDetails?) {
                        isLoading.set(false)

                        showErrorBanner(throwable?.errorMsg)

                    }

                })
        }
    }

    fun initializeBranchesSpinners() {

        branchesNames.add(0, getStringResource(R.string.str_car_insurance_branch))

        for ((i, branche) in branchesList.withIndex()) {

            branchesNames.add(branche.name)

        }
    }


    override fun onViewClicked(position: Int, id: Int) {
    }


    fun showChangePhotoDialog(cameraRequestCode: Int, galleryRequestCode: Int) {

        var cameraRequestPermissionLauncher =
            (lifecycleOwner as RequestCarInsuranceFragment).baseActivity?.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    dispatchCapturePictureIntent(cameraRequestCode)
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }


        var galleryRequestPermissionLauncher =
            (lifecycleOwner as RequestCarInsuranceFragment).baseActivity?.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    dispatchTakePictureIntent(galleryRequestCode, false)
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
            cameraRequestCode,
            galleryRequestCode,
            false
        )

    }


    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        var bm: Bitmap? = null
        if (requestCode == APPConstants.REQUEST_IMAGE_CAPTURE ||
            requestCode == APPConstants.REQUEST_IMAGE_SECOND_CAPTURE
            && resultCode == Activity.RESULT_OK
        ) {


            bm = extras?.get("data") as Bitmap
            val bytes = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 90, bytes)
            val BYTE: ByteArray
            BYTE = bytes.toByteArray()

            if (requestCode == APPConstants.REQUEST_IMAGE_CAPTURE) {

                licenseFrontBase64 = Base64.encodeToString(BYTE, Base64.DEFAULT)
                licenseFrontImageAdded.set(true)
            } else {

                licenseBackBase64 = Base64.encodeToString(BYTE, Base64.DEFAULT)
                licenseBackImageAdded.set(true)
            }


        } else if (requestCode == APPConstants.REQUEST_TAKE_PHOTO ||
            requestCode == APPConstants.REQUEST_TAKE_SECOND_PHOTO &&
            resultCode == Activity.RESULT_OK
        ) {


            if (extras != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(
                        (lifecycleOwner as RequestCarInsuranceFragment).baseActivity?.getApplicationContext()
                            ?.getContentResolver(), extras.get(APPConstants.INTENT_DATA) as Uri?
                    )
                } catch (e: IOException) {
                    showErrorBanner(e.message)
                }
            }

            val bytes = ByteArrayOutputStream()
            bm!!.compress(Bitmap.CompressFormat.PNG, 90, bytes)
            val BYTE: ByteArray
            BYTE = bytes.toByteArray()

            if (requestCode == APPConstants.REQUEST_TAKE_PHOTO) {
                licenseFrontBase64 = Base64.encodeToString(BYTE, Base64.DEFAULT)
                licenseFrontImageAdded.set(true)
            } else {
                licenseBackBase64 = Base64.encodeToString(BYTE, Base64.DEFAULT)
                licenseBackImageAdded.set(true)
            }

        }


    }


    fun getBranches() {

        appRepository.getBranches(
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<List<Branche?>?>?> {
                override fun onSuccess(result: BaseResponse<List<Branche?>?>?) {

                    branchesList.addAll(result?.data as ArrayList<Branche>)
                    initializeBranchesSpinners()
                }

                override fun onError(throwable: ErrorDetails?) {

                }


            })

    }

    fun onRemoveLicenseFrontImage() {
        licenseFrontBase64 = null
        licenseFrontImageAdded.set(false)
    }


    fun onRmoveLicenseBackImage() {
        licenseBackBase64 = null
        licenseBackImageAdded.set(false)
    }


    private fun getShowInsuranceDialogConfirmationNavigator(): ConfirmationDialogNavigator? {

        return object : ConfirmationDialogNavigator {
            override fun onYesClicked() {

            }

            override fun onNoClicked() {
                dismissConfirmationDialog()
                goBack()
            }
        }
    }
}