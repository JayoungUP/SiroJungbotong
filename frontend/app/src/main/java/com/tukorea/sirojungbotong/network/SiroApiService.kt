package com.tukorea.sirojungbotong.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface SiroApiService {
    @Multipart
    @POST("/api/flyers")
    suspend fun createFlyer(
        @Part("storeId") storeId: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part("expireAt") expireAt: RequestBody,
        @Part("usesSiro") usesSiro: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<CreateFlyerResponse>

    @Multipart
    @POST("/api/flyers/{flyerId}/items")
    suspend fun addItem(
        @Path("flyerId") flyerId: Long,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("validFrom") validFrom: RequestBody,
        @Part("validUntil") validUntil: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<AddItemResponse>

    @GET("stores/my")
    suspend fun getMyStores(): Response<MyStoresResponse>

    // 추가된 즐겨찾기 목록 가져오기
    @GET("stores/liked")
    suspend fun getLikedStores(): Response<LikedStoresResponse>

    @POST("stores/liked/{storeId}")
    suspend fun addLikedStore(@Path("storeId") storeId: Long): Response<Void>

    @DELETE("stores/liked/{storeId}")
    suspend fun removeLikedStore(@Path("storeId") storeId: Long): Response<Void>

    // 즐겨찾기 추가된 개별 가게 상세정보 가져오기
    @GET("stores/{storeId}")
    suspend fun getStoreDetail(@Path("storeId") storeId: Long): Response<StoreDetailResponse>
    @POST("member/signup/user/email")
    suspend fun signupUser(
        @Body body: UserSignupRequest
    ): Response<Any>
    @POST("member/signup/owner/email")
    suspend fun signupOwner(
        @Body body: OwnerSignupRequest
    ): Response<Any>

    @Multipart
    @POST("stores")
    suspend fun uploadStore(
        @Part("name") name: RequestBody,
        @Part("market") market: RequestBody,
        @Part("address") address: RequestBody,
        @Part("openTime") openTime: RequestBody,
        @Part("closeTime") closeTime: RequestBody,
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Any> // 또는 실제 응답 객체

    @GET("flyers/{id}")
    fun getFlyerDetail(@Path("id") id: Long): Call<FlyerDetailResponse>

    // 전단 목록 조회 GET 요청
    @GET("flyers")
    fun getFlyers(
        @Query("market") market: String?,
        @Query("category") category: List<String>?,
        @Query("usesSiro") usesSiro: Boolean?,
        @Query("sort") sort: String?,
    ): Call<FlyerListResponse>


    @POST("/api/flyers/scrap/{flyerId}")
    suspend fun scrapFlyer(@Path("flyerId") flyerId: Long): Response<Unit>

    @DELETE("/api/flyers/scrap/{flyerId}")
    suspend fun unscrapFlyer(@Path("flyerId") flyerId: Long): Response<Unit>

    @GET("/api/flyers/scrap")
    suspend fun getScrappedFlyers(): Response<ScrapFlyerListResponse>


}
