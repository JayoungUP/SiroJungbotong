package com.jayoungup.sirojungbotong.domain.store.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalTime

data class StoreCreateRequestDto(
    @field:Schema(description = "업장명", example = "김밥천국 신림점")
    val name: String,

    @field:Schema(description = "업장 주소", example = "서울 관악구 신림로 1")
    val address: String,

    @field:Schema(description = "운영 시작 시간", example = "09:00")
    val openTime: LocalTime,

    @field:Schema(description = "운영 종료 시간", example = "21:00")
    val closeTime: LocalTime
)