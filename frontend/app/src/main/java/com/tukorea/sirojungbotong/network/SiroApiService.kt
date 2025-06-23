package com.tukorea.sirojungbotong.network

import com.tukorea.sirojungbotong.FlyerDetailResponse
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
        @Part image: MultipartBody.Part?
    ): Response<AddItemResponse>

    @GET("stores/me")
    suspend fun getMyStores(): Response<MyStoresResponse>

    // 추가된 즐겨찾기 목록 가져오기
    @GET("stores/liked")
    suspend fun getLikedStores(): Response<LikedStoresResponse>

    // 즐겨찾기 추가된 개별 가게 상세정보 가져오기
    @GET("stores/{storeId}")
    suspend fun getStoreDetail(@Path("storeId") storeId: Long): Response<StoreDetailResponse>

    // ✅ 전단지 상세 조회 (FlyerDetailActivity 용)
    @GET("flyers/{id}")
    fun getFlyerDetail(@Path("id") id: Long): Call<FlyerDetailResponse>

    // ✅ 이메일 회원가입 관련 API
    @POST("member/signup/user/email")
    suspend fun signupUser(@Body body: UserSignupRequest): Response<Any>

    @POST("member/signup/owner/email")
    suspend fun signupOwner(@Body body: OwnerSignupRequest): Response<Any>
}
