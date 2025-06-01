package com.jayoungup.sirojungbotong.domain.flyer.dto

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Data
import java.time.LocalDateTime

@Data
data class FlyerResponseDto(
    @field:Schema(description = "전단지 ID", example = "1")
    val id: Long,

    @field:Schema(description = "가게 이름", example = "김밥천국 신림점")
    val storeName: String,

    @field:Schema(description = "카테고리", example = "음식점")
    val category: String,

    @field:Schema(description = "전단지 설명", example = "할인 이벤트 안내")
    val description: String,

    @field:Schema(description = "전단지 만료일", example = "2025-06-30T23:59:59")
    val expireAt: LocalDateTime,

    @Schema(description = "시로 이용 여부", example = "true")
    val usesSiro: Boolean,

    @Schema(description = "이미지 파일 경로 또는 URL", example = "backend/uploads/20250602_101530_image.png")
    val imageUrl: String?,

    @Schema(description = "전단지 생성 시각", example = "2025-06-02T10:15:30")
    val createdAt: LocalDateTime,

    @Schema(description = "전단지 최종 수정 시각", example = "2025-06-02T10:20:00")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(flyer: Flyer): FlyerResponseDto = FlyerResponseDto(
            flyer.id,
            flyer.storeName,
            flyer.category,
            flyer.description,
            flyer.expireAt,
            flyer.usesSiro,
            flyer.imageUrl,
            flyer.createdAt,
            flyer.updatedAt
        )
    }
}