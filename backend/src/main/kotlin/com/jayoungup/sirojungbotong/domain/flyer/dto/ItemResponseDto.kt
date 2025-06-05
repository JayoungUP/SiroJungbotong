package com.jayoungup.sirojungbotong.domain.flyer.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "전단지 품목 응답 DTO")
data class ItemResponseDto(
    @Schema(description = "품목 ID", example = "1")
    val id: Long,

    @Schema(description = "전단지 ID", example = "2")
    val flyerId: Long,

    @Schema(description = "품목명", example = "떡볶이")
    val name: String,

    @Schema(description = "설명", example = "매운 떡볶이")
    val description: String,

    @Schema(description = "가격", example = "3000")
    val price: Int,

    @field:Schema(description = "유효 시작일 (yyyy-MM-dd)", example = "2025-06-01")
    val validFrom: String,

    @field:Schema(description = "유효 종료일 (yyyy-MM-dd)", example = "2025-06-30")
    val validUntil: String,

    @Schema(description = "이미지 경로", example = "backend/uploads/items/123456_item.jpg")
    val imageUrl: String?
)