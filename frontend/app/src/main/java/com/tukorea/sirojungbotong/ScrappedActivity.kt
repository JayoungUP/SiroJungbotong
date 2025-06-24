package com.tukorea.sirojungbotong

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tukorea.sirojungbotong.adapter.FlyerAdapter
import com.tukorea.sirojungbotong.network.ApiClient
import com.tukorea.sirojungbotong.network.Flyer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 내가 스크랩한 전단지 전용 화면
 *
 * XML : activity_scrapped_business.xml
 * API : getScrappedFlyers() / getStoreDetail()
 * 홈화면 구현 방식(HomeActivity)과 동일한 패턴
 */
/**
 * 내가 스크랩한 전단지 전용 화면
 *
 * XML : activity_scrapped_business.xml
 * API : getScrappedFlyers() / getStoreDetail()
 * 홈화면 구현 방식(HomeActivity)과 동일한 패턴
 */
class ScrappedActivity : AppCompatActivity() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var pbLoading: ProgressBar
    private val flyerAdapter = FlyerAdapter() // ✅ 기본 생성자 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrapped_business)

        // ← 뒤로가기
        findViewById<ImageView>(R.id.gotoback).setOnClickListener { finish() }

        // RecyclerView-2열
        rvFavorites = findViewById(R.id.rv_favorites)
        rvFavorites.layoutManager = GridLayoutManager(this, 2)
        rvFavorites.adapter = flyerAdapter

        // 로딩 인디케이터 (레이아웃에 없다면 ProgressBar 하나 추가해 주세요)
        pbLoading = ProgressBar(this).apply { visibility = View.GONE }
        (findViewById<FrameLayout>(R.id.fragment_favorites)).addView(pbLoading)

        loadScrappedFlyers()
    }

    /** API 호출 → 전단지 + 가게이름 매핑 → 어댑터 제출 */
    private fun loadScrappedFlyers() {
        pbLoading.visibility = View.VISIBLE
        val api = ApiClient.create(applicationContext)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val res = api.getScrappedFlyers()
                if (res.isSuccessful && res.body()?.status == 200) {
                    val flyers = res.body()!!.data

                    // storeId ↔ 이름 매핑
                    val storeNameMap = mutableMapOf<Int, String>()
                    flyers.map { it.storeId }.distinct().forEach { id ->
                        val detail = api.getStoreDetail(id).body()
                        if (detail?.status == 200) {
                            storeNameMap[id.toInt()] = detail.data.name
                        }
                    }
                    // 각 Flyer 객체에 가게 이름 주입
                    flyers.forEach { it.storeName = storeNameMap[it.storeId.toInt()] }

                    withContext(Dispatchers.Main) {
                        flyerAdapter.submitList(flyers, storeNameMap) // ✅ 리스트 적용
                        pbLoading.visibility = View.GONE
                    }
                } else {
                    showError("전단지를 불러오지 못했습니다. (${res.code()})")
                }
            } catch (e: Exception) {
                showError("네트워크 오류: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun showError(msg: String) = withContext(Dispatchers.Main) {
        pbLoading.visibility = View.GONE
        Toast.makeText(this@ScrappedActivity, msg, Toast.LENGTH_SHORT).show()
    }
}
