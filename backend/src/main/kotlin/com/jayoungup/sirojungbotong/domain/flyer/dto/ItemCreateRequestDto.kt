package com.jayoungup.sirojungbotong.domain.flyer.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ItemCreateRequestDto(
    @field:Schema(description = "품목명", example = "떡볶이")
    val name: String,

    @field:Schema(description = "품목 설명", example = "매콤달콤한 떡볶이")
    val description: String,

    @field:Schema(description = "가격", example = "3000")
    val price: Int,

    @field:Schema(description = "유효 시작일 (yyyy-MM-dd)", example = "2025-06-01")
    val validFrom: String,

    @field:Schema(description = "유효 종료일 (yyyy-MM-dd)", example = "2025-06-30")
    val validUntil: String
)