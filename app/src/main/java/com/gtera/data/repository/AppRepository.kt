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


    fun signIn(
        lifecycleOwner: LifecycleOwner?,
        user: User?,
        listener: APICommunicatorListener<BaseResponse<User?>?>?
    ) {
        apiCommunicator.signIn(user, object : CommunicatorListener<BaseResponse<User?>?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: BaseResponse<User?>?) {
                super.onSuccess(result)
                dataBaseCommunicator.insertUser(result?.data, Observers.emptyListener)
                updateUserAuthenticationData(result, true)

            }
        })
    }

    fun signOut(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<LogOutResponse>?>?
    ) {
        apiCommunicator.signOut(object : CommunicatorListener<BaseResponse<LogOutResponse>?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: BaseResponse<LogOutResponse>?) {
                super.onSuccess(result)
                deleteUserAuthenticationData(true)

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

    fun signInWithSocialAccount(
        lifecycleOwner: LifecycleOwner?,
        socialRequest: SocialLoginRequest?,
        listener: APICommunicatorListener<BaseResponse<User?>?>?
    ) {
        apiCommunicator.signInWithSocialAccount(
            socialRequest,
            object : CommunicatorListener<BaseResponse<User?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<User?>?) {
                    super.onSuccess(result)
                    updateUserAuthenticationData(result, true)
                }
            })
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


    fun sendResetPassword(
        resetPasswordRequest: ResetPasswordRequest?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ResetPassword?>?>?
    ) {
        apiCommunicator.sendResetPassword(
            resetPasswordRequest,
            object : CommunicatorListener<BaseResponse<ResetPassword?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<ResetPassword?>?) {
                    super.onSuccess(result)
                }
            })
    }


    fun changePassword(
        changePasswordRequest: ChangePasswordRequest?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<User?>?
    ) {
        apiCommunicator.changePassword(changePasswordRequest, object : CommunicatorListener<User?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: User?) {
                super.onSuccess(result)
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




    fun getNews(
        perPage: Int?,
        pageNumber: Int?,
        sortBy: String?,
        desc: String?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<New?>?>?>?
    ) {
        apiCommunicator.getNews(perPage,pageNumber,sortBy,desc,object : CommunicatorListener<BaseResponse<ArrayList<New?>?>?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: BaseResponse<ArrayList<New?>?>?) {
                super.onSuccess(result)
            }
        })
    }

    fun getTopDeals(
        isHotDeals: Boolean,
        sortBy: String?,
        desc: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<TopDeal?>?>?>?
    ) {
        apiCommunicator.getTopDeals(
            isHotDeals,
            sortBy,
            desc,
            object : CommunicatorListener<BaseResponse<ArrayList<TopDeal?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<ArrayList<TopDeal?>?>?) {
                    super.onSuccess(result)
                }
            })


    }

    fun getOffers(
        perPage: Int?,
        pageNumber: Int?,
        sortBy: String?,
        desc: String?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Offer?>?>?>?
    ) {
        apiCommunicator.getOffers(perPage,pageNumber,sortBy,desc,object : CommunicatorListener<BaseResponse<ArrayList<Offer?>?>?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: BaseResponse<ArrayList<Offer?>?>?) {
                super.onSuccess(result)
            }
        })
    }


    fun getBrands(
        searchName: String?,
        perPage: Int?,
        sortBy: String?,
        desc: String?,
        pageNumber: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Brand?>?>?>?
    ) {
        apiCommunicator.getBrands(
            searchName,
            perPage,
            sortBy,
            desc,
            pageNumber,
            object : CommunicatorListener<BaseResponse<ArrayList<Brand?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<ArrayList<Brand?>?>?) {
                    super.onSuccess(result)
                }
            })
    }


    fun getCars(
        page: Int?,
        perPage: Int?,
        used: Int?,
        new: Int?,
        modelId: Int?,
        brandsId: String?,
        searchName: String?,
        budgetId: Int?,
        sortBy: String?,
        desc: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>?
    ) {
        apiCommunicator.getCars(
            page,
            perPage,
            used,
            new,
            modelId,
            brandsId,
            searchName,
            budgetId,
            sortBy,
            desc,
            object : CommunicatorListener<BaseResponse<ArrayList<Car?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun getCars(
        queryMap: HashMap<String, String>,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>?
    ) {
        apiCommunicator.getCars(
            queryMap,
            object : CommunicatorListener<BaseResponse<ArrayList<Car?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun getCar(
        carId: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Car?>?>?
    ) {
        apiCommunicator.getCar(carId, object : CommunicatorListener<BaseResponse<Car?>?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: BaseResponse<Car?>?) {
                super.onSuccess(result)
            }
        })
    }

    fun getRelatedNews(
        newsId: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<New?>?>?>?
    ) {
        apiCommunicator.getRelatedNews(
            newsId,
            object : CommunicatorListener<BaseResponse<ArrayList<New?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<ArrayList<New?>?>?) {
                    super.onSuccess(result)
                }
            })
    }

    fun getBudgets(
        page: Int?,
        perPage: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Budget?>?>?>?
    ) {
        apiCommunicator.getBudgets(
            page,
            perPage,
            object : CommunicatorListener<BaseResponse<ArrayList<Budget?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<ArrayList<Budget?>?>?) {
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



//    fun addProductToCart(cartProduct: CartProduct,
//                         lifecycleOwner: LifecycleOwner?,
//                         listener: APICommunicatorListener<Void?>
//    ) {
//
//        dataBaseCommunicator.addProductToCart(
//            cartProduct,
//            object : APICommunicatorListener<Void?> {
//                override fun onSuccess(result: Void?) {
//
//                }
//
//                override fun onError(throwable: ErrorDetails?) {
//
//                }
//
//            })
//
//    }
    fun clearCart() {
        dataBaseCommunicator.clearCart(
            object : APICommunicatorListener<Void?> {
                override fun onSuccess(result: Void?) {

                }

                override fun onError(throwable: ErrorDetails?) {

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


    fun addCarCompare(
        addToCompareRequest: AddRemoveToCompareRequest,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Compare?>?>?
    ) {
        apiCommunicator.addCarCompare(
            addToCompareRequest,
            object : CommunicatorListener<BaseResponse<Compare?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<Compare?>?) {
                    super.onSuccess(result)
                }
            })
    }

    fun removeCarCompare(
        removeCompareRequest: AddRemoveToCompareRequest,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<DeleteCompareResponse?>?
    ) {
        apiCommunicator.removeCarCompare(
            removeCompareRequest,
            object : CommunicatorListener<DeleteCompareResponse?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: DeleteCompareResponse?) {
                    super.onSuccess(result)
                }
            })
    }


    fun getCarCompare(
        sortBy: String?,
        desc: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>?
    ) {
        apiCommunicator.getCarCompare(
            sortBy,
            desc,
            object : CommunicatorListener<BaseResponse<ArrayList<Car?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<ArrayList<Car?>?>?) {
                    super.onSuccess(result)
                }
            })
    }

    fun updateProfile(
        updatedUser: User,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<User?>?>?
    ) {
        apiCommunicator.updateProfile(
            updatedUser,
            object : CommunicatorListener<BaseResponse<User?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<User?>?) {
                    super.onSuccess(result)
                    updateUserAuthenticationData(result)
                }
            })
    }

    fun getGovernrates(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<List<Governorate?>?>?>?
    ) {
        apiCommunicator.getGovernrates(
            object : CommunicatorListener<BaseResponse<List<Governorate?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<List<Governorate?>?>?) {
                    super.onSuccess(result)
                }
            })
    }

    fun getCities(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<List<City?>?>?>?
    ) {
        apiCommunicator.getCities(
            object : CommunicatorListener<BaseResponse<List<City?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<List<City?>?>?) {
                    super.onSuccess(result)
                }
            })
    }


    fun getFiltrationCriteria(
        used: Boolean?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<List<FiltrationCriteria?>?>?>?
    ) {
        apiCommunicator.getFiltrationCriteria(
            used,
            object : CommunicatorListener<BaseResponse<List<FiltrationCriteria?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<List<FiltrationCriteria?>?>?) {
                    super.onSuccess(result)
                }
            })
    }


    fun getRelatedCarSearch(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<List<Car>?>?>
    ) {
        apiCommunicator.getRelatedCarSearch(
            object : CommunicatorListener<BaseResponse<List<Car>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<List<Car>?>?) {
                    super.onSuccess(result)
                }
            })
    }


    fun addToFavorites(
        addToFavoritesRequest: FavoritesRequest,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<FavoriteResponse?>
    ) {
        apiCommunicator.addToFavorites(
            addToFavoritesRequest,
            object : CommunicatorListener<FavoriteResponse?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: FavoriteResponse?) {
                    super.onSuccess(result)
                }
            })
    }

    fun removeFromFavorites(
        addToFavoritesRequest: FavoritesRequest,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<FavoriteResponse?>
    ) {
        apiCommunicator.removeFromFavorites(
            addToFavoritesRequest,
            object : CommunicatorListener<FavoriteResponse?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: FavoriteResponse?) {
                    super.onSuccess(result)
                }
            })
    }


    fun getFavorites(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<List<Favourite?>?>?>?
    ) {
        apiCommunicator.getFavorites(
            object : CommunicatorListener<BaseResponse<List<Favourite?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<List<Favourite?>?>?) {
                    super.onSuccess(result)
                }
            })
    }

    fun getBranches(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<List<Branche?>?>?>?
    ) {
        apiCommunicator.getBranches(
            object : CommunicatorListener<BaseResponse<List<Branche?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<List<Branche?>?>?) {
                    super.onSuccess(result)
                }
            })
    }


    fun requestCarRenewalInsurance(
        carRenewalRequest: CarRenewalRequest,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<FavoriteResponse?>
    ) {
        apiCommunicator.requestCarRenewalInsurance(
            carRenewalRequest,
            object : CommunicatorListener<FavoriteResponse?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: FavoriteResponse?) {
                    super.onSuccess(result)
                }
            })
    }

    fun getInsuranceRequests(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<List<CarInsuranceRenewal?>?>?>?
    ) {
        apiCommunicator.getInsuranceRequests(
            object : CommunicatorListener<BaseResponse<List<CarInsuranceRenewal?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<List<CarInsuranceRenewal?>?>?) {
                    super.onSuccess(result)
                }
            })
    }


    fun getModels(
        brandId: Int?,
        perPage: Int?,
        sortBy: String?,
        desc: String?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Model?>?>?>
    ) {
        apiCommunicator.getModels(
            brandId,
            perPage,
            sortBy,
            desc,
            object : CommunicatorListener<BaseResponse<ArrayList<Model?>?>?>(
                getLiveData(
                    lifecycleOwner,
                    listener
                )
            ) {
                override fun onSuccess(result: BaseResponse<ArrayList<Model?>?>?) {
                    super.onSuccess(result)
                }
            })
    }

    fun signUp(
        lifecycleOwner: LifecycleOwner?,
        user: User?,
        listener: APICommunicatorListener<BaseResponse<User?>?>?
    ) {
        apiCommunicator.signUp(user, object : CommunicatorListener<BaseResponse<User?>?>(
            getLiveData(
                lifecycleOwner,
                listener
            )
        ) {
            override fun onSuccess(result: BaseResponse<User?>?) {
                super.onSuccess(result)
                setAccessToken(result?.meta?.accessToken)
                setRefreshToken(result?.meta?.refreshToken)
                dataBaseCommunicator.insertUser(
                    result?.data,
                    object : APICommunicatorListener<Void?> {
                        override fun onSuccess(result: Void?) {

                        }

                        override fun onError(throwable: ErrorDetails?) {

                        }

                    })
            }
        })
    }

    fun isUserLoggedIn(): Boolean {
        return preferencesHelper.isUserLoggedIn
    }

    fun setGuestUser() {
        preferencesHelper.setIsGuest()
    }

    fun isGuestUser(): Boolean {
        return preferencesHelper.isGuestUser
    }

    fun reCreateGuest(lifecycleOwner: LifecycleOwner?) {
        preferencesHelper.deleteAccessToken()
        preferencesHelper.removeGuest()
        guestUser(lifecycleOwner)
    }

    fun guestUser(lifecycleOwner: LifecycleOwner?) {
        guestUser(lifecycleOwner, object : APICommunicatorListener<User?> {
            override fun onSuccess(result: User?) {}
            override fun onError(throwable: ErrorDetails?) {}

        })
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

    fun getDeviceToken(): String? {
        return preferencesHelper.deviceToken
    }

    fun sendDeviceToken(deviceToken: String?) {
        preferencesHelper.deviceToken = deviceToken
        assignDeviceToken(deviceToken)
    }

    fun deviceTokenNeedAssign(): Boolean {
        return !TextUtils.isEmpty(preferencesHelper.deviceToken) &&
                preferencesHelper.isDeviceTokenRegistered == 0
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

    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<User?>?
    ) {
        TODO("Not yet implemented")
    }

    fun updateProfile(
        lifecycleOwner: LifecycleOwner?,
        user: User?,
        listener: APICommunicatorListener<User?>?
    ) {
        TODO("Not yet implemented")
    }

    fun changePassword(
        lifecycleOwner: LifecycleOwner?,
        oldPassword: String?,
        newPassword: String?,
        listener: APICommunicatorListener<Void?>?
    ) {
        TODO("Not yet implemented")
    }

    fun resetPassword(
        lifecycleOwner: LifecycleOwner?,
        email: String?,
        listener: APICommunicatorListener<Void?>?
    ) {
        TODO("Not yet implemented")
    }

    fun logout(listener: APICommunicatorListener<Void>) {}
    fun logoutLocale(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<Void?>?
    ) {
        dataBaseCommunicator.clearData(listener)
        preferencesHelper.deleteAccessToken()
    }

    fun logout(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<Void?>?
    ) {
        logoutLocale(lifecycleOwner, listener)
    }

    fun defineGuestUser(lifecycleOwner: LifecycleOwner?) {
        TODO("Not yet implemented")
    }

    fun guestUser(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<User?>?
    ) {

        TODO("Not yet implemented")
    }

    fun isActiveUser(): Boolean {
        TODO("Not yet implemented")
    }

    fun assignDeviceToken(deviceToken: String?) {
        TODO("Not yet implemented")
    }


    fun changeLanguage(context: Context, language: String) {
        LocaleHelper.setLocale(context, language)
        preferencesHelper.appLanguage = language
    }

    fun getJobTitles(
        page: Int?,
        perPage: Int?,
        sortBy: String?,
        desc: Int?,
        trashed: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<JobTitle?>?>?>?
    ) {
        apiCommunicator.getJobTitles(
            page,
            perPage,
            sortBy,
            desc,
            trashed,
            object : CommunicatorListener<BaseResponse<ArrayList<JobTitle?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun calculateIt(
        body: HashMap<String, Any>,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>?
    ) {
        apiCommunicator.calculateIt(
            body,
            object : CommunicatorListener<BaseResponse<ArrayList<Car?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun ContactUs(
        body: HashMap<String, String>,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Any>>
    ) {
        apiCommunicator.ContactUs(
            body,
            object : CommunicatorListener<BaseResponse<Any>>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun RenewalInsurancePay(
        id: Int,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<Payment?>
    ) {
        apiCommunicator.RenewalInsurancePay(
            id,
            object : CommunicatorListener<Payment?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun getNewById(
        id: Int,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<New?>>
    ) {
        apiCommunicator.getNewById(
            id,
            object : CommunicatorListener<BaseResponse<New?>>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun getOfferById(
        id: Int,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Offer?>>
    ) {
        apiCommunicator.getOfferById(
            id,
            object : CommunicatorListener<BaseResponse<Offer?>>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun getMyCars(
        page: Int?,
        perPage: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>?
    ) {
        apiCommunicator.getMyCars(
            page,
            perPage,
            object : CommunicatorListener<BaseResponse<ArrayList<Car?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun getMyCarsAddingSpecs(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<CarAddingSpecs?>?>?
    ) {
        apiCommunicator.getMyCarsAddingSpecs(
            object : CommunicatorListener<BaseResponse<CarAddingSpecs?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun addNewMyCar(
        addNewCarRequest: AddNewCarRequest,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {


        apiCommunicator.addNewMyCar(addNewCarRequest,
            object : CommunicatorListener<BaseResponse<Any?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun getMyNotifications(
        perPage: Int?,
        pageNumber: Int?,
        sortBy: String?,
        desc: String?,
        trashed:String?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Notification?>?>?>
    ) {
        apiCommunicator.getMyNotifications(perPage,pageNumber, sortBy, desc, trashed,
            object : CommunicatorListener<BaseResponse<ArrayList<Notification?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun getMyMessages(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Message?>?>?>
    ) {
        apiCommunicator.getMyMessages(
            object : CommunicatorListener<BaseResponse<ArrayList<Message?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun startConversation(
        messagesItem: MessagesItem,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {

        apiCommunicator.startConversation(messagesItem,
            object : CommunicatorListener<BaseResponse<Any?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun getConversationHistory(
        id: Int,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Message?>?>
    ) {
        apiCommunicator.getConversationHistory(
            id,
            object : CommunicatorListener<BaseResponse<Message?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun sendConversationMessage(
        messagesItem: MessagesItem,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {


        apiCommunicator.sendConversationMessage(messagesItem,
            object : CommunicatorListener<BaseResponse<Any?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun getMyUnReadNotifications(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Notification?>?>?>
    ) {
        apiCommunicator.getMyUnReadNotifications(
            object : CommunicatorListener<BaseResponse<ArrayList<Notification?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun markAsUnread(
        id: Int,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {
        apiCommunicator.markAsUnread(
            id,
            object : CommunicatorListener<BaseResponse<Any?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun markAllAsUnread(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {
        apiCommunicator.markAllAsUnread(
            object : CommunicatorListener<BaseResponse<Any?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun getFAQ(
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<FAQ?>?>?>
    ) {
        apiCommunicator.getFAQ(
            object : CommunicatorListener<BaseResponse<ArrayList<FAQ?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun getOrderNowData(
        orderNowDataRequest: OrderNowDataRequest,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<OrderNowViewData?>?>
    ) {
        apiCommunicator.getOrderNowData(
            orderNowDataRequest,
            object : CommunicatorListener<BaseResponse<OrderNowViewData?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun getUserOrders(
        perPage: Int?,
        pageNumber: Int?,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Order?>?>?>
    ) {
        apiCommunicator.getUserOrders(
            perPage,
            pageNumber,
            object : CommunicatorListener<BaseResponse<ArrayList<Order?>?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun orderCar(
        order: OrderNowRequest,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {
        apiCommunicator.orderCar(
            order,
            object : CommunicatorListener<BaseResponse<Any?>?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }

    fun payCarOrder(
        orderId: Int,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<Payment?>
    ) {
        apiCommunicator.payCarOrder(
            orderId,
            object : CommunicatorListener<Payment?>(
                getLiveData(lifecycleOwner, listener)
            ) {})

    }


    fun downloadFile(
        url: String,
        lifecycleOwner: LifecycleOwner?,
        listener: APICommunicatorListener<ResponseBody>
    ) {
        apiCommunicator.downloadFile(url,
            object : CommunicatorListener<ResponseBody>(
                getLiveData(lifecycleOwner, listener)
            ) {

            })
    }


}