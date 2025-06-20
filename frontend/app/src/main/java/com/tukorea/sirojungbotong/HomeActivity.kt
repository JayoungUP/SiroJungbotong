package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tukorea.sirojungbotong.model.Flyer
import com.tukorea.sirojungbotong.model.FlyerItem
import com.tukorea.sirojungbotong.model.MarketData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class HomeActivity : AppCompatActivity() {

    private lateinit var fabAdd: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var rvMarkets: RecyclerView
    private lateinit var btnSearch: ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val dummyFlyer = Flyer(
            id = 1,
            storeId = 1,
            description = "샘플 전단지",
            imageUrl = "",
            items = listOf(
                FlyerItem(
                    id = 1,
                    flyerId = 1,
                    name = "토마토 8개",
                    price = 6000,
                    imageUrl = "https://picsum.photos/300"
                )
            )
        )

        val dummyData = listOf(
            MarketData("정왕시장", List(10) { dummyFlyer }),
            MarketData("삼미시장", List(10) { dummyFlyer }),
            MarketData("도일시장", List(10) { dummyFlyer }),
            MarketData("오이도전통수산시장", List(10) { dummyFlyer })
        )

        val rvMarkets = findViewById<RecyclerView>(R.id.rv_markets)
        rvMarkets.layoutManager = LinearLayoutManager(this)
        rvMarkets.adapter = MarketAdapter(dummyData)

        // 바인딩
        fabAdd = findViewById(R.id.fab_add)
        bottomNav = findViewById(R.id.bottom_nav)
        btnSearch = findViewById(R.id.btn_search)



        // FloatingActionButton 클릭 이벤트
        fabAdd.setOnClickListener {
            // 예: 업로드 화면으로 이동
            val intent = Intent(this, MarketListupActivity::class.java)
            startActivity(intent)
        }

        // 검색 버튼 클릭 이벤트
        btnSearch.setOnClickListener {
            //val intent = Intent(this, SearchActivity::class.java)
            //startActivity(intent)
        }

        // 하단 네비게이션 처리
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_favorites -> {
                    //startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    //startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
