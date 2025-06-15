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
}
