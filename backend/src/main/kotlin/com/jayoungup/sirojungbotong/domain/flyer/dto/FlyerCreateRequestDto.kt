package com.jayoungup.sirojungbotong.domain.flyer.dto

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data

@Data
data class FlyerCreateRequestDto(
    @field:Schema(description = "업장 ID", example = "1")
    val storeId: Long,

    @field:Schema(description = "카테고리", example = "음식점")
    val category: String,

    @field:Schema(description = "전단지 설명", example = "정왕역 1번 출구에서 100m! 할인 이벤트 중!")
    val description: String,

    @field:Schema(description = "만료일 (yyyy-MM-dd 형태)", example = "2025-06-30")
    val expireAt: String,

    @field:Schema(description = "시로 이용 여부", example = "true")
    val usesSiro: Boolean
)