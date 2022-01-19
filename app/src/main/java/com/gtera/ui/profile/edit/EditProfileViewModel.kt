package com.gtera.ui.profile.edit

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.widget.DatePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.City
import com.gtera.data.model.Governorate
import com.gtera.data.model.User
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.base.SpinnerInterface
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.utils.*
import com.gtera.utils.APPConstants.INTENT_DATA
import com.gtera.utils.APPConstants.REQUEST_IMAGE_CAPTURE
import com.gtera.utils.APPConstants.REQUEST_TAKE_PHOTO
import com.gtera.utils.Utilities.changeNumbersToEnglish
import com.gtera.utils.Utilities.formatNumbers
import com.gtera.utils.Utilities.reFormatDateEnglish
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class EditProfileViewModel @Inject constructor() : BaseViewModel<EditProfileNavigator>(),
    ViewHolderInterface {


    var attempRequest = 0
    private var selectedSortPos: Int = 0;
    private var profilePictureBase64: String? = null
    var placeHolder: ObservableField<Int> = ObservableField(R.drawable.ic_profile_info_placeholder)
    var loggedUser: User? = null
    var profilePicture = ObservableField("")
    var profilePictureUri = ObservableField("")
    var profilePictureBitmap = ObservableField<Bitmap>()
    var firstName = ObservableField("")
    var lastName = ObservableField("")
    var userMail = ObservableField("")
    var userAddress = ObservableField("")
    var phoneNumber = ObservableField("")
    var birthdate = ObservableField("")
    var gender = ObservableField(1)

    var firstNameStatus = ObservableBoolean(false)
    var lastNameStatus = ObservableBoolean(false)
    var userMailStatus = ObservableBoolean(false)
    var userAddressStatus = ObservableBoolean(false)
    var phoneNumberStatus = ObservableBoolean(false)
    var birthdateStatus = ObservableBoolean(false)
    var governoratSelectionStatus = ObservableBoolean(false)
    var citySelectionStatus = ObservableBoolean(false)

    var firstNameError = ObservableField("")
    var lastNameError = ObservableField("")
    var userMailError = ObservableField("")
    var userAddressError = ObservableField("")
    var phoneNumberError = ObservableField("")
    var governorateError = ObservableField("")
    var cityError = ObservableField("")

    private val myCalendar = Calendar.getInstance()

    protected var selectedCity: City? = null
    protected var selectedGovernorate: Governorate? = null
    val cities: ArrayList<City> = ArrayList()

    var isClickable = ObservableBoolean(false)

    protected var governoratesList: ArrayList<Governorate> = ArrayList<Governorate>()
    protected var governoratesCitiesList: ArrayList<City> = ArrayList<City>()

    var governoratesNames = ObservableArrayList<String>()
    var citiesNames = ObservableArrayList<String>()
    protected var governorateSelectedPos = 0
    protected var citySelectedPos: Int = 0

    var citySelectedIndex = ObservableInt(0)
    var governorateSelectedIndex = ObservableInt(0)

    var governoratesListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {

            if (position != APPConstants.NO_SELECTION && position != 0 && position < governoratesList.size && governorateSelectedPos != position)
                onGovernorateItemSelected(position)
        }
    }

    var cityListener: SpinnerInterface = object : SpinnerInterface {

        override fun onClick(position: Int) {
            if (position != APPConstants.NO_SELECTION && position != 0 && position < governoratesCitiesList.size && position != citySelectedPos
            ) {
                onCityItemSelected(position)
            }
        }
    }

    override fun onViewCreated() {
        super.onViewCreated()


        appRepository.getLoggedInUser(lifecycleOwner, object : APICommunicatorListener<User?> {
            override fun onSuccess(result: User?) {
                loggedUser = result

                initializeUserData()
            }

            override fun onError(throwable: ErrorDetails?) {

            }
        })

        getGovernorates()


    }

    private fun initializeUserData() {

        firstName.set(loggedUser?.firstName.let { it ?: "" })
        lastName.set(loggedUser?.lastName.let { it ?: "" })
        userMail.set(loggedUser?.email.let { it ?: "" })
        phoneNumber.set(loggedUser?.phoneNumber.let { formatNumbers(it, false) ?: "" })
        birthdate.set(loggedUser?.birthDate.let {
            if (it != null)
                Utilities.formatDate(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(it)) else ""
        }!!)
        gender.set(loggedUser?.gender.let { it })
        governorateSelectedIndex.set(loggedUser?.governorateId.let { it ?: 0 })
        citySelectedIndex.set(loggedUser?.cityId.let { it ?: 0 })
        gender.set(loggedUser?.gender.let { it ?: 0 })
        profilePicture.set(loggedUser?.profilePicture.let { it ?: "" })
    }

    protected fun onGovernorateItemSelected(position: Int) {
        governorateSelectedPos = position
        governorateSelectedIndex.set(position)
        updateCities()
    }

    protected fun onCityItemSelected(position: Int) {
        citySelectedPos = position
        citySelectedIndex.set(position)
        isClickable.set(true)
    }

    protected fun validInputFields(): Boolean {

        firstNameStatus.set(false)
        lastNameStatus.set(false)
        userMailStatus.set(false)
        userAddressStatus.set(false)
        phoneNumberStatus.set(false)
        birthdateStatus.set(false)

        val successMsg = getStringResource(R.string.looks_great)
        var isValid = true

        if (TextUtils.isEmpty(firstName.get()) ||
            Objects.requireNonNull(firstName.get())?.let { Utilities.isValidPersonName(it).not() }!!
        ) {
            firstNameError.set(
                resourceProvider.getString(

                    if (TextUtils.isEmpty(firstName.get())) R.string.field_is_required else if (Objects.requireNonNull<String>(
                            firstName.get()
                        ).length < 3
                    ) R.string.str_profile_edit_first_name_length_short else R.string.str_profile_edit_first_name_special_characters,
                    getStringResource(R.string.str_account_details_first_name)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(firstNameError.get())) {
            firstNameStatus.set(true)
            firstNameError.set(
                successMsg
            )
        }


        if (TextUtils.isEmpty(lastName.get()) ||
            Objects.requireNonNull(lastName.get())?.let { Utilities.isValidPersonName(it).not() }!!
        ) {
            lastNameError.set(
                resourceProvider.getString(

                    if (TextUtils.isEmpty(lastName.get())) R.string.field_is_required else if (Objects.requireNonNull<String>(
                            lastName.get()
                        ).length < 3
                    ) R.string.str_profile_edit_last_name_length_short else R.string.str_profile_edit_last_name_special_characters,
                    getStringResource(R.string.str_account_details_last_name)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(lastNameError.get())) {
            lastNameStatus.set(true)
            lastNameError.set(
                successMsg
            )
        }

        if (TextUtils.isEmpty(userMail.get()) ||
            Objects.requireNonNull(userMail.get())?.let { Utilities.isValidEmail(it).not() }!!
        ) {
            userMailError.set(
                resourceProvider.getString(
                    if (TextUtils.isEmpty(userMail.get())) R.string.field_is_required else R.string.str_edit_account_email_validation,
                    getStringResource(R.string.str_account_details_email)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(userMailError.get())) {
            userMailStatus.set(true)
            userMailError.set(
                successMsg
            )
        }


        if (TextUtils.isEmpty(phoneNumber.get()) ||
            Objects.requireNonNull(phoneNumber.get())
                ?.let { Utilities.isValidPhoneNumber(it).not() }!!
        ) {
            phoneNumberError.set(
                resourceProvider.getString(
                    if (TextUtils.isEmpty(phoneNumber.get())) R.string.field_is_required else if (Objects.requireNonNull<String>(
                            phoneNumber.get()
                        ).length < 3
                    ) R.string.str_edit_account_phone_number_validation else R.string.str_profile_edit_mobile_special_characters,
                    getStringResource(R.string.str_account_details_phone_number)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(phoneNumberError.get())) {
            phoneNumberStatus.set(true)
            phoneNumberError.set(
                successMsg
            )
        }


        if (TextUtils.isEmpty(userAddress.get()) ||
            Objects.requireNonNull(userAddress.get())?.let { Utilities.isValidPersonName(it).not() }!!
        ) {
            userAddressError.set(
                resourceProvider.getString(

                    if (TextUtils.isEmpty(userAddress.get())) R.string.field_is_required else if (Objects.requireNonNull<String>(
                            userAddress.get()
                        ).length < 3
                    ) R.string.str_profile_edit_last_name_length_short else R.string.str_profile_edit_last_name_special_characters,
                    getStringResource(R.string.str_account_details_address)
                )
            )
            isValid = false
        } else if (!TextUtils.isEmpty(userAddressError.get())) {
            userAddressStatus.set(true)
            userAddressError.set(
                successMsg
            )
        }

//        if (governorateSelectedIndex.get() <= 0) {
//            governoratSelectionStatus.set(true)
//            governorateError.set(getStringResource(R.string.str_profile_edit_governorate_validation))
//            isValid = false
//        } else {
//            governoratSelectionStatus.set(false)
//            governorateError.set("")
//        }

//        if (citySelectedIndex.get() <= 0) {
//            citySelectionStatus.set(true)
//            cityError.set(getStringResource(R.string.str_profile_edit_city_validation))
//            isValid = false
//        } else {
//            citySelectionStatus.set(false)
//            cityError.set("")
//        }



        return isValid
    }

    fun editBtnClick() {

        if (isLoading.get()) return

        if (validInputFields()) {


            isLoading.set(true)

            val user = User(
                Objects.requireNonNull(userMail.get()!!),
                Objects.requireNonNull(firstName.get()!!),
                Objects.requireNonNull(lastName.get()),
                Objects.requireNonNull(userAddress.get()),
                Objects.requireNonNull(phoneNumber.get()?.let { changeNumbersToEnglish(it) }),
                Objects.requireNonNull(birthdate.get()?.let { reFormatDateEnglish(it) }),
                Objects.requireNonNull(gender.get())
            )

            if (profilePictureBase64 != null && profilePictureBase64?.isEmpty()!!.not())
                user.profilePicture = profilePictureBase64

            if (governorateSelectedPos > 0) user.governorateId = governoratesList.get(governorateSelectedPos-1).id
            if (citySelectedPos > 0) user.cityId = cities.get(citySelectedPos-1).id


            appRepository.updateProfile(user, lifecycleOwner,
                object : APICommunicatorListener<BaseResponse<User?>?> {

                    override fun onSuccess(result: BaseResponse<User?>?) {

                        showSuccessBanner(getStringResource(R.string.str_updated_successfully))
                        isLoading.set(false)
                        goBack()


                    }

                    override fun onError(throwable: ErrorDetails?) {
                        isLoading.set(false)

                        showErrorBanner(throwable?.errorMsg)


                    }

                })

        }
    }

    fun initializeLocationDataSpinners() {

        governoratesNames.add(0, getStringResource(R.string.str_account_details_governorate))
        var loggedUserGovernorateId: Int =
            if (loggedUser?.governorateId == null) 0 else loggedUser?.governorateId as Int
        for ((i, governorate) in governoratesList.withIndex()) {

            governoratesNames.add(governorate.name)
            if (loggedUserGovernorateId!! > 0 && loggedUserGovernorateId == governorate.id) {

                governorateSelectedIndex.set(i+1)
            }


        }

        if (governorateSelectedIndex.get() > 0) {

            var loggedUserCityId: Int =
                if (loggedUser?.cityId == null) 0 else loggedUser?.cityId as Int
            for ((k, city) in governoratesCitiesList.withIndex()) {
                if (loggedUserCityId!! > 0 && loggedUserCityId == city.id)
                    citySelectedIndex.set(k)
            }


        } else {
            citiesNames.add(0, getStringResource(R.string.str_account_details_city))
        }


    }

    protected fun updateCities() {


        citiesNames.clear()
        cities.clear()

        if (governorateSelectedPos != 0) {

            citiesNames.add(0, getStringResource(R.string.str_account_details_city))

            for (city in governoratesCitiesList) {

                if (governoratesList.get(governorateSelectedPos).id == city.governorateId) {
                    cities.add(city)
                    citiesNames.add(city.name)
                }
            }


            if (governorateSelectedIndex.get() > 0) {

                var loggedUserCityId: Int =
                    if (loggedUser?.cityId == null) 0 else loggedUser?.cityId as Int
                for ((k, city) in governoratesCitiesList.withIndex()) {
                    if (loggedUserCityId!! > 0 && loggedUserCityId == city.id)
                        citySelectedIndex.set(k+1)
                }


            } else {
                citiesNames.add(0, getStringResource(R.string.str_account_details_city))
            }
        }
    }

    fun onBirthDateClick() {

        showDatePickerDialog(object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                birthdate.set(Utilities.formatDate(myCalendar.getTime()))
            }

        }, myCalendar,false,true)
    }

    override fun onViewClicked(position: Int, id: Int) {
    }


    fun showChangePhotoDialog() {

        var cameraRequestPermissionLauncher =
            (lifecycleOwner as EditProfileFragment).baseActivity?.registerForActivityResult(
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


        var gallaryRequestPermissionLauncher =
            (lifecycleOwner as EditProfileFragment).baseActivity?.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    dispatchTakePictureIntent(REQUEST_TAKE_PHOTO, false)
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
            gallaryRequestPermissionLauncher,
            REQUEST_IMAGE_CAPTURE,
            REQUEST_TAKE_PHOTO,
            false
        )



    }





    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        var bm: Bitmap? = null
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = extras?.get("data") as Bitmap
            profilePictureUri.set("")
            bm = extras?.get("data") as Bitmap
            profilePictureBitmap.set(bm)


        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {



            if (extras != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(
                        (lifecycleOwner as EditProfileFragment).baseActivity?.getApplicationContext()
                            ?.getContentResolver(), extras.get(INTENT_DATA) as Uri?
                    )
                } catch (e: IOException) {
                    showErrorBanner(e.message)
                }
            }


            profilePictureBitmap.set(null)
            profilePictureUri.set((extras?.get(INTENT_DATA) as Uri?).toString())

        }
        val bytes = ByteArrayOutputStream()
        bm!!.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val BYTE: ByteArray
        BYTE = bytes.toByteArray()
        profilePictureBase64 = Base64.encodeToString(BYTE, Base64.DEFAULT)

    }

    fun getGovernorates() {

        appRepository.getGovernrates(
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<List<Governorate?>?>?> {
                override fun onSuccess(result: BaseResponse<List<Governorate?>?>?) {

                    governoratesList.addAll(result?.data as ArrayList<Governorate>)
                    initializeLocationDataSpinners()
                    getCities()
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
                            getGovernorates()
                    }else
                        showErrorBanner(throwable.errorMsg)
                }



            })

    }

    fun getCities(){
        appRepository.getCities(
            lifecycleOwner,
            object : APICommunicatorListener<BaseResponse<List<City?>?>?> {

                override fun onSuccess(result: BaseResponse<List<City?>?>?) {
                    governoratesCitiesList.addAll(result?.data as ArrayList<City>)
                    updateCities()
                }

                override fun onError(throwable: ErrorDetails?) {
                    if (throwable?.statusCode != STATUS_CODE_UNAUTHORIZED_ERROR ||
                        throwable.statusCode != STATUS_CODE_SERVER_ERROR ||
                        throwable.statusCode != STATUS_CODE_NETWORK_ERROR ||
                        throwable.statusCode != STATUS_CODE_TIMEOUT_ERROR
                    ) {
                        attempRequest=0
                        attempRequest++
                        if (attempRequest > 2) {
                            showErrorBanner(throwable?.errorMsg)
                            return
                        } else
                            getCities()
                    }else
                        showErrorBanner(throwable.errorMsg)
                }


            })
    }

    fun changeGender(gender: Int) {

        this.gender.set(gender)
    }


}