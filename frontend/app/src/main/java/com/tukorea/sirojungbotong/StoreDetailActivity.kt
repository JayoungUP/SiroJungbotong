package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tukorea.sirojungbotong.adapter.FlyerAdapter
import com.tukorea.sirojungbotong.network.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreDetailActivity : AppCompatActivity() {

    private lateinit var ivFavorite: ImageView
    private var isLiked = false
    private var currentStoreId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_detail)

        currentStoreId = intent.getLongExtra("store_id", -1L)
        if (currentStoreId == -1L) {
            Toast.makeText(this, "가게 정보가 없습니다", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }
        ivFavorite = findViewById(R.id.ivFavorite)

        // 즐겨찾기 초기 상태 확인
        checkIfLiked(currentStoreId)

        ivFavorite.setOnClickListener {
            toggleFavorite(currentStoreId)
        }

        fetchStoreDetail(currentStoreId)
    }

    private fun checkIfLiked(storeId: Long) {
        val api = ApiClient.create(applicationContext)
        lifecycleScope.launch {
            try {
                val response = api.getLikedStores()
                if (response.isSuccessful) {
                    val isInList = response.body()?.data?.any { it.storeId.toLong() == storeId } == true
                    isLiked = isInList
                    updateFavoriteIcon()
                }
            } catch (e: Exception) {
                Log.e("StoreDetail", "즐겨찾기 확인 실패: ${e.message}")
            }
        }
    }

    private fun toggleFavorite(storeId: Long) {
        val api = ApiClient.create(applicationContext)
        lifecycleScope.launch {
            try {
                val response = if (isLiked) {
                    api.removeLikedStore(storeId)
                } else {
                    api.addLikedStore(storeId)
                }

                if (response.isSuccessful) {
                    isLiked = !isLiked
                    updateFavoriteIcon()
                } else {
                    Toast.makeText(this@StoreDetailActivity, "서버 오류", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("StoreDetail", "즐겨찾기 토글 실패: ${e.message}")
                Toast.makeText(this@StoreDetailActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFavoriteIcon() {
        ivFavorite.setImageResource(
            if (isLiked) R.drawable.ic_full_star else R.drawable.ic_empty_star
        )
    }

    private fun fetchStoreDetail(storeId: Long) {
        val api = ApiClient.create(applicationContext)
        lifecycleScope.launch {
            try {
                val response = api.getStoreDetail(storeId)
                if (response.isSuccessful) {
                    val store = response.body()?.data
                    if (store != null) updateUI(store)
                } else {
                    Toast.makeText(this@StoreDetailActivity, "가게 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("StoreDetail", "API 실패: ${e.message}")
                Toast.makeText(this@StoreDetailActivity, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(store: StoreDetail) {
        findViewById<TextView>(R.id.tvStoreName).text = store.name
        findViewById<TextView>(R.id.tvMarket).text = store.market
        findViewById<TextView>(R.id.tvAddress).text = store.address
        findViewById<TextView>(R.id.tvTime).text = "${store.openTime} ~ ${store.closeTime}"
        findViewById<TextView>(R.id.tvCategory).text = store.category
        findViewById<TextView>(R.id.tvLikeCount).text = "관심 ${store.likeCount}명"

        Glide.with(this)
            .load(formatImageUrl(store.imageUrl))
            .placeholder(R.drawable.sample_thumbnail)
            .into(findViewById(R.id.ivStoreImage))

        val storeNameMap = store.flyers.associate { it.storeId.toInt() to store.name }

        val recyclerView = findViewById<RecyclerView>(R.id.rvFlyers)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = FlyerAdapter(store.flyers, storeNameMap) { flyer ->
            val intent = Intent(this, FlyerDetailActivity::class.java)
            intent.putExtra("flyer_id", flyer.id)
            startActivity(intent)
        }
    }

    private fun formatImageUrl(path: String?): String {
        if (path.isNullOrEmpty()) return ""
        val cleaned = path.replace("backend/", "").replace("/home/juno/app/", "")
        val finalPath = if (cleaned.startsWith("/")) cleaned else "/$cleaned"
        return "http://sirojungbotong.r-e.kr$finalPath"
    }
}

