package com.gtera.ui.mycars

import android.view.View
import com.gtera.R
import com.gtera.base.BaseViewModel
import com.gtera.data.error.ErrorDetails
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.User
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.authorization.login.SignInActivity
import com.gtera.ui.authorization.register.SignUpActivity
import com.gtera.ui.common.ViewHolderInterface
import com.gtera.ui.utils.AppScreenRoute.*
import java.util.*
import javax.inject.Inject

class MyCarsViewModel @Inject constructor() : BaseViewModel<MyCarsNavigator>(),
    ViewHolderInterface {


    var adapter: BaseAdapter<MyCarItemViewModel>? = null
    private val list =
        ArrayList<MyCarItemViewModel>()

    var myCarsCount: String? = ""
    init {
        adapter = BaseAdapter(list, this)
    }

    constructor(activityClass: Class<*>?) : this() {
        adapter = BaseAdapter(list, this)
    }

    var onSignInBtnClick = View.OnClickListener { view: View? ->
        openActivity(SignInActivity::class.java, null);
    }

    var onSignUpBtnClick = View.OnClickListener {
        openActivity(SignUpActivity::class.java, null);
    }

    override fun onViewCreated() {
        super.onViewCreated()
        initViewData()
    }

    override fun onViewRecreated() {
        super.onViewRecreated()
        initViewData()
    }

    private fun initViewData() {

        if (appRepository.isUserLoggedIn()) {

            appRepository.getLoggedInUser(lifecycleOwner, object : APICommunicatorListener<User?> {
                override fun onSuccess(result: User?) {
                    result?.carsCount.let { myCarsCount = it?.toString() }

                }

                override fun onError(throwable: ErrorDetails?) {
                    showErrorBanner(resourceProvider.getString(R.string.something_went_wrong))
                }
            })
            if (list.size == 0)
                getItemsList()
        } else
            navigator!!.viewRequiredLogIn()


    }

    private fun getItemsList() {
        list.clear()
        list.add(
            MyCarItemViewModel(
                R.drawable.ic_my_cars,
                R.string.str_myCar,
                myCarsCount!!,
                resourceProvider
            )
        )
        list.add(
            MyCarItemViewModel(
                R.drawable.ic_maintenance,
                R.string.str_maintenance,
                "",
                resourceProvider
            )
        )
        list.add(
            MyCarItemViewModel(
                R.drawable.ic_advertising,
                R.string.str_my_advertising,
                "",
                resourceProvider
            )
        )
        list.add(
            MyCarItemViewModel(
                R.drawable.ic_my_compare,
                R.string.str_compare,
                "",
                resourceProvider
            )
        )
        list.add(
            MyCarItemViewModel(
                R.drawable.ic_insurancce,
                R.string.str_renewal_insurance,
                "",
                resourceProvider
            )
        )

        list.add(
            MyCarItemViewModel(
                R.drawable.ic_order_now,
                R.string.str_order_requests,
                "",
                resourceProvider
            )
        )

        adapter!!.notifyDataSetChanged()
    }

    override fun onViewClicked(position: Int, id: Int) {
        val titleResource = list[position].getTitleResource()
        if (titleResource == R.string.str_compare) {
            if (appRepository.isUserLoggedIn()){

                openView(CAR_COMPARE_SCREEN, null)
            }else{

                showRequiredLoginDialog(getDrawableResources(R.drawable.ic_login),getGoToSignInConfirmationNavigator()!!)
            }

        }else if (titleResource == R.string.str_renewal_insurance){
            openView(CAR_RENEWAL_INSURANCE_LIST, null)
        }else if (titleResource == R.string.str_myCar){
            openView(MY_CARS_LIST, null)
        }else if (titleResource == R.string.str_maintenance){
            openView(CAR_MAINTENANCE_LIST,null)
        }else if (titleResource == R.string.str_order_requests){
            openView(ORDER_LIST,null)
        }
    }





}