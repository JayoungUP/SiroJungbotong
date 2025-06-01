package com.jayoungup.sirojungbotong.domain.flyer.dto

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data

@Data
data class FlyerUpdateRequestDto(
    @Schema(description = "가게 이름", example = "김밥천국 신림점")
    val storeName: String,

    @Schema(description = "카테고리", example = "음식점")
    val category: String,

    @Schema(description = "전단지 설명", example = "신메뉴 출시 기념 이벤트 중!")
    val description: String,

    @Schema(description = "만료일 (yyyy-MM-dd 형태)", example = "2025-07-15")
    val expireAt: String,

    @Schema(description = "시로 이용 여부", example = "false")
    val usesSiro: Boolean
)