package com.amir.bagshop.model.net

import com.amir.bagshop.model.data.AddNewCommentResponse
import com.amir.bagshop.model.data.AddToCartResponse
import com.amir.bagshop.model.data.AdsResponse
import com.amir.bagshop.model.data.CheckOut
import com.amir.bagshop.model.data.CommentResponse
import com.amir.bagshop.model.data.LoginResponse
import com.amir.bagshop.model.data.ProductsResponse
import com.amir.bagshop.model.data.SubmitOrder
import com.amir.bagshop.model.data.UserCartInfo
import com.amir.bagshop.model.repository.TokenInMemory
import com.amir.bagshop.util.BASE_URL
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {


    //users
    @POST("signUp")
    suspend fun signUp(@Body jsonObject: JsonObject): LoginResponse

    @POST("signIn")
    suspend fun signIn(@Body jsonObject: JsonObject): LoginResponse


    //refresh token
    @GET("refreshToken")
    fun refreshToken(): Call<LoginResponse>


    //products
    @GET("getProducts")
    suspend fun getAllProducts(): ProductsResponse

    @GET("getSliderPics")
    suspend fun getAllAds(): AdsResponse


    //comments
    @POST("getComments")
    suspend fun getAllComments(@Body jsonObject: JsonObject): CommentResponse

    @POST("addNewComment")
    suspend fun addComment(@Body jsonObject: JsonObject): AddNewCommentResponse


    @POST("addToCart")
    suspend fun addProductToCart(@Body jsonObject: JsonObject): AddToCartResponse


    @GET("getUserCart")
    suspend fun getUserCart(): UserCartInfo


    @POST("removeFromCart")
    suspend fun removeFromCart(@Body jsonObject: JsonObject): AddToCartResponse


    //pay
    @POST("submitOrder")
    suspend fun submitOrder(@Body jsonObject: JsonObject) : SubmitOrder

    @POST("checkout")
    suspend fun checkout(@Body jsonObject: JsonObject) : CheckOut


}


fun createApiService(): ApiService {

    val okHttpClient = OkHttpClient.Builder()

        .addInterceptor {

            val oldRequest = it.request()

            val newRequest = oldRequest.newBuilder()

            if (TokenInMemory.token != null)

                newRequest.addHeader("Authorization", TokenInMemory.token!!)

            newRequest.addHeader("Accept", "application/json")

            newRequest.method(oldRequest.method, oldRequest.body)

            return@addInterceptor it.proceed(newRequest.build())
        }.build()


    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)
}





