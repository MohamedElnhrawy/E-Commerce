package com.gtera.data.remote

import com.gtera.data.model.*
import com.gtera.data.model.requests.*
import com.gtera.data.model.response.*
import com.gtera.data.remote.Endpoints.URL_HOME
import com.gtera.data.remote.Endpoints.URL_REFRESH_TOKEN
import com.gtera.data.remote.Endpoints.URL_SEARCH
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET



interface ApiInterface {

    @GET(URL_HOME)
    fun getHomeContent(): Single<HomeContent?>?
    @GET(URL_SEARCH)

    fun searchProducts(
//        @Query("name") searchName: String?
    ): Single<BaseResponse<java.util.ArrayList<Product?>?>?>?


    @POST(URL_REFRESH_TOKEN)
    fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Call<RefreshTokenResponse?>?

}