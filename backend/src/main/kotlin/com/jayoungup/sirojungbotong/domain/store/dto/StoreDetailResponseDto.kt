package com.jayoungup.sirojungbotong.domain.store.dto

import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerResponseDto
import com.jayoungup.sirojungbotong.domain.store.entity.Store
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalTime

data class StoreDetailResponseDto(
    @field:Schema(description = "업장 ID", example = "1")
    val id: Long,

    @field:Schema(description = "사장 회원 ID", example = "2")
    val ownerId: Long,

    @field:Schema(description = "업장명", example = "김밥천국 정왕점")
    val name: String,

    @field:Schema(description = "시장명", example = "정왕시장")
    val market: String,

    @field:Schema(description = "업장 주소", example = "경기도 시흥시 정왕동")
    val address: String,

    @field:Schema(description = "운영 시작 시간", example = "09:00")
    val openTime: LocalTime,

    @field:Schema(description = "운영 종료 시간", example = "21:00")
    val closeTime: LocalTime,

    @field:Schema(description = "업장 카테고리", example = "분식")
    val category: String,

    @field:Schema(description = "업장 이미지 경로", example = "backend/uploads/stores/1720038880000_store.jpg")
    val imageUrl: String?,

    @field:Schema(description = "업장 전단지 목록")
    val flyers: List<FlyerResponseDto>,

    @field:Schema(description = "좋아요 수", example = "17")
    val likeCount: Int
)