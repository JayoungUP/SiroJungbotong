package com.jayoungup.sirojungbotong.domain.flyer.dto

import lombok.Data

@Data
data class FlyerCreateRequestDto(
    val storeName: String,
    val category: String,
    val description: String,
    val expireAt: String,
    val usesSiro: Boolean
)