package com.tukorea.sirojungbotong.network

import com.tukorea.sirojungbotong.model.Flyer

// 서버 응답 전체
data class AddItemResponse(
    val status: Int,
    val data: ItemData
)

data class CreateFlyerResponse(
    val id: Long
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
