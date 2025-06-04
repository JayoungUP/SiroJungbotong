package com.jayoungup.sirojungbotong.domain.flyer.mapper

import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerCreateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerResponseDto
import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import com.jayoungup.sirojungbotong.domain.store.entity.Store
import java.time.LocalDateTime


object FlyerMapper {

    fun toEntity(dto: FlyerCreateRequestDto, imageUrl: String?, store: Store): Flyer =
        Flyer(
            store = store,
            category = dto.category,
            description = dto.description,
            expireAt = LocalDateTime.parse(dto.expireAt),
            usesSiro = dto.usesSiro,
            imageUrl = imageUrl
        )


    fun toDto(flyer: Flyer): FlyerResponseDto = FlyerResponseDto(
        id = flyer.id,
        storeId = flyer.store.id,
        category = flyer.category,
        description = flyer.description,
        expireAt = flyer.expireAt,
        usesSiro = flyer.usesSiro,
        imageUrl = flyer.imageUrl,
        createdAt = flyer.createdAt,
        updatedAt = flyer.updatedAt
    )
}