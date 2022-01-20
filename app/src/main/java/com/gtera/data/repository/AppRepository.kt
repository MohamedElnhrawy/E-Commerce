package com.gtera.data.repository

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.gtera.data.error.ErrorDetails
import com.gtera.data.helper.APICommunicator
import com.gtera.data.helper.CommunicatorListener
import com.gtera.data.helper.Observers
import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.local.db.DataBaseCommunicator
import com.gtera.data.local.preferences.PreferencesHelper
import com.gtera.data.model.*
import com.gtera.data.model.requests.*
import com.gtera.data.model.response.*
import com.gtera.data.remote.Endpoints
import com.gtera.data.response.APIResponse
import com.gtera.utils.LocaleHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val apiCommunicator: APICommunicator,
    private val preferencesHelper: PreferencesHelper,
    private val dataBaseCommunicator: DataBaseCommunicator
) {

    var loggedUser: User? = null


    private fun <T> getLiveData(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<T>?
    ): MutableLiveData<APIResponse<T>> {
        var liveData = MutableLiveData<APIResponse<T>>()
        GlobalScope.launch(Dispatchers.Main) {
            liveData.observe(
                lifecycleOwner!!,
                Observers.getLiveDataObserver(listener!!, lifecycleOwner)
            )
        }
        return liveData
    }

    private fun updateUserAuthenticationData(result: BaseResponse<User?>?, updateToken: Boolean) {
        if (updateToken) {
            setAccessToken(result?.meta?.accessToken)
            setRefreshToken(result?.meta?.refreshToken)
        }
        loggedUser = result?.data
        dataBaseCommunicator.insertUser(
            result?.data,
            object : APICommunicatorListener<Void?> {
                override fun onSuccess(result: Void?) {

                }

                override fun onError(throwable: ErrorDetails?) {

                }

            })
    }


    fun deleteUserAuthenticationData(deleteTokens: Boolean) {
        if (deleteTokens) {
            deleteAccessToken()
            deleteRefreshToken()
        }
        dataBaseCommunicator.deleteLoggedInUser(object : APICommunicatorListener<Void?> {
            override fun onSuccess(result: Void?) {

            }

            override fun onError(throwable: ErrorDetails?) {

            }

        })
    }


    fun updateUserAuthenticationData(result: BaseResponse<User?>?) {

        dataBaseCommunicator.updateLoggedInUser(
            result?.data,
            object : APICommunicatorListener<Void?> {
                override fun onSuccess(result: Void?) {

                }

                override fun onError(throwable: ErrorDetails?) {

                }

            })

    }
    fun insertUserLocally(user: User) {

        loggedUser = user
        dataBaseCommunicator.insertUser(
            user,
            object : APICommunicatorListener<Void?> {
                override fun onSuccess(result: Void?) {
                    setIsUserLoggedIn(true)
                }

                override fun onError(throwable: ErrorDetails?) {

                }

            })
    }

    fun getHome(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<HomeContent?>?
    ) {
        apiCommunicator.getHome(object : CommunicatorListener<HomeContent?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: HomeContent?) {
                super.onSuccess(result)
            }
        })
    }

    fun searchProducts(
        searchName: String,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Product?>?>?>?
    ) {
        apiCommunicator.searchProducts(searchName,object : CommunicatorListener<BaseResponse<ArrayList<Product?>?>?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: BaseResponse<ArrayList<Product?>?>?) {
                super.onSuccess(result)
            }
        })
    }

    fun addProductToCart(
        cartProduct: CartProduct) {

        dataBaseCommunicator.addProductToCart(
            cartProduct,
            object : APICommunicatorListener<Void?> {
                override fun onSuccess(result: Void?) {
                    Log.e("ppp","added")
                }

                override fun onError(throwable: ErrorDetails?) {
                    Log.e("ppp",throwable?.errorMsg)

                }

            })
    }

    fun clearCart() {
        dataBaseCommunicator.clearCart(
            object : APICommunicatorListener<Void?> {
                override fun onSuccess(result: Void?) {
                    Log.e("ppp","cleared")
                }

                override fun onError(throwable: ErrorDetails?) {
                    Log.e("ppp",throwable?.errorMsg)
                }

            })

    }

    fun getLiveCartList(
        lifecycleOwner: LifecycleOwner?,
        listener: Observer<List<CartProduct?>?>?
    ) {
        val liveData: LiveData<List<CartProduct?>?>? = dataBaseCommunicator.getLiveCartList()
        liveData?.observe(lifecycleOwner!!, listener!!)
    }

    fun isUserLoggedIn(): Boolean {
        return preferencesHelper.isUserLoggedIn
    }

    fun setIsUserLoggedIn(status:Boolean) {
        preferencesHelper.setIsUserLoggedIn(status)
    }



    fun getAppVersion(): Int {
        return 0
    }

    fun setAppVersion(versionCode: Int) {}
    fun getAppLanguage(): String? {
        return preferencesHelper.appLanguage
    }

    fun setAppLanguage(language: String?) {
        preferencesHelper.appLanguage = language
    }

    fun isPushNotificationEnabled(): Boolean {
        return preferencesHelper.isPushNotificationEnabled
    }

    fun pushNotificationUpdate(isEnabled: Boolean) {
        preferencesHelper.pushNotificationUpdate(isEnabled)
    }

    fun getAccessToken(): String? {
        return preferencesHelper.accessToken
    }

    fun setAccessToken(token: String?) {
        preferencesHelper.accessToken = token
    }

    fun setRefreshToken(token: String?) {
        preferencesHelper.refreshToken = token
    }

    fun deleteAccessToken() {
        preferencesHelper.deleteAccessToken()
    }

    fun deleteRefreshToken() {
        preferencesHelper.deleteRefreshToken()
    }



    fun getLoggedInUser(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<User?>
    ) {
        dataBaseCommunicator.getLoggedInUser(
            CommunicatorListener(
                getLiveData(lifecycleOwner, listener)
            )
        )
    }



    fun logoutLocale(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<Void?>?
    ) {
        dataBaseCommunicator.clearData(listener)
        preferencesHelper.deleteAccessToken()
        preferencesHelper.setIsUserLoggedIn(false)
    }

    fun logout(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<Void?>?
    ) {
        logoutLocale(lifecycleOwner, listener)
    }



    fun changeLanguage(context: Context, language: String) {
        LocaleHelper.setLocale(context, language)
        preferencesHelper.appLanguage = language
    }




}