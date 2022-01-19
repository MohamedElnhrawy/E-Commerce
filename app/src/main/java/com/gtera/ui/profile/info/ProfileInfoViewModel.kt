package com.gtera.ui.profile.info

import androidx.databinding.ObservableField
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.User
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute.EDIT_PROFILE
import com.gtera.utils.Utilities
import com.gtera.utils.Utilities.formatNumbers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ProfileInfoViewModel @Inject constructor() : BaseViewModel<ProfileInfoNavigator>(),
    ViewHolderInterface {


    var loggedUser: User? = null
    var placeHolder: ObservableField<Int> = ObservableField(R.drawable.ic_profile_info_placeholder)
    var userImage: ObservableField<String> = ObservableField("")
    var userImageRadius: ObservableField<Int> = ObservableField(150)
    var adapter: BaseAdapter<ProfileInfoItemViewModel>? = null
    private val list =
        ArrayList<ProfileInfoItemViewModel>()

    init {
        adapter = BaseAdapter(list, this)
    }

    constructor(activityClass: Class<*>?) : this() {
        adapter = BaseAdapter(list, this)
    }

    override fun onViewCreated() {
        super.onViewCreated()

        getLoggedUserData()


    }

    override fun onViewRecreated() {
        super.onViewRecreated()

        getLoggedUserData()

    }

    private fun getLoggedUserData() {
        appRepository.getLoggedInUser(lifecycleOwner, object : APICommunicatorListener<User?> {

            override fun onSuccess(result: User?) {
                loggedUser = result
                userImage.set(loggedUser?.profilePicture.let { it ?: "" })

                getItemsList()
            }

            override fun onError(throwable: ErrorDetails?) {

            }
        })
    }


    private fun getItemsList() {

        list.clear()


        list.add(
            ProfileInfoItemViewModel(
                getStringResource(R.string.str_account_details_first_name),
                loggedUser?.firstName.let { it?:"" }
            )
        )

        list.add(
            ProfileInfoItemViewModel(
                getStringResource(R.string.str_account_details_last_name),
                loggedUser?.lastName.let { it?:"" }
            )
        )


        list.add(
            ProfileInfoItemViewModel(
                getStringResource(R.string.str_account_details_email),
                loggedUser?.email.let { it?:"" }
            )
        )

        list.add(
            ProfileInfoItemViewModel(
                getStringResource(R.string.str_account_details_phone_number),
                loggedUser?.phoneNumber.let { formatNumbers(it, false)?:"" }
            )
        )

        list.add(
            ProfileInfoItemViewModel(
                getStringResource(R.string.str_account_details_birthdate),
                loggedUser?.birthDate.let {
                    if (it != null)
                        Utilities.formatDate(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(it)) else ""
                }!!

            )
        )

        list.add(
            ProfileInfoItemViewModel(
                getStringResource(R.string.str_account_details_gender),
                loggedUser?.gender.let { if(it ==1) getStringResource(R.string.str_edit_account_male) else getStringResource(R.string.str_edit_account_female)}
            )
        )


        list.add(
            ProfileInfoItemViewModel(
                getStringResource(R.string.str_account_details_governorate),
                loggedUser?.governorate.let { it?.name ?: "" }

            )
        )

        list.add(
            ProfileInfoItemViewModel(
                getStringResource(R.string.str_account_details_city),
                loggedUser?.city.let { it?.name ?: "" }

            )
        )


        adapter!!.notifyDataSetChanged()
    }

    override fun onViewClicked(position: Int, id: Int) {

    }

    fun editProfile() {

        openView(EDIT_PROFILE, null)
    }


}