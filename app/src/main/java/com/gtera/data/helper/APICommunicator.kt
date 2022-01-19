package com.gtera.data.helper

import com.gtera.data.interfaces.APICommunicatorListener
import com.gtera.data.model.*
import com.gtera.data.model.requests.*
import com.gtera.data.model.response.*
import com.gtera.data.remote.ApiInterface
import com.gtera.data.remote.Endpoints.URL_CHANGE_PASSWORD
import com.gtera.data.remote.Endpoints.URL_HOME
import com.gtera.data.remote.Endpoints.URL_NEWS
import com.gtera.data.remote.Endpoints.URL_OFFERS
import com.gtera.data.remote.Endpoints.URL_SEARCH
import com.gtera.data.remote.Endpoints.URL_SEND_RESET_PASSWORD
import com.gtera.data.remote.Endpoints.URL_SIGN_IN
import com.gtera.data.remote.Endpoints.URL_SIGN_UP
import com.gtera.data.remote.Endpoints.URL_TOP_DEALS
import com.gtera.di.providers.ResourceProvider
import io.michaelrocks.paranoid.Obfuscate
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import javax.inject.Inject

@Obfuscate
class APICommunicator @Inject constructor(
    private val apiService: ApiInterface,
    val resourceProvider: ResourceProvider
) {


    private fun <T> buildObservable(
        observable: Single<T>?,
        listener: APICommunicatorListener<T>
    ) {
        observable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(Observers.getSingleObserver(listener, resourceProvider))
    }

    private fun buildObservable(
        observable: Completable?,
        listener: APICommunicatorListener<Void?>
    ) {
        observable!!.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Observers.getCompletableObserver(listener, resourceProvider))
    }

    fun signIn(
        user: User?,
        listener: APICommunicatorListener<BaseResponse<User?>?>
    ) {
        val url: String = URL_SIGN_IN
        buildObservable(apiService.signIn(url, user), listener)
    }

    fun signOut(
        listener: APICommunicatorListener<BaseResponse<LogOutResponse>?>
    ) {

        buildObservable(apiService.signOut(), listener)
    }


    fun signInWithSocialAccount(
        socialRequest: SocialLoginRequest?,
        listener: APICommunicatorListener<BaseResponse<User?>?>
    ) {

        buildObservable(apiService.signInWithSocialAccount(socialRequest), listener)
    }

    fun signUp(
        user: User?,
        listener: APICommunicatorListener<BaseResponse<User?>?>
    ) {
        val url: String = URL_SIGN_UP
        buildObservable(apiService.signUp(url, user), listener)
    }

    fun sendResetPassword(
        resetPasswordRequest: ResetPasswordRequest?,
        listener: APICommunicatorListener<BaseResponse<ResetPassword?>?>
    ) {
        val url: String = URL_SEND_RESET_PASSWORD
        buildObservable(apiService.sendResetPassword(url, resetPasswordRequest), listener)
    }


    fun changePassword(
        changePasswordRequest: ChangePasswordRequest?,
        listener: APICommunicatorListener<User?>
    ) {
        val url: String = URL_CHANGE_PASSWORD
        buildObservable(apiService.changePassword(url, changePasswordRequest), listener)
    }


    fun getHome(
        listener: APICommunicatorListener<HomeContent?>
    ) {
        buildObservable(apiService.getHomeContent(), listener)
    }

    fun searchProducts(searchText:String,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Product?>?>?>
    ) {
        buildObservable(apiService.searchProducts(), listener)
    }


    fun getNews(
        perPage: Int?,
        pageNumber: Int?,
        sortBy: String?,
        desc: String?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<New?>?>?>
    ) {
        val url: String = URL_NEWS
        buildObservable(apiService.getNews(url,perPage,pageNumber,sortBy,desc), listener)
    }

    fun getOffers(
        perPage: Int?,
        pageNumber: Int?,
        sortBy: String?,
        desc: String?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Offer?>?>?>
    ) {
        val url: String = URL_OFFERS
        buildObservable(apiService.getOffers(url,perPage,pageNumber, sortBy, desc), listener)
    }

    fun getTopDeals(
        isTopDeals: Boolean,
        sortBy: String?,
        desc: Int?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<TopDeal?>?>?>
    ) {
        val url: String = URL_TOP_DEALS
        buildObservable(apiService.getTopDeals(url, isTopDeals,sortBy,desc), listener)
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
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>
    ) {

        var queryMap: HashMap<String, String> = HashMap()

        queryMap.put("page", page.toString())
        queryMap.put("perPage", perPage.toString())
        queryMap.put("used", used.toString())
        queryMap.put("new", new.toString())
        modelId.let {
            if (it != null) queryMap.put(
                "model_id",
                modelId.toString()
            )
        }
        searchName.let {
            if (it != null && !it.isEmpty()) queryMap.put(
                "name",
                searchName.toString()
            )
        }
        budgetId.let {
            if (it != null) queryMap.put(
                "budget_id",
                budgetId.toString()
            )
        }
        brandsId.let { if (it != null&& !it.isEmpty()) queryMap.put("brand_id", brandsId!!) }
        sortBy.let { if (it != null && !it.isEmpty()) queryMap.put("sortBy", sortBy.toString()) }
        desc.let { if (it != null) queryMap.put("desc", desc.toString()) }

        buildObservable(
            apiService.getCars(
                queryMap
            ), listener
        )
    }


    fun getCars(
        queryMap: HashMap<String, String>,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>
    ) {
        buildObservable(
            apiService.getCars(
                queryMap
            ), listener
        )
    }
    fun getCar(
        carId: Int?,
        listener: APICommunicatorListener<BaseResponse<Car?>?>
    ) {
        buildObservable(apiService.getCar(carId), listener)
    }

    fun getRelatedNews(
        newId: Int?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<New?>?>?>
    ) {
        buildObservable(apiService.getRelatedNews(newId), listener)
    }

    fun getBrands(
        searchName: String?,
        perPage: Int?,
        sortBy: String?,
        desc: String?,
        pageNumber: Int?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Brand?>?>?>
    ) {

        buildObservable(
            apiService.getBrands(searchName, perPage, sortBy, desc, pageNumber),
            listener
        )
    }

    fun getModels(
        brandId: Int?,
        perPage: Int?,
        sortBy: String?,
        desc: String?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Model?>?>?>
    ) {
        buildObservable(apiService.getModels(brandId, perPage, sortBy, desc), listener)
    }

    fun getBudgets(
        page: Int?,
        perPage: Int?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Budget?>?>?>
    ) {
        buildObservable(apiService.getBudgets(page, perPage), listener)
    }

    fun addCarCompare(
        addToCompareRequest: AddRemoveToCompareRequest,
        listener: APICommunicatorListener<BaseResponse<Compare?>?>
    ) {
        buildObservable(apiService.addCarCompare(addToCompareRequest), listener)
    }

    fun removeCarCompare(
        removeCompareRequest: AddRemoveToCompareRequest,
        listener: APICommunicatorListener<DeleteCompareResponse?>
    ) {
        buildObservable(apiService.removeCarCompare(removeCompareRequest), listener)
    }


    fun getCarCompare(
        sortBy: String?,
        desc: Int?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>
    ) {
        buildObservable(apiService.getCarCompare(sortBy, desc), listener)
    }


    fun updateProfile(
        updatedUser: User,
        listener: APICommunicatorListener<BaseResponse<User?>?>
    ) {
        buildObservable(apiService.updateProfile(updatedUser), listener)
    }


    fun getGovernrates(
        listener: APICommunicatorListener<BaseResponse<List<Governorate?>?>?>
    ) {
        buildObservable(apiService.getGovernorates(), listener)
    }


    fun getCities(
        listener: APICommunicatorListener<BaseResponse<List<City?>?>?>
    ) {
        buildObservable(apiService.getCities(), listener)
    }


    fun getFiltrationCriteria(
        used: Boolean?,
        listener: APICommunicatorListener<BaseResponse<List<FiltrationCriteria?>?>?>
    ) {
        val usedValue: Int? = if (used == null) null else if (used) 1 else 0
        buildObservable(apiService.getFiltrationCriteria(usedValue), listener)
    }

    fun getRelatedCarSearch(
        listener: APICommunicatorListener<BaseResponse<List<Car>?>?>
    ) {
        buildObservable(apiService.getRelatedCarSearch(), listener)
    }

    fun addToFavorites(
        addToFavoritesRequest: FavoritesRequest,
        listener: APICommunicatorListener<FavoriteResponse?>
    ) {
        buildObservable(apiService.addToFavorites(addToFavoritesRequest), listener)
    }


    fun removeFromFavorites(
        addToFavoritesRequest: FavoritesRequest,
        listener: APICommunicatorListener<FavoriteResponse?>
    ) {
        buildObservable(apiService.removeFromFavorites(addToFavoritesRequest), listener)
    }


    fun getFavorites(
        listener: APICommunicatorListener<BaseResponse<List<Favourite?>?>?>
    ) {
        buildObservable(apiService.getFavorites(), listener)
    }

    fun getBranches(
        listener: APICommunicatorListener<BaseResponse<List<Branche?>?>?>
    ) {
        buildObservable(apiService.getBranches(), listener)
    }


    fun requestCarRenewalInsurance(
        carRenewalRequest: CarRenewalRequest,
        listener: APICommunicatorListener<FavoriteResponse?>
    ) {
        buildObservable(apiService.requestCarRenewalInsurance(carRenewalRequest), listener)
    }


    fun getInsuranceRequests(
        listener: APICommunicatorListener<BaseResponse<List<CarInsuranceRenewal?>?>?>
    ) {
        buildObservable(apiService.getInsuranceRequests(), listener)
    }


    fun getJobTitles(
        page: Int?,
        perPage: Int?,
        sortBy: String?,
        desc: Int?,
        trashed: Int?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<JobTitle?>?>?>
    ) {

        buildObservable(
            apiService.getJobTitles(
                page,
                perPage,
                sortBy,
                desc,
                trashed
            ), listener
        )
    }

    fun calculateIt(
       body: HashMap<String,Any>,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>
    ) {

        buildObservable(
            apiService.calculateIt(
                body
            ), listener
        )
    }

    fun ContactUs(
       body: HashMap<String,String>,
        listener: APICommunicatorListener<BaseResponse<Any>>
    ) {

        buildObservable(
            apiService.contactUs(
                body
            ), listener
        )
    }

    fun RenewalInsurancePay(
       id:Int,
        listener: APICommunicatorListener<Payment?>
    ) {

        buildObservable(
            apiService.renewalInsurancePay(
                id
            ), listener
        )
    }

    fun getNewById(
       id:Int,
        listener: APICommunicatorListener<BaseResponse<New?>>
    ) {

        buildObservable(
            apiService.getNewById(
                id
            ), listener
        )
    }

    fun getOfferById(
       id:Int,
        listener: APICommunicatorListener<BaseResponse<Offer?>>
    ) {

        buildObservable(
            apiService.getOfferById(
                id
            ), listener
        )
    }


    fun getMyCars(
        page: Int?,
        perPage: Int?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Car?>?>?>
    ) {

        buildObservable(
            apiService.getMyCars(
                page,
                perPage
            ), listener
        )
    }

    fun getMyCarsAddingSpecs(
        listener: APICommunicatorListener<BaseResponse<CarAddingSpecs?>?>
    ) {
        buildObservable(
            apiService.getMyCarsAddingSpecs(), listener
        )
    }


    fun addNewMyCar(
        addNewCarRequest: AddNewCarRequest,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {

        buildObservable(
            apiService.addNewMyCar(
                addNewCarRequest
            ), listener
        )
    }


    fun getMyMessages(
        listener: APICommunicatorListener<BaseResponse<ArrayList<Message?>?>?>
    ) {
        buildObservable(
            apiService.getMyMessages(), listener
        )
    }


    fun startConversation(
        messagesItem: MessagesItem,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {

        buildObservable(
            apiService.startConversation(
                messagesItem
            ), listener
        )
    }


    fun getConversationHistory(
        id: Int,
        listener: APICommunicatorListener<BaseResponse<Message?>?>
    ) {

        buildObservable(
            apiService.getConversationHistory(
                id
            ), listener
        )
    }


    fun sendConversationMessage(
        messagesItem: MessagesItem,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {

        buildObservable(
            apiService.sendConversationMessage(
                messagesItem
            ), listener
        )
    }

    fun getMyNotifications(
        perPage: Int?,
        pageNumber: Int?,
        sortBy: String?,
        desc: String?,
        trashed:String?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Notification?>?>?>
    ) {
        buildObservable(
            apiService.getMyNotifications(perPage,pageNumber,sortBy,desc,trashed), listener
        )
    }


    fun getMyUnReadNotifications(
        listener: APICommunicatorListener<BaseResponse<ArrayList<Notification?>?>?>
    ) {
        buildObservable(
            apiService.getMyUnReadNotifications(), listener
        )
    }


    fun markAsUnread(
        id: Int,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {

        buildObservable(
            apiService.markAsUnread(
                id
            ), listener
        )
    }


    fun markAllAsUnread(
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {

        buildObservable(
            apiService.markAllAsUnread(
            ), listener
        )
    }


    fun getFAQ(
        listener: APICommunicatorListener<BaseResponse<ArrayList<FAQ?>?>?>
    ) {
        buildObservable(
            apiService.getFAQ(), listener
        )
    }


    fun getOrderNowData(
        orderNowDataRequest: OrderNowDataRequest,
        listener: APICommunicatorListener<BaseResponse<OrderNowViewData?>?>
    ) {
        buildObservable(
            apiService.getOrderNowData(orderNowDataRequest), listener
        )
    }

    fun getUserOrders(
        perPage: Int?,
        pageNumber: Int?,
        listener: APICommunicatorListener<BaseResponse<ArrayList<Order?>?>?>
    ) {
        buildObservable(
            apiService.getUserOrders(perPage, pageNumber), listener
        )
    }

    fun orderCar(
        order: OrderNowRequest,
        listener: APICommunicatorListener<BaseResponse<Any?>?>
    ) {
        buildObservable(
            apiService.orderCar(order), listener
        )
    }

    fun payCarOrder(
        orderId: Int,
        listener: APICommunicatorListener<Payment?>
    ) {
        buildObservable(
            apiService.payCarOrder(orderId), listener
        )
    }

    fun downloadFile(
        url: String,
        listener: APICommunicatorListener<ResponseBody>
    ) {
        buildObservable(
            apiService.downloadFile(url), listener
        )
    }


}