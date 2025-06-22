package com.tukorea.sirojungbotong.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface SiroApiService {
    @Multipart
    @POST("flyers/{flyerId}/items/addItem")
    suspend fun addItem(
        @Path("flyerId") flyerId: Long,
        @Part image: MultipartBody.Part?,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("price") price: Int,
        @Query("validFrom") validFrom: String,   // yyyy-MM-dd
        @Query("validUntil") validUntil: String  // yyyy-MM-dd
    ): Response<AddItemResponse>

    // 추가된 즐겨찾기 목록 가져오기
    @GET("stores/liked")
    suspend fun getLikedStores(): Response<LikedStoresResponse>

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
}
