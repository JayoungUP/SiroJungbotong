package com.jayoungup.sirojungbotong.global.config.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "공통 응답 래퍼")
data class BaseResponse<T>(
    @field:Schema(description = "HTTP 상태 코드", example = "200")
    val status: Int,

    @field:Schema(description = "응답 데이터", nullable = true)
    val data: T?
)