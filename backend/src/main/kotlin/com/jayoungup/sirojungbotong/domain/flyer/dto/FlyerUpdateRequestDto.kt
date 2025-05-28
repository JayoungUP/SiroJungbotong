package com.jayoungup.sirojungbotong.domain.flyer.dto

import lombok.Data

@Data
data class FlyerUpdateRequestDto(
    val storeName: String,
    val category: String,
    val description: String,
    val expireAt: String,
    val usesSiro: Boolean
)