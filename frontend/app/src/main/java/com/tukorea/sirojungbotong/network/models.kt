package com.tukorea.sirojungbotong.network

import com.tukorea.sirojungbotong.model.Flyer

// 서버 응답 전체
data class AddItemResponse(
    val status: Int,
    val data: ItemData
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