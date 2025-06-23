package com.jayoungup.sirojungbotong.domain.flyer.dto

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import java.time.LocalDate
import java.time.LocalDateTime

@Data
data class FlyerResponseDto(
    @field:Schema(description = "전단지 ID", example = "1")
    val id: Long,

    @field:Schema(description = "가게 ID", example = "1")
    val storeId: Long,

    @field:Schema(description = "카테고리", example = "음식점")
    val category: String,

    @field:Schema(description = "전단지 설명", example = "할인 이벤트 안내")
    val description: String,

    @field:Schema(description = "전단지 만료일", example = "2025-06-30")
    val expireAt: LocalDate,

    @field:Schema(description = "시루 이용 여부", example = "true")
    val usesSiro: Boolean,

    @field:Schema(description = "이미지 파일 경로 또는 URL", example = "backend/uploads/flyers/20250602_101530_image.png")
    val imageUrl: String?,

    @field:Schema(description = "전단지 생성 시각", example = "2025-06-02T10:15:30")
    val createdAt: LocalDateTime,

    @field:Schema(description = "전단지 최종 수정 시각", example = "2025-06-02T10:20:00")
    val updatedAt: LocalDateTime,

    @field:Schema(description = "전단지에 포함된 품목 리스트")
    val items: List<ItemResponseDto> = emptyList(),

    @field:Schema(description = "스크랩 수", example = "5")
    val scrapCount: Int
)