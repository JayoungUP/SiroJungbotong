package com.tukorea.sirojungbotong.network

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.tukorea.sirojungbotong.util.PreferenceUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FlyerApi {
    @GET("flyers/{id}")
    fun getFlyerDetail(@Path("id") id: Long): Call<FlyerDetailResponse>
}

object ApiClient {
    private const val BASE_URL = "http://sirojungbotong.r-e.kr/api/"

    // Retrofit 생성 공통 함수
    private fun getRetrofit(context: Context): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = PreferenceUtil.getAccessToken(context)
                Log.d("TOKEN_CHECK", "accessToken: $token")
                val requestBuilder = chain.request().newBuilder()
                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 기존 서비스 (전단 등록 등)
    fun create(context: Context): SiroApiService {
        return getRetrofit(context).create(SiroApiService::class.java)
    }

    // 전단 상세 조회 API
    fun createFlyerApi(context: Context): FlyerApi {
        return getRetrofit(context).create(FlyerApi::class.java)
    }

}
