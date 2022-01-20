package com.gtera.ui.profile

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.User
import com.gtera.data.model.response.BaseResponse
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.dialog.confirmation.ConfirmationDialogNavigator
import com.gtera.ui.splash.SplashActivity
import com.gtera.ui.utils.AppScreenRoute.*
import java.util.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : BaseViewModel<ProfileNavigator>(),
    ViewHolderInterface {


    var loggedUser: User? = User()
    var userName: ObservableField<String> = ObservableField("")
    var profileImage: ObservableField<String> = ObservableField("")
    var placeHolder: ObservableField<Int> = ObservableField(R.drawable.ic_profile_info_placeholder)
    var isLoggedIn: ObservableBoolean = ObservableBoolean(false)
    var adapter: BaseAdapter<ProfileItemViewModel>? = null
    private val list =
        ArrayList<ProfileItemViewModel>()

    init {
        adapter = BaseAdapter(list, this)
    }

    constructor(activityClass: Class<*>?) : this() {
        adapter = BaseAdapter(list, this)
    }

    override fun onViewCreated() {
        super.onViewCreated()

        getItemsList()
    }

    override fun onViewRecreated() {
        super.onViewRecreated()
        getItemsList()
    }


    private fun getItemsList() {
            drawProfileList()
    }

    private fun drawProfileList() {
        list.clear()

        if (appRepository.isUserLoggedIn()) {
            list.add(
                ProfileItemViewModel(
                    R.drawable.ic_messages,
                    R.string.cart,
                    resourceProvider,
                    true,
                   0
                )
            )

//            list.add(
//                ProfileItemViewModel(
//                    R.drawable.ic_language,
//                    R.string.str_profile_language,
//                    resourceProvider,
//                    true,
//                    0
//                )
//            )

            list.add(
                ProfileItemViewModel(
                    R.drawable.ic_log_out,
                    R.string.str_profile_logout,
                    resourceProvider,
                    false,
                    0
                )
            )
        } else {
//            list.add(
//                ProfileItemViewModel(
//                    R.drawable.ic_language,
//                    R.string.str_profile_language,
//                    resourceProvider,
//                    true,
//                    0
//                )
//            )
            list.add(
                ProfileItemViewModel(
                    R.drawable.ic_sign_in,
                    R.string.str_profile_sign_in,
                    resourceProvider,
                    true,
                    0
                )
            )
        }


        adapter!!.notifyDataSetChanged()
    }


    override fun onViewClicked(position: Int, id: Int) {
        val titleResource = list[position].getTitleResource()
        if (titleResource == R.string.str_profile_sign_in)
            openView(SIGN_IN_SCREEN, null)
          else if (titleResource == R.string.str_profile_logout) {

            showConfirmationDialog(
                getDrawableResources(R.drawable.ic_logout),
                getStringResource(R.string.str_profile_log_out_confirmation_message),
                getSignOutConfirmationNavigator()!!
            )

        } else if (titleResource == R.string.str_profile_language)
            openFragment(R.id.action_change_language, null)
         else if (titleResource == R.string.cart)
            openView(CART_SCREEN, null)

    }


    private fun getSignOutConfirmationNavigator(): ConfirmationDialogNavigator? {

        return object : ConfirmationDialogNavigator {
            override fun onYesClicked() {

                appRepository.logoutLocale(lifecycleOwner, object :
                    APICommunicatorListener<Void?> {
                    override fun onSuccess(result: Void?) {
                        FirebaseAuth.getInstance().signOut()
                        openNewActivity(SplashActivity::class.java, null)
                    }

                    override fun onError(throwable: ErrorDetails?) {
                        showErrorBanner(throwable?.errorMsg)
                    }

                })
            }

            override fun onNoClicked() {
                dismissConfirmationDialog()
            }
        }
    }

}