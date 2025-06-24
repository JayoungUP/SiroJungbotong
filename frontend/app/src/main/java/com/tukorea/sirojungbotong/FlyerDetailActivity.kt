package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.tukorea.sirojungbotong.network.ApiClient
import com.tukorea.sirojungbotong.network.Flyer
import com.tukorea.sirojungbotong.network.FlyerDetailResponse
import com.tukorea.sirojungbotong.network.StoreDetail
import com.tukorea.sirojungbotong.network.StoreDetailResponse
import kotlinx.coroutines.launch
import retrofit2.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class FlyerDetailActivity : AppCompatActivity() {

    private var isScrapped = false
    private var flyerId: Long = -1L
    private lateinit var viewPager: ViewPager2
    private lateinit var tvImageIndicator: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flyer_detail)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        viewPager = findViewById(R.id.viewPagerImages)
        tvImageIndicator = findViewById(R.id.tvImageIndicator)

        // 수정: Long 타입으로 받아야 함
        val receivedId = intent.getLongExtra("flyer_id", -1L)
        flyerId = receivedId
        if (receivedId == -1L) {
            Toast.makeText(this, "전단지 ID가 전달되지 않았습니다", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchFlyerDetail(flyerId)
        fetchScrapStatus(flyerId)
    }

    private fun fetchFlyerDetail(id: Long) {
        val apiService = ApiClient.createFlyerApi(applicationContext)

        apiService.getFlyerDetail(id).enqueue(object : Callback<FlyerDetailResponse> {
            override fun onResponse(call: Call<FlyerDetailResponse>, response: Response<FlyerDetailResponse>) {
                if (response.isSuccessful) {
                    val flyerData = response.body()?.data
                    if (flyerData != null) {
                        Log.d("FlyerDetail_JSON", flyerData.toString())
                        lifecycleScope.launch {
                            fetchStoreDetailAndUpdate(flyerData)
                        }
                    }
                } else {
                    Toast.makeText(this@FlyerDetailActivity, "전단지 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FlyerDetailResponse>, t: Throwable) {
                Log.e("FlyerDetail", "전단지 API 호출 실패: ${t.message}")
                Toast.makeText(this@FlyerDetailActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private suspend fun fetchStoreDetailAndUpdate(flyerData: Flyer) {
        val storeService = ApiClient.create(applicationContext)
        try {
            val response = storeService.getStoreDetail(flyerData.storeId)
            if (response.isSuccessful) {
                val store = response.body()?.data
                updateUI(flyerData, store)
            } else {
                updateUI(flyerData, null)
            }
        } catch (e: Exception) {
            Log.e("FlyerDetail", "가게 API 실패: ${e.message}")
            updateUI(flyerData, null)
        }
    }


        private fun updateUI(flyerData: Flyer, storeData: StoreDetail?) {
        val item = flyerData.items.firstOrNull()
        if (item == null) {
            Toast.makeText(this, "상품 정보가 없습니다", Toast.LENGTH_SHORT).show()
            return
        }

        val imageUrls = mutableListOf<String>()
        imageUrls.add(formatImageUrl(flyerData.imageUrl))
        flyerData.items.forEach {
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

        findViewById<TextView>(R.id.tvMarketName).text = storeData?.name ?: "가게명 없음"
        findViewById<TextView>(R.id.tvMarketAddress).text = storeData?.address ?: "주소 없음"
        findViewById<TextView>(R.id.tvProductName).text = item.name
        findViewById<TextView>(R.id.tvPrice).text = "${item.price}원"
        findViewById<TextView>(R.id.tvInterest).text = "${flyerData.scrapCount}명이 관심을 갖고 있어요"
        findViewById<TextView>(R.id.tvExpiry).text = "유효기간 ${calcRemainDays(item.validUntil)}일 남음"

        // 가게 썸네일 이미지 로드
        val ivMarketThumb = findViewById<ImageView>(R.id.ivMarketThumb)
        Glide.with(this)
            .load(formatImageUrl(storeData?.imageUrl))
            .placeholder(R.drawable.sample_thumbnail)
            .into(ivMarketThumb)

        // ⭐️ 스크랩 기능
        val ivScrap = findViewById<ImageView>(R.id.ivScrap)
        ivScrap.setImageResource(if (isScrapped) R.drawable.ic_full_star else R.drawable.ic_empty_star)

        ivScrap.setOnClickListener {
            isScrapped = !isScrapped
            ivScrap.setImageResource(
                if (isScrapped) R.drawable.ic_full_star else R.drawable.ic_empty_star
            )

            Toast.makeText(this, if (isScrapped) "스크랩 추가됨" else "스크랩 해제됨", Toast.LENGTH_SHORT).show()

            val api = ApiClient.create(applicationContext)
            lifecycleScope.launch {
                try {
                    val response = if (isScrapped) {
                        api.scrapFlyer(flyerId)
                    } else {
                        api.unscrapFlyer(flyerId)
                    }

                    if (!response.isSuccessful) {
                        Toast.makeText(this@FlyerDetailActivity, "서버 에러", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("SCRAP", "요청 실패: ${e.message}")
                    Toast.makeText(this@FlyerDetailActivity, "요청 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 가게 상세 이동
        val layout = findViewById<LinearLayout>(R.id.layoutStoreInfo)
        layout.setOnClickListener {
            val intent = Intent(this, StoreDetailActivity::class.java)
            intent.putExtra("store_id", flyerData.storeId)
            startActivity(intent)
        }
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
        val cleanedPath = path.replace("backend/", "").replace("/home/juno/app/", "")
        val finalPath = if (cleanedPath.startsWith("/")) cleanedPath else "/$cleanedPath"
        return "http://sirojungbotong.r-e.kr$finalPath"
    }

    private fun fetchScrapStatus(flyerId: Long) {
        val api = ApiClient.create(applicationContext)
        lifecycleScope.launch {
            try {
                val response = api.getScrappedFlyers()
                if (response.isSuccessful) {
                    val flyerList = response.body()?.data ?: emptyList()
                    isScrapped = flyerList.any { it.id == flyerId }

                    val ivScrap = findViewById<ImageView>(R.id.ivScrap)
                    ivScrap.setImageResource(
                        if (isScrapped) R.drawable.ic_full_star else R.drawable.ic_empty_star
                    )
                }
            } catch (e: Exception) {
                Log.e("SCRAP", "스크랩 상태 확인 실패: ${e.message}")
            }
        }
    }
}