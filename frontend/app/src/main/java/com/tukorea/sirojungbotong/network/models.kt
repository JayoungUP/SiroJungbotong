package com.tukorea.sirojungbotong.network

// 서버 응답 전체
data class AddItemResponse(
    val status: Int,
    val data: ItemData
)

data class CreateFlyerResponse(
    val status: Int,
    val data: Flyer
)

data class MarketData(
    val marketName: String,
    val items: List<Flyer>
)

// data 필드 내부
data class ItemData(
    val id: Long,
    val flyerId: Long,
    val name: String,
    val description: String,
    val price: Int,
    val validFrom: String,
    val validUntil: String,
    val imageUrl: String?
)
data class UserSignupRequest(
    val loginId: String,
    val email: String,
    val password: String,
    val name: String,
    val nickname: String
)

data class OwnerSignupRequest(
    val loginId: String,
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    val b_no: String,
    val start_dt: String,  // yyyy-MM-dd 포맷
    val p_nm: String,
    val p_nm2: String?     // 외국인일 때만 값, 아니면 null
)


data class LikedStoresResponse(
    val status: Int,
    val data: List<LikedStore>
)

data class LikedStore(
    val storeId: Long,
    val storeName: String
)

data class StoreDetailResponse(
    val status: Int,
    val data: StoreDetail
)

data class StoreDetail(
    val id: Long,
    val name: String,
    val market: String,
    val address: String,
    val thumbnailUrl: String,
    val imageUrl: String?,
    val flyers: List<Flyer>
)

data class MyStoresResponse(
    val status: Int,
    val data: List<MyStore>
)

data class MyStore(
    val id: Long,
    val ownerId: Long,
    val name: String,
    val market: String,
    val address: String,
    val openTime: String,
    val closeTime: String,
    val category: String?,
    val imageUrl: String?,
    val flyers: List<Flyer>,
    val likeCount: Int
)

data class FlyerListResponse(
    val status: Int,
    val data: FlyerListData
)

data class FlyerListData(
    val content: List<Flyer>,
    val page: PageInfo
)

data class PageInfo(
    val size: Int,
    val number: Int,
    val totalElements: Int,
    val totalPages: Int
)

data class StoreData(
    val id: Long,
    val market: String,
    val address: String
)

data class FlyerDetailResponse(
    val status: Int,
    val data: Flyer
)

data class Flyer(
    val id: Long,
    val storeId: Long,
    val category: String,
    val description: String?,
    val expireAt: String,
    val usesSiro: Boolean,
    val imageUrl: String?,
    val createdAt: String,
    val updatedAt: String,
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