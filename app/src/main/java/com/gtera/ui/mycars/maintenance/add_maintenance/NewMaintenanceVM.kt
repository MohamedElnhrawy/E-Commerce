package com.gtera.ui.mycars.maintenance.add_maintenance

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.DatePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.ui.adapter.AutoCompleteAdapter
import com.gtera.ui.base.SearchActionListener
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject

class NewMaintenanceVM @Inject constructor() : BaseViewModel<NewMaintenainceNavigator>() {


    private var licenseFrontBase64: String? = null
    var licenseFrontImageAdded = ObservableField<Boolean>(false)


    private val myCalendar = Calendar.getInstance()
    var date = ObservableField("")
    var KM = ObservableField("")
    var totalCost = ObservableField("")
    var notes = ObservableField("")


    var searchListener: SearchActionListener? = null
    var searchTxt: ObservableField<String> = ObservableField("")
    var adapter: AutoCompleteAdapter? = null

    override fun onViewCreated() {
        super.onViewCreated()
        adapter = AutoCompleteAdapter(
            context,
            R.layout.auto_complete_item_layout
        )
    }


    fun onDateClick(){
        showDatePickerDialog(object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date.set(Utilities.formatDate(myCalendar.getTime()))
            }

        }, myCalendar,true,false)
    }

    fun searchUsers(text: String) {
        if (text.trim().length <= 2)
            return

    }
    override fun searchItemClick(position: Int) {

    }

    fun requestMaintenance(){

    }

    fun showChangePhotoDialog(cameraRequestCode: Int, galleryRequestCode: Int) {

        val cameraRequestPermissionLauncher =
            (lifecycleOwner as NewMaintenanceFragment).baseActivity?.registerForActivityResult(
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


        var gallaryRequestPermissionLauncher =
            (lifecycleOwner as NewMaintenanceFragment).baseActivity?.registerForActivityResult(
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
            gallaryRequestPermissionLauncher,
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


//            if(extras?.get("data") != null)
            bm = extras?.get("data") as Bitmap
            val bytes = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 90, bytes)
            val BYTE: ByteArray
            BYTE = bytes.toByteArray()

            if (requestCode == APPConstants.REQUEST_IMAGE_CAPTURE) {

                licenseFrontBase64 = Base64.encodeToString(BYTE, Base64.DEFAULT)
                licenseFrontImageAdded.set(true)
            }

        } else if (requestCode == APPConstants.REQUEST_TAKE_PHOTO ||
            requestCode == APPConstants.REQUEST_TAKE_SECOND_PHOTO &&
            resultCode == Activity.RESULT_OK
        ) {


            if (extras != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(
                        (lifecycleOwner as NewMaintenanceFragment).baseActivity?.getApplicationContext()
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
            }

        }


    }
}