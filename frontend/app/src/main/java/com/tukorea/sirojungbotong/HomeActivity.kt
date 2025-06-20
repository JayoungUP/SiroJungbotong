package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tukorea.sirojungbotong.model.Flyer
import com.tukorea.sirojungbotong.model.FlyerItem
import com.tukorea.sirojungbotong.model.MarketData
import com.tukorea.sirojungbotong.model.FilterState
import com.tukorea.sirojungbotong.model.SortType

class HomeActivity : AppCompatActivity() {

    private lateinit var fabAdd: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var rvMarkets: RecyclerView
    private lateinit var btnSearch: ImageButton
    private lateinit var btnFilterOn: ImageButton
    private lateinit var btnFilterOff: ImageButton
    private lateinit var btnSortLatest: ImageButton
    private lateinit var btnSortPopular: ImageButton

    private var filterState = FilterState()
    private lateinit var fullMarketList: List<MarketData>
    private var currentSortType = SortType.LATEST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        // 바인딩
        fabAdd = findViewById(R.id.fab_add)
        bottomNav = findViewById(R.id.bottom_nav)
        btnSearch = findViewById(R.id.btn_search)
        rvMarkets = findViewById(R.id.rv_markets)
        btnFilterOn = findViewById(R.id.btn_filter_on)
        btnFilterOff = findViewById(R.id.btn_filter_off)
        btnSortLatest = findViewById(R.id.btn_latest)
        btnSortPopular = findViewById(R.id.btn_popular)

        rvMarkets.layoutManager = LinearLayoutManager(this)

        // 필터 클릭 시 BottomSheet 열기
        btnFilterOn.setOnClickListener { openFilterSheet() }
        btnFilterOff.setOnClickListener { openFilterSheet() }

        // 정렬 클릭 시 BottomSheet 열기
        btnSortLatest.setOnClickListener { openSortSheet() }
        btnSortPopular.setOnClickListener { openSortSheet() }

        // 더미 데이터 준비
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
                    imageUrl = "https://picsum.photos/300",
                    category = "농산물"
                )
            )
        )

        fullMarketList = listOf(
            MarketData("정왕시장", List(10) { dummyFlyer }),
            MarketData("삼미시장", List(10) { dummyFlyer }),
            MarketData("도일시장", List(10) { dummyFlyer }),
            MarketData("오이도전통수산시장", List(10) { dummyFlyer })
        )

        updateSortButtons() // ✅ 초기 정렬 버튼 표시
        applyFilter(fullMarketList)

        // FloatingActionButton 클릭 이벤트
        fabAdd.setOnClickListener {
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

    private fun applyFilter(fullList: List<MarketData>) {
        val filtered = fullList.filter { market ->
            val matchesMarket =
                filterState.selectedMarkets.isEmpty() || filterState.selectedMarkets.contains(market.marketName)

            val matchesCategory = market.items.any { flyer ->
                flyer.items.any { item ->
                    filterState.selectedCategories.isEmpty() || filterState.selectedCategories.contains(item.category)
                }
            }

            matchesMarket && matchesCategory
        }.let { list ->
            when (currentSortType) {
                SortType.LATEST -> list // 추후 최신순 정렬
                SortType.POPULAR -> list // 추후 인기순 정렬
            }
        }

        rvMarkets.adapter = MarketAdapter(filtered)
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
            applyFilter(fullMarketList)
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
