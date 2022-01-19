package com.gtera.ui.profile.messages.details

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.Message
import com.gtera.data.model.MessagesItem
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.base.ListOrientation
import com.gtera.ui.common.ClickListener
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.profile.edit.EditProfileFragment
import com.gtera.utils.APPConstants
import com.gtera.utils.APPConstants.MESSAGE_ID
import com.gtera.utils.APPConstants.MESSAGE_USER_IMAGE
import com.gtera.utils.Utilities
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class MessageDetailsViewModel @Inject constructor() : BaseViewModel<MessageDetailsNavigator>() {

    init {
        initMessagesView()
    }

    var isRefreshing = ObservableBoolean(false)
    var currentMessage = ObservableField("")
    var userImage: String? = null
    var senderImage: String? = null
    var messageId: Int? = null
    var selectedPhotoPicture = ObservableField("")
    var selectedPhotoPictureUri = ObservableField("")
    var selectedPhotoPictureBitmap = ObservableField<Bitmap>()
    private var selectedPhotoPictureBase64: String? = null

    //swipe
    var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    var messagesDetailsListOrientation: ListOrientation? = ListOrientation.ORIENTATION_VERTICAL

    var messagesDetailsListAdapter: BaseAdapter<MessageDetailsItemViewModel>? = null

    // adapter's lists
    protected var messagesDetailsList: ArrayList<MessageDetailsItemViewModel> =
        ArrayList<MessageDetailsItemViewModel>()


    override fun onViewCreated() {
        super.onViewCreated()

        userImage = dataExtras?.getString(MESSAGE_USER_IMAGE)
        messageId = dataExtras?.getInt(MESSAGE_ID)
        getConversationHistory()

    }

    private fun getConversationHistory() {

        showLoading(false)
        appRepository.getConversationHistory(
            messageId!!,
            lifecycleOwner,
            object :
                APICommunicatorListener<BaseResponse<Message?>?> {
                override fun onSuccess(result: BaseResponse<Message?>?) {
                    hideLoading()
                    hasData(result?.data?.messages?.size!!)
                    addMessages(result.data?.messages!!)
                    senderImage = result.data?.user?.profilePicture
                    isRefreshing.set(false)
                }

                override fun onError(throwable: ErrorDetails?) {
                    isRefreshing.set(false)
                    hideLoading()
                    hasData(0)
                    showErrorView(
                        throwable?.statusCode!!,
                        throwable.errorMsg,
                        object : ClickListener {
                            override fun onViewClicked() {
                                getConversationHistory()
                            }
                        })
                }

            })
    }


    protected fun initMessagesView() {

        messagesDetailsListAdapter = BaseAdapter(messagesDetailsList, object : ViewHolderInterface {


            override fun onViewClicked(position: Int, id: Int) {


//                        openView(AppScreenRoute.CAR_DETAILS, extras)


            }
        })

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            isRefreshing.set(true)
            getConversationHistory()
        }
    }

    protected fun addMessages(messages: List<MessagesItem?>) {
        if (Utilities.isNullList(messages)) return

        this.messagesDetailsList.clear()
        for (message in messages) {

            val carCompareItemViewModel = MessageDetailsItemViewModel(message!!, context, userImage, senderImage)
            this.messagesDetailsList.add(carCompareItemViewModel)
        }
        messagesDetailsListAdapter?.updateList(messagesDetailsList)
        messagesDetailsListAdapter?.notifyDataSetChanged()
    }


    fun sendMessage(){

    }


    fun showChoosePhotoDialog() {

        var cameraRequestPermissionLauncher =
            (lifecycleOwner as EditProfileFragment).baseActivity?.registerForActivityResult(
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


        var gallaryRequestPermissionLauncher =
            (lifecycleOwner as EditProfileFragment).baseActivity?.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    dispatchTakePictureIntent(APPConstants.REQUEST_TAKE_PHOTO, false)
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
            APPConstants.REQUEST_IMAGE_CAPTURE,
            APPConstants.REQUEST_TAKE_PHOTO,
            false
        )

    }

    override fun onViewResult(requestCode: Int, resultCode: Int, extras: Bundle?) {
        var bm: Bitmap? = null
        if (requestCode == APPConstants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            val imageBitmap = extras?.get("data") as Bitmap
            selectedPhotoPictureUri.set("")
            bm = extras?.get("data") as Bitmap
            selectedPhotoPictureBitmap.set(bm)


        } else if (requestCode == APPConstants.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {



            if (extras != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(
                        (lifecycleOwner as EditProfileFragment).baseActivity?.getApplicationContext()
                            ?.getContentResolver(), extras.get(APPConstants.INTENT_DATA) as Uri?
                    )
                } catch (e: IOException) {
                    showErrorBanner(e.message)
                }
            }


            selectedPhotoPictureBitmap.set(null)
            selectedPhotoPictureUri.set((extras?.get(APPConstants.INTENT_DATA) as Uri?).toString())

        }
        val bytes = ByteArrayOutputStream()
        bm!!.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val BYTE: ByteArray
        BYTE = bytes.toByteArray()
        selectedPhotoPictureBase64 = Base64.encodeToString(BYTE, Base64.DEFAULT)

    }
}