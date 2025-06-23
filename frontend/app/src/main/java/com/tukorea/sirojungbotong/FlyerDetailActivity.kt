package com.tukorea.sirojungbotong

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.tukorea.sirojungbotong.network.ApiClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class FlyerDetailActivity : AppCompatActivity() {

    private var flyerId: Long = -1L
    private lateinit var viewPager: ViewPager2
    private lateinit var tvImageIndicator: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flyer_detail)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // 뷰 바인딩
        viewPager = findViewById(R.id.viewPagerImages)
        tvImageIndicator = findViewById(R.id.tvImageIndicator)

        // ✅ 수정된 부분: Int로 받고 Long으로 변환
        val receivedId = intent.getIntExtra("flyer_id", -1)
        flyerId = receivedId.toLong()
        if (receivedId == -1) {
            Toast.makeText(this, "전단지 ID가 전달되지 않았습니다", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchFlyerDetail(flyerId)
    }

    private fun fetchFlyerDetail(id: Long) {
        // 🔹 FlyerApi 사용 (getFlyerDetail 포함)
        val apiService = ApiClient.createFlyerApi(applicationContext)

        apiService.getFlyerDetail(id).enqueue(object : Callback<FlyerDetailResponse> {
            override fun onResponse(
                call: Call<FlyerDetailResponse>,
                response: Response<FlyerDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        Log.d("FlyerDetail_JSON", response.body().toString())
                        updateUI(data)
                    } else {
                        Toast.makeText(this@FlyerDetailActivity, "전단지 정보가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@FlyerDetailActivity, "서버 응답 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FlyerDetailResponse>, t: Throwable) {
                Log.e("FlyerDetail", "API 호출 실패: ${t.message}")
                Toast.makeText(this@FlyerDetailActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(data: FlyerData) {
        val item = data.items.firstOrNull()
        if (item == null) {
            Toast.makeText(this, "상품 정보가 없습니다", Toast.LENGTH_SHORT).show()
            return
        }

        // 🔸 ViewPager 이미지 리스트 구성
        val imageUrls = mutableListOf<String>()
        imageUrls.add("http://sirojungbotong.r-e.kr" + data.imageUrl) // 전단지 메인 이미지
        data.items.forEach {
            imageUrls.add("http://sirojungbotong.r-e.kr" + it.imageUrl) // 각 품목 이미지
        }

        // 🔸 ViewPager 어댑터 연결
        val adapter = ImageSliderAdapter(imageUrls)
        viewPager.adapter = adapter
        tvImageIndicator.text = "1/${imageUrls.size}"

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tvImageIndicator.text = "${position + 1}/${imageUrls.size}"
            }
        })

        // 나머지 텍스트 뷰들
        findViewById<TextView>(R.id.tvMarketName).text = data.storeName
        findViewById<TextView>(R.id.tvMarketAddress).text = "카테고리: ${data.category}"
        findViewById<TextView>(R.id.tvProductName).text = item.name
        findViewById<TextView>(R.id.tvPrice).text = "${item.price}원"
        findViewById<TextView>(R.id.tvInterest).text = "${data.scrapCount}명이 관심을 갖고 있어요"
        findViewById<TextView>(R.id.tvExpiry).text = "유효기간 ${calcRemainDays(item.validUntil)}일 남음"
    }

    private fun calcRemainDays(validUntil: String): Long {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val today = LocalDate.now()
            val expireDate = LocalDate.parse(validUntil, formatter)
            ChronoUnit.DAYS.between(today, expireDate)
        } catch (e: Exception) {
            0L
        }
    }
}

// 🔗 Retrofit 인터페이스
interface FlyerApi {
    @GET("flyers/{id}")
    fun getFlyerDetail(@Path("id") id: Long): Call<FlyerDetailResponse>
}

// 🔍 응답 데이터 모델
data class FlyerDetailResponse(
    val status: Int,
    val data: FlyerData
)

data class FlyerData(
    val id: Long,
    val storeName: String,
    val category: String,
    val imageUrl: String,
    val items: List<FlyerItem>,
    val scrapCount: Int
)

data class FlyerItem(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val validFrom: String,
    val validUntil: String,
    val imageUrl: String
)
