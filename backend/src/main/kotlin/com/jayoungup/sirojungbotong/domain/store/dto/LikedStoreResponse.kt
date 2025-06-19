package com.jayoungup.sirojungbotong.domain.store.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "즐겨찾기된 가게 응답 DTO")
data class LikedStoreResponse(
    @Schema(description = "가게 ID", example = "1")
    val storeId: Long,

    @Schema(description = "가게 이름", example = "스타벅스 강남점")
    val storeName: String
)