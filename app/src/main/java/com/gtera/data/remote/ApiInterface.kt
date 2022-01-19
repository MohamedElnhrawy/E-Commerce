package com.gtera.data.remote

import com.gtera.data.model.*
import com.gtera.data.model.requests.*
import com.gtera.data.model.response.*
import com.gtera.data.remote.Endpoints.URL_ADD_MY_CAR_SPECS
import com.gtera.data.remote.Endpoints.URL_ADD_NEW_MY_CAR
import com.gtera.data.remote.Endpoints.URL_ANY_MODELS_DATA
import com.gtera.data.remote.Endpoints.URL_BRANCHES
import com.gtera.data.remote.Endpoints.URL_BRANDS
import com.gtera.data.remote.Endpoints.URL_BUDGETS
import com.gtera.data.remote.Endpoints.URL_CALCULATE_IT
import com.gtera.data.remote.Endpoints.URL_CARS
import com.gtera.data.remote.Endpoints.URL_CAR_DETAILS
import com.gtera.data.remote.Endpoints.URL_CAR_INSURANCE_RENEWAL
import com.gtera.data.remote.Endpoints.URL_CITIES
import com.gtera.data.remote.Endpoints.URL_COMPARE
import com.gtera.data.remote.Endpoints.URL_CONTACT_US
import com.gtera.data.remote.Endpoints.URL_CONVERSATION_START
import com.gtera.data.remote.Endpoints.URL_CREATE_ORDER
import com.gtera.data.remote.Endpoints.URL_DELETE_CAR_COMPARE
import com.gtera.data.remote.Endpoints.URL_FAQ
import com.gtera.data.remote.Endpoints.URL_FAVORITES
import com.gtera.data.remote.Endpoints.URL_FAVORITE_UPDATE
import com.gtera.data.remote.Endpoints.URL_FILTER_ATTRIBUTE
import com.gtera.data.remote.Endpoints.URL_GET_CAR_COMPARE
import com.gtera.data.remote.Endpoints.URL_GET_CAR_INSURANCE_RENEWAL
import com.gtera.data.remote.Endpoints.URL_GET_CAR_INSURANCE_RENEWAL_PAY
import com.gtera.data.remote.Endpoints.URL_GET_MY_CAR
import com.gtera.data.remote.Endpoints.URL_GET_NEWS_BYID
import com.gtera.data.remote.Endpoints.URL_GET_OFFER_BYID
import com.gtera.data.remote.Endpoints.URL_GOVERNORATES
import com.gtera.data.remote.Endpoints.URL_HOME
import com.gtera.data.remote.Endpoints.URL_JOB_TITLES
import com.gtera.data.remote.Endpoints.URL_LOG_OUT
import com.gtera.data.remote.Endpoints.URL_MESSAGES
import com.gtera.data.remote.Endpoints.URL_MODELS
import com.gtera.data.remote.Endpoints.URL_MY_CONVERSATION
import com.gtera.data.remote.Endpoints.URL_NOTIFICATIONS
import com.gtera.data.remote.Endpoints.URL_NOTIFICATIONS_MARK_ALL_AS_READ
import com.gtera.data.remote.Endpoints.URL_NOTIFICATIONS_MARK_AS_READ
import com.gtera.data.remote.Endpoints.URL_NOTIFICATIONS_UNREAD
import com.gtera.data.remote.Endpoints.URL_PAY_ORDER
import com.gtera.data.remote.Endpoints.URL_REFRESH_TOKEN
import com.gtera.data.remote.Endpoints.URL_RELATED_NEWS
import com.gtera.data.remote.Endpoints.URL_SEARCH
import com.gtera.data.remote.Endpoints.URL_SEARCH_RELATED
import com.gtera.data.remote.Endpoints.URL_SEND_CONVERSATION_MESSAGE
import com.gtera.data.remote.Endpoints.URL_SIGN_IN_SOCIAL
import com.gtera.data.remote.Endpoints.URL_UPDATE_PROFILE
import com.gtera.data.remote.Endpoints.URL_USER_ORDERS
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.GET



interface ApiInterface {


    @POST
    fun signUp(@Url url: String?, @Body user: User?): Single<BaseResponse<User?>?>?

    @POST
    fun signIn(@Url url: String?, @Body user: User?): Single<BaseResponse<User?>?>?

    @GET(URL_LOG_OUT)
    fun signOut(): Single<BaseResponse<LogOutResponse>?>?

    @POST(URL_SIGN_IN_SOCIAL)
    fun signInWithSocialAccount(@Body socialRequest: SocialLoginRequest?): Single<BaseResponse<User?>?>?

    @POST
    fun changePassword(
        @Url url: String?,
        @Body changePasswordRequest: ChangePasswordRequest?
    ): Single<User?>?

    @POST
    fun sendResetPassword(
        @Url url: String?,
        @Body user: ResetPasswordRequest?
    ): Single<BaseResponse<ResetPassword?>?>?

    @GET(URL_HOME)
    fun getHomeContent(): Single<HomeContent?>?
    @GET(URL_SEARCH)
    fun searchProducts(
//        @Query("name") searchName: String?
    ): Single<BaseResponse<java.util.ArrayList<Product?>?>?>?


    @GET(URL_BRANDS)
    fun searchProducts(
        @Query("name") searchName: String?,
        @Query("perPage") perPage: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("desc") desc: String?,
        @Query("page") pageNumber: Int?
    ): Single<BaseResponse<ArrayList<Brand?>?>?>?



    @GET
    fun getNews(
        @Url url: String?,
        @Query("perPage") perPage: Int?,
        @Query("page") pageNumber: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("desc") desc: String?
    ): Single<BaseResponse<ArrayList<New?>?>?>?

    @GET
    fun getTopDeals(
        @Url url: String?,
        @Query("hot_deal") hotDeal: Boolean?
        ,@Query("sortBy") sortBy: String?
        ,@Query("desc") desc: Int?
    ): Single<BaseResponse<ArrayList<TopDeal?>?>?>?

    @GET
    fun getOffers(
        @Url url: String?,
        @Query("perPage") perPage: Int?,
        @Query("page") pageNumber: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("desc") desc: String?
    ): Single<BaseResponse<ArrayList<Offer?>?>?>?

    @GET(URL_BRANDS)
    fun getBrands(
        @Query("name") searchName: String?,
        @Query("perPage") perPage: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("desc") desc: String?,
        @Query("page") pageNumber: Int?
    ): Single<BaseResponse<ArrayList<Brand?>?>?>?

    @GET(URL_MODELS)
    fun getModels(
        @Query("brand_id") brandId: Int?,
        @Query("perPage") perPage: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("desc") desc: String?
    ): Single<BaseResponse<ArrayList<Model?>?>?>?

    @GET(URL_CARS)
    fun getCars(
        @QueryMap queryMap : Map<String, String>
    ): Single<BaseResponse<ArrayList<Car?>?>?>?

    @GET(URL_CAR_DETAILS)
    fun getCar(
        @Path("id") carId: Int?
    ): Single<BaseResponse<Car?>?>?


    @GET(URL_RELATED_NEWS)
    fun getRelatedNews(
        @Path("id") newId: Int?
    ): Single<BaseResponse<ArrayList<New?>?>?>?

    @GET(URL_BUDGETS)
    fun getBudgets(
        @Query("page") page: Int?,
        @Query("perPage") perPage: Int?
    ): Single<BaseResponse<ArrayList<Budget?>?>?>?

    @POST(URL_COMPARE)
    fun addCarCompare(
        @Body addToCompareRequest: AddRemoveToCompareRequest
    ): Single<BaseResponse<Compare?>?>?

    @HTTP(method = "DELETE", path = URL_DELETE_CAR_COMPARE, hasBody = true)
    fun removeCarCompare(
        @Body removeCompareRequest: AddRemoveToCompareRequest
    ): Single<DeleteCompareResponse?>?


    @GET(URL_GET_CAR_COMPARE)
    fun getCarCompare(
        @Query("sortBy") sortBy: String?,
        @Query("desc") desc: Int?
    ): Single<BaseResponse<ArrayList<Car?>?>?>?

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST(URL_REFRESH_TOKEN)
    fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Call<RefreshTokenResponse?>?

    @POST(URL_UPDATE_PROFILE)
    fun updateProfile(
        @Body updatedUser: User
    ): Single<BaseResponse<User?>?>?

    @GET(URL_GOVERNORATES)
    fun getGovernorates(
    ): Single<BaseResponse<List<Governorate?>?>?>?

    @GET(URL_CITIES)
    fun getCities(
    ): Single<BaseResponse<List<City?>?>?>?

    @GET(URL_FILTER_ATTRIBUTE)
    fun getFiltrationCriteria(
        @Query("used") used: Int?
    ): Single<BaseResponse<List<FiltrationCriteria?>?>?>?

    @GET(URL_SEARCH_RELATED)
    fun getRelatedCarSearch(
    ): Single<BaseResponse<List<Car>?>?>?

    @POST(URL_FAVORITE_UPDATE)
    fun addToFavorites(
        @Body addToFavoritesRequest: FavoritesRequest
    ): Single<FavoriteResponse?>?

    @HTTP(method = "DELETE", path = URL_FAVORITE_UPDATE, hasBody = true)
    fun removeFromFavorites(
        @Body addToFavoritesRequest: FavoritesRequest
    ): Single<FavoriteResponse?>?

    @GET(URL_FAVORITES)
    fun getFavorites(
    ): Single<BaseResponse<List<Favourite?>?>?>?


    @POST(URL_CAR_INSURANCE_RENEWAL)
    fun requestCarRenewalInsurance(
        @Body carRenewalRequest: CarRenewalRequest
    ): Single<FavoriteResponse?>?


    @GET(URL_GET_CAR_INSURANCE_RENEWAL)
    fun getInsuranceRequests(
    ): Single<BaseResponse<List<CarInsuranceRenewal?>?>?>?

    @GET(URL_BRANCHES)
    fun getBranches(
    ): Single<BaseResponse<List<Branche?>?>?>?

    @GET(URL_JOB_TITLES)
    fun getJobTitles(
        @Query("page") page: Int?,
        @Query("perPage") perPage: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("desc") desc: Int?,
        @Query("trashed") trashed: Int?

    ): Single<BaseResponse<ArrayList<JobTitle?>?>?>?


    @POST(URL_CALCULATE_IT)
    fun calculateIt(
       @Body body: HashMap<String,Any>

    ): Single<BaseResponse<ArrayList<Car?>?>?>?

    @POST(URL_CONTACT_US)
    fun contactUs(@Body body:HashMap<String,String>): Single<BaseResponse<Any>>

    @GET(URL_GET_CAR_INSURANCE_RENEWAL_PAY)
    fun renewalInsurancePay(@Path("id") id:Int): Single<Payment?>?

    @GET(URL_GET_NEWS_BYID)
    fun getNewById(@Path("id") id:Int): Single<BaseResponse<New?>>?

    @GET(URL_GET_OFFER_BYID)
    fun getOfferById(@Path("id") id:Int): Single<BaseResponse<Offer?>>?

    @GET(URL_GET_MY_CAR)
    fun getMyCars(
        @Query("page") page: Int?,
        @Query("perPage") perPage: Int?
    ): Single<BaseResponse<ArrayList<Car?>?>?>?

    @GET(URL_ADD_MY_CAR_SPECS)
    fun getMyCarsAddingSpecs(): Single<BaseResponse<CarAddingSpecs?>?>?

    @POST(URL_ADD_NEW_MY_CAR)
    fun addNewMyCar(
        @Body addNewCarRequest: AddNewCarRequest

    ): Single<BaseResponse<Any?>?>?

    @GET(URL_MESSAGES)
    fun getMyMessages(): Single<BaseResponse<ArrayList<Message?>?>?>?

    @POST(URL_CONVERSATION_START)
    fun startConversation(@Body messagesItem: MessagesItem): Single<BaseResponse<Any?>?>?

    @GET(URL_MY_CONVERSATION)
    fun getConversationHistory(@Path("id") id: Int): Single<BaseResponse<Message?>?>?

    @POST(URL_SEND_CONVERSATION_MESSAGE)
    fun sendConversationMessage(@Body messagesItem: MessagesItem): Single<BaseResponse<Any?>?>?

    @GET(URL_NOTIFICATIONS)
    fun getMyNotifications(
        @Query("perPage") perPage: Int?,
        @Query("page") pageNumber: Int?,
        @Query("sortBy") sortBy: String?,
        @Query("desc") desc: String?,
        @Query("trashed") trashed: String?
    ): Single<BaseResponse<ArrayList<Notification?>?>?>?

    @GET(URL_NOTIFICATIONS_UNREAD)
    fun getMyUnReadNotifications(): Single<BaseResponse<ArrayList<Notification?>?>?>?

    @PATCH(URL_NOTIFICATIONS_MARK_AS_READ)
    fun markAsUnread(@Path("id") id: Int): Single<BaseResponse<Any?>?>?

    @PATCH(URL_NOTIFICATIONS_MARK_ALL_AS_READ)
    fun markAllAsUnread(): Single<BaseResponse<Any?>?>?

    @GET(URL_FAQ)
    fun getFAQ(): Single<BaseResponse<ArrayList<FAQ?>?>?>?

    @POST(URL_ANY_MODELS_DATA)
    fun getOrderNowData(@Body orderNowDataRequest: OrderNowDataRequest ): Single<BaseResponse<OrderNowViewData?>?>?

    @GET(URL_USER_ORDERS)
    fun getUserOrders(
        @Query("perPage") perPage: Int?,
        @Query("page") pageNumber: Int?
    ): Single<BaseResponse<ArrayList<Order?>?>?>?

    @POST(URL_CREATE_ORDER)
    fun orderCar(@Body order:OrderNowRequest): Single<BaseResponse<Any?>?>?

    @POST(URL_PAY_ORDER)
    fun payCarOrder( @Path("id") id:Int ): Single<Payment?>?


    @Streaming
    @GET
    fun downloadFile(@Url urlString: String): Single<ResponseBody>

}