package com.jayoungup.sirojungbotong.domain.store.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalTime

data class StoreUpdateRequestDto(
    @field:Schema(description = "업장명", example = "김밥천국 연남점")
    val name: String,

    @field:Schema(description = "업장 주소", example = "서울 마포구 연남동")
    val address: String,

    @field:Schema(description = "운영 시작 시간", example = "10:00")
    val openTime: LocalTime,

    @field:Schema(description = "운영 종료 시간", example = "20:00")
    val closeTime: LocalTime
)