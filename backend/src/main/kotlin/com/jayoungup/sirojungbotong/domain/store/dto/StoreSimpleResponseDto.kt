package com.jayoungup.sirojungbotong.domain.store.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalTime

data class StoreSimpleResponseDto(
    @field:Schema(description = "업장 ID", example = "1")
    val id: Long,

    @field:Schema(description = "사장 회원 ID", example = "2")
    val ownerId: Long,

    @field:Schema(description = "업장명", example = "김밥천국 신림점")
    val name: String,

    @field:Schema(description = "시장명", example = "정왕시장")
    val market: String,

    @field:Schema(description = "업장 주소", example = "서울 관악구 신림로 1")
    val address: String,

    @field:Schema(description = "운영 시작 시간", example = "09:00")
    val openTime: LocalTime,

    @field:Schema(description = "운영 종료 시간", example = "21:00")
    val closeTime: LocalTime,

    @field:Schema(description = "업장 이미지 경로", example = "backend/uploads/stores/1720038880000_store.jpg")
    val imageUrl: String?,

    @field:Schema(description = "사업자 등록 서류 경로", example = "backend/uploads/stores/1720038880000_bizdoc.jpg")
    val businessDocumentUrl: String?
)