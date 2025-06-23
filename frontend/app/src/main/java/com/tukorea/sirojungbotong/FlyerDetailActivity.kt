package com.tukorea.sirojungbotong

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.tukorea.sirojungbotong.network.ApiClient
import retrofit2.*
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

        viewPager = findViewById(R.id.viewPagerImages)
        tvImageIndicator = findViewById(R.id.tvImageIndicator)

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

        val imageUrls = mutableListOf<String>()
        imageUrls.add(formatImageUrl(data.imageUrl))
        data.items.forEach {
            imageUrls.add(formatImageUrl(it.imageUrl))
        }

        val adapter = ImageSliderAdapter(imageUrls)
        viewPager.adapter = adapter
        tvImageIndicator.text = "1/${imageUrls.size}"

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tvImageIndicator.text = "${position + 1}/${imageUrls.size}"
            }
        })

        // ✅ 상호명과 주소 출력
        findViewById<TextView>(R.id.tvMarketName).text = data.storeName
        findViewById<TextView>(R.id.tvMarketAddress).text = data.address

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

    private fun formatImageUrl(path: String?): String {
        if (path.isNullOrEmpty()) return ""
        val cleanedPath = path.replace("backend/", "").let {
            if (it.startsWith("/")) it else "/$it"
        }
        return "http://sirojungbotong.r-e.kr$cleanedPath"
    }
}

// Retrofit 인터페이스
interface FlyerApi {
    @GET("flyers/{id}")
    fun getFlyerDetail(@Path("id") id: Long): Call<FlyerDetailResponse>
}

// 응답 모델
data class FlyerDetailResponse(
    val status: Int,
    val data: FlyerData
)

data class FlyerData(
    val id: Long,
    val storeName: String,
    val category: String,
    val address: String,       // ✅ 주소 추가
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