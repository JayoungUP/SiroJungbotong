package com.tukorea.sirojungbotong.network

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
