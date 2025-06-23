package com.tukorea.sirojungbotong.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApi {
    @GET("stores/{id}")
    fun getStoreDetail(@Path("id") id: Long): Call<StoreDetailResponse>
}