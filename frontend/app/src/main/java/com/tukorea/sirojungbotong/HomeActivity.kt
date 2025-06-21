package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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

    // --- Profile 화면 뷰 ---
    private lateinit var tvGreeting: TextView
    private lateinit var layAccountInfo: LinearLayout
    private lateinit var tvUserId: TextView
    private lateinit var imgEditMethod: ImageView
    private lateinit var layOwnerControls: LinearLayout
    private lateinit var btnAddStore: TextView
    private lateinit var btnManageFlyers: TextView
    private lateinit var tvNotLoggedIn: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnResetPassword: Button
    private var isLoggedIn = true// TODO: 로그인 상태 연결 확// 인
    private var isOwner    = true   // TODO: 점주인지 확인

    private var filterState = FilterState()
    private lateinit var fullMarketList: List<MarketData>
    private var currentSortType = SortType.LATEST

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
        //즐겨찾기 버튼 내용( 실제로 값은 list에 담긴거 가져올꺼임)
        rvFavorites = layFav.findViewById(R.id.rv_favorites)
        rvFavorites.layoutManager = GridLayoutManager(this, 2)
        rvFavorites.adapter = FlyerAdapter(fullMarketList[0].items)
        tvGreeting = layPro.findViewById(R.id.tv_greeting)           // id="@+id/tv_greeting"
        val tvUserName = layPro.findViewById<TextView>(R.id.username)    // id="@+id/username"
        val tvOwnerName = layPro.findViewById<TextView>(R.id.ownername)   // id="@+id/ownername"

        layAccountInfo =
            layPro.findViewById(R.id.lay_account_info)      // id="@+id/lay_account_info"
        tvUserId = layPro.findViewById(R.id.idstring)             // id="@+id/idstring"
        val tvLoginEmail =
            layPro.findViewById<TextView>(R.id.logintoemail) // id="@+id/logintoemail"
        val tvLoginKakao =
            layPro.findViewById<TextView>(R.id.logintokakao) // id="@+id/logintokakao"
        //imgEditMethod    = layPro.findViewById(R.id.iv_edit_method)       // id="@+id/iv_edit_method"
        layOwnerControls =
            layPro.findViewById(R.id.lay_owner_controls)    // id="@+id/lay_owner_controls"
        btnAddStore = layPro.findViewById(R.id.btn_add_store)         // id="@+id/btn_add_store"
        btnManageFlyers =
            layPro.findViewById(R.id.btn_manage_flyers)     // id="@+id/btn_manage_flyers"
        btnLogout = layPro.findViewById(R.id.btn_logout)            // id="@+id/btn_logout"
        btnResetPassword =
            layPro.findViewById(R.id.btn_reset_password)    // id="@+id/btn_reset_password"

        // ----------------------프로필에서의 버튼들---------------------------------------------------
        btnAddStore.setOnClickListener {
            startActivity(Intent(this, MarketListupActivity::class.java))
        }
        btnManageFlyers.setOnClickListener {
            print("아직 구현중")
        }
        btnLogout.setOnClickListener {
            // 로그아웃 처리
            isLoggedIn = false
            bottomNav.selectedItemId = R.id.nav_home
        }
        btnResetPassword.setOnClickListener {
            startActivity(Intent(this, FindActivity::class.java))
        }


        // 하단 네비게이션 처리
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                    R.id.nav_home -> {

                        layHome.visibility = View.VISIBLE
                        layFav.visibility = View.GONE
                        layPro.visibility = View.GONE
                        true
                    }

                    R.id.nav_favorites -> {
                        layHome.visibility = View.GONE
                        layFav.visibility = View.VISIBLE
                        layPro.visibility = View.GONE
                        true
                    }

                    R.id.nav_profile -> {
                        if (!isLoggedIn) {
                                // 로그인 안 됐으면 로그인 화면으로
                                startActivity(Intent(this, LoginActivity::class.java))
                                // 선택 표시를 HOME 으로(또는 아무것도 안 바꿀 거면 false)
                                bottomNav.selectedItemId = R.id.nav_home
                                false
                            } else {
                                // 로그인 된 상태면 프로필 뷰 토글
                                layHome.visibility    = View.GONE
                                layFav.visibility     = View.GONE
                                layPro.visibility     = View.VISIBLE
                                true
                            }
                    }


                    else -> false
                }
            }
    }
        //bottomNav.selectedItemId = R.id.nav_home

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
        if (filtered.size == 1) {
            // MarketData.items 가 List<Flyer> 입니다.
            val flyers = filtered[0].items
            rvMarkets.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            rvMarkets.adapter = FlyerAdapter(filtered[0].items)

        } else {
            // 3) 그 외 모드: 기존 섹션별 리스트
            rvMarkets.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvMarkets.adapter = MarketAdapter(filtered)
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
