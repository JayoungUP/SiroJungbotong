package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourapp.SignupExtraActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tukorea.sirojungbotong.adapter.FlyerAdapter
import com.tukorea.sirojungbotong.adapter.MarketAdapter
import com.tukorea.sirojungbotong.model.*
import com.tukorea.sirojungbotong.network.ApiClient
import com.tukorea.sirojungbotong.network.Flyer
import com.tukorea.sirojungbotong.network.MarketData
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity() {
    private lateinit var layHome: FrameLayout
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var rvMarkets: RecyclerView
    private lateinit var btnSearch: ImageButton
    private lateinit var btnFilterOn: ImageButton
    private lateinit var btnFilterOff: ImageButton
    private lateinit var btnSortLatest: ImageButton
    private lateinit var btnSortPopular: ImageButton
    private lateinit var layFav: FrameLayout
    private lateinit var layPro: FrameLayout
    private lateinit var rvFavorites: RecyclerView

    private lateinit var tvGreeting: TextView
    private lateinit var layAccountInfo: LinearLayout
    private lateinit var tvUserId: TextView
    private lateinit var imgEditMethod: ImageView
    private lateinit var layOwnerControls: LinearLayout
    private lateinit var btnAddStore: TextView
    private lateinit var btnscrapped: TextView
    private lateinit var btnfavoritemarket:TextView
    private lateinit var btnManageFlyers: TextView
    private lateinit var tvNotLoggedIn: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnResetPassword: Button

    private var isLoggedIn = true // TODO: 로그인 상태 연결 확인
    private var isOwner = true    // TODO: 점주인지 확인

    private var filterState = FilterState()
    private var fullMarketList: List<MarketData> = listOf() // 초기화 추가
    private var currentSortType = SortType.LATEST

    private var storeNameMap: Map<Int, String> = emptyMap()
    private var storeCategoryMap: Map<Int, String> = emptyMap() // 가게 ID → 카테고리 매핑

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homefinal)

        layHome = findViewById(R.id.lay_home)
        layFav = findViewById(R.id.lay_fav)
        layPro = findViewById(R.id.lay_profile)
        bottomNav = findViewById(R.id.bottom_nav)

        // 바인딩
        fabAdd = findViewById(R.id.fab_add)
        btnSearch = findViewById(R.id.btn_search)
        rvMarkets = findViewById(R.id.rv_markets)
        btnFilterOn = findViewById(R.id.btn_filter_on)
        btnFilterOff = findViewById(R.id.btn_filter_off)
        btnSortLatest = findViewById(R.id.btn_latest)
        btnSortPopular = findViewById(R.id.btn_popular)

        // 필터 클릭 시 BottomSheet 열기
        btnFilterOn.setOnClickListener { openFilterSheet() }
        btnFilterOff.setOnClickListener { openFilterSheet() }

        // 정렬 클릭 시 BottomSheet 열기
        btnSortLatest.setOnClickListener { openSortSheet() }
        btnSortPopular.setOnClickListener { openSortSheet() }

        rvFavorites = layFav.findViewById(R.id.rv_favorites)
        rvFavorites.layoutManager = GridLayoutManager(this, 2)

        // 로그인된 경우에만 즐겨찾기 전단지 불러오기
        if (isLoggedIn) {
            fetchFavoriteFlyers()
        }

        updateSortButtons() // 초기 정렬 버튼 표시
        fetchFlyersByMarkets() // 초기 전단지 불러오기

        fabAdd.setOnClickListener {
            startActivity(Intent(this, FlyerUploadActivity::class.java))
        }

        btnSearch.setOnClickListener {
            // 검색 기능 구현 예정
        }

        tvGreeting = layPro.findViewById(R.id.tv_greeting)
        val tvUserName = layPro.findViewById<TextView>(R.id.username)
        val tvOwnerName = layPro.findViewById<TextView>(R.id.ownername)

        layAccountInfo = layPro.findViewById(R.id.lay_account_info)
        tvUserId = layPro.findViewById(R.id.idstring)
        val tvLoginEmail = layPro.findViewById<TextView>(R.id.logintoemail)
        val tvLoginKakao = layPro.findViewById<TextView>(R.id.logintokakao)

        // ----------------------프로필에서의 버튼들---------------------------------------------------
        btnAddStore = layPro.findViewById(R.id.btn_add_store)
        btnManageFlyers = layPro.findViewById(R.id.btn_manage_flyers)
        btnLogout = layPro.findViewById(R.id.btn_logout)
        btnResetPassword = layPro.findViewById(R.id.btn_reset_password)
        btnscrapped=layPro.findViewById(R.id.btn_scrapped)


        btnAddStore.setOnClickListener {
            startActivity(Intent(this, SignupExtraActivity::class.java))
        }
        btnscrapped.setOnClickListener {
            startActivity(Intent(this, ScrappedActivity::class.java))
        }



        btnManageFlyers.setOnClickListener {
            Toast.makeText(this, "아직 구현 중입니다.", Toast.LENGTH_SHORT).show()
        }
        btnLogout.setOnClickListener {
            isLoggedIn = false
            bottomNav.selectedItemId = R.id.nav_home
        }
        btnResetPassword.setOnClickListener {
            startActivity(Intent(this, FindActivity::class.java))
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    layHome.visibility = View.VISIBLE
                    layFav.visibility = View.GONE
                    layPro.visibility = View.GONE
                    fetchFlyersByMarkets()
                    true
                }
                R.id.nav_favorites -> {
                    layHome.visibility = View.GONE
                    layFav.visibility = View.VISIBLE
                    layPro.visibility = View.GONE
                    fetchFavoriteFlyers()
                    true
                }
                R.id.nav_profile -> {
                    if (!isLoggedIn) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        bottomNav.selectedItemId = R.id.nav_home
                        false
                    } else {
                        layHome.visibility = View.GONE
                        layFav.visibility = View.GONE
                        layPro.visibility = View.VISIBLE
                        true
                    }
                }
                else -> false
            }
        }
    }

    private fun fetchFlyersByMarkets() {
        val apiService = ApiClient.create(applicationContext)
        val fetchedMarkets = mutableListOf<MarketData>()
        val nameMap = mutableMapOf<Int, String>()
        val categoryMap = mutableMapOf<Int, String>() // 추가

        // 필터 조건 적용
        val selectedMarkets = if (filterState.selectedMarkets.isEmpty()) {
            listOf("정왕시장", "삼미시장", "도일시장", "오이도전통수산시장")
        } else {
            filterState.selectedMarkets
        }

        val usesSiro = if (filterState.useSiru) true else null
        val categories = filterState.selectedCategories.takeIf { it.isNotEmpty() }
        val sortParam = if (currentSortType == SortType.POPULAR) "scrap" else null

        CoroutineScope(Dispatchers.IO).launch {
            val jobs = selectedMarkets.map { marketName ->
                async {
                    try {
                        val categoryParams = filterState.selectedCategories.takeIf { it.isNotEmpty() }?.toList()

                        Log.d("FILTER_REQ", "요청 → market=$marketName, usesSiro=$usesSiro, categories=$categories")

                        val response = apiService.getFlyers(
                            market = marketName,
                            category = categoryParams,
                            usesSiro = usesSiro,
                            sort = sortParam
                        ).execute()

                        if (response.isSuccessful && response.body()?.status == 200) {
                            val flyers = response.body()!!.data.content

                            flyers.forEach {
                                Log.d("FILTER_RES", "flyerId=${it.id}, category=${it.category}, storeId=${it.storeId}")
                            }

                            // storeId로 가게 이름, 카테고리 매핑
                            val storeJobs = flyers.map { flyer ->
                                async {
                                    val detailRes = apiService.getStoreDetail(flyer.storeId)
                                    if (detailRes.isSuccessful && detailRes.body()?.status == 200) {
                                        val detail = detailRes.body()!!.data
                                        nameMap[flyer.storeId.toInt()] = detail.name
                                        categoryMap[flyer.storeId.toInt()] = detail.category // 가게 카테고리 저장
                                    }
                                }
                            }
                            storeJobs.awaitAll()
                            fetchedMarkets.add(MarketData(marketName, flyers))
                        } else {
                            Log.e("FLYERS_API", "$marketName 실패: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e("FLYERS_API", "$marketName 예외", e)
                    }
                }
            }
            jobs.awaitAll()
            withContext(Dispatchers.Main) {
                fullMarketList = fetchedMarkets
                storeNameMap = nameMap
                storeCategoryMap = categoryMap // 저장
                applyFilter(fullMarketList)
            }
        }
    }

    private fun fetchFavoriteFlyers() {
        val apiService = ApiClient.create(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val likedRes = apiService.getLikedStores()
                if (likedRes.isSuccessful && likedRes.body()?.status == 200) {
                    val likedStores = likedRes.body()!!.data
                    val allFlyers = mutableListOf<Flyer>()
                    val nameMap = mutableMapOf<Int, String>()
                    for (store in likedStores) {
                        val detailRes = apiService.getStoreDetail(store.storeId)
                        if (detailRes.isSuccessful && detailRes.body()?.status == 200) {
                            val detail = detailRes.body()!!.data
                            allFlyers.addAll(detail.flyers)
                            nameMap[store.storeId.toInt()] = detail.name
                        } else {
                            Log.w("FAVORITE_API", "상세 실패: ${store.storeId}")
                        }
                    }
                    storeNameMap = nameMap
                    withContext(Dispatchers.Main) {
                        rvFavorites.adapter = FlyerAdapter(allFlyers, storeNameMap) { flyer ->
                            val intent = Intent(this@HomeActivity, FlyerDetailActivity::class.java)
                            intent.putExtra("flyer_id", flyer.id)
                            startActivity(intent)
                        }
                    }
                } else {
                    Log.e("FAVORITE_API", "즐겨찾기 실패: ${likedRes.code()}")
                }
            } catch (e: Exception) {
                Log.e("FAVORITE_API", "오류 발생", e)
            }
        }
    }

    private fun applyFilter(fullList: List<MarketData>) {
        val filtered = fullList.mapNotNull { market ->
            val filteredFlyers = market.items.filter { flyer ->
                val storeCategory = storeCategoryMap[flyer.storeId.toInt()]
                filterState.selectedCategories.isEmpty() || filterState.selectedCategories.contains(storeCategory)
            }

            if ((filterState.selectedMarkets.isEmpty() || filterState.selectedMarkets.contains(market.marketName)) && filteredFlyers.isNotEmpty()) {
                MarketData(market.marketName, filteredFlyers)
            } else null
        }

        rvMarkets.layoutManager = LinearLayoutManager(this)
        rvMarkets.adapter = MarketAdapter(filtered, storeNameMap) { flyer ->
            val intent = Intent(this, FlyerDetailActivity::class.java)
            intent.putExtra("flyer_id", flyer.id)
            startActivity(intent)
        }
    }

    private fun openFilterSheet() {
        val sheet = FilterBottomSheetFragment(filterState) { newState ->
            filterState = newState
            updateFilterButtonState()
            applyFilter(fullMarketList)
        }
        sheet.show(supportFragmentManager, "FilterSheet")
    }

    private fun openSortSheet() {
        val sheet = SortBottomSheetFragment(currentSortType) { newSort ->
            currentSortType = newSort
            updateSortButtons()
            fetchFlyersByMarkets()
        }
        sheet.show(supportFragmentManager, "SortSheet")
    }

    private fun updateFilterButtonState() {
        val isActive = filterState.useSiru ||
                filterState.selectedMarkets.isNotEmpty() ||
                filterState.selectedCategories.isNotEmpty()

        btnFilterOn.visibility = if (isActive) View.VISIBLE else View.GONE
        btnFilterOff.visibility = if (isActive) View.GONE else View.VISIBLE
    }

    private fun updateSortButtons() {
        btnSortLatest.visibility = if (currentSortType == SortType.LATEST) View.VISIBLE else View.GONE
        btnSortPopular.visibility = if (currentSortType == SortType.POPULAR) View.VISIBLE else View.GONE
    }
}
