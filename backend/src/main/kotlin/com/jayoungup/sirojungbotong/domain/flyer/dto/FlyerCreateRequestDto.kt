package com.jayoungup.sirojungbotong.domain.flyer.dto

data class FlyerCreateRequestDto(
    val storeName: String,
    val category: String,
    val description: String,
    val expireAt: String,
    val usesSiro: Boolean
)