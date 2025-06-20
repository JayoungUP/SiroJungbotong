package com.jayoungup.sirojungbotong.domain.store.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.time.LocalTime

data class StoreUpdateRequestDto(
    @field:Schema(description = "업장명", example = "김밥천국 정왕점")
    @field:NotBlank(message = "업장명을 입력해주세요.")
    val name: String,

    @field:Schema(description = "시장명", example = "정왕시장")
    @field:NotBlank(message = "시장명을 입력해주세요.")
    val market: String,

    @field:Schema(description = "업장 주소", example = "경기도 시흥시 정왕동 11")
    @field:NotBlank(message = "주소를 입력해주세요.")
    val address: String,

    @field:Schema(description = "운영 시작 시간", example = "10:00")
    val openTime: LocalTime,

    @field:Schema(description = "운영 종료 시간", example = "20:00")
    val closeTime: LocalTime
)