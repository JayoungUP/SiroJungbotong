package com.jayoungup.sirojungbotong.domain.flyer.dto

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import java.time.LocalDateTime

data class FlyerResponseDto(
    val id: Long,
    val storeName: String,
    val category: String,
    val description: String,
    val expireAt: LocalDateTime,
    val usesSiro: Boolean,
    val imageUrl: String?,
    val createdAt: LocalDateTime,
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