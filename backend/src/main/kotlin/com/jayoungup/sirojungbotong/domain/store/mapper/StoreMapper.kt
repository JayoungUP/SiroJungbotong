package com.jayoungup.sirojungbotong.domain.store.mapper

import com.jayoungup.sirojungbotong.domain.flyer.mapper.FlyerMapper
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.store.dto.StoreCreateRequestDto
import com.jayoungup.sirojungbotong.domain.store.dto.StoreDetailResponseDto
import com.jayoungup.sirojungbotong.domain.store.dto.StoreSimpleResponseDto
import com.jayoungup.sirojungbotong.domain.store.entity.Store


object StoreMapper {

    fun toEntity(dto: StoreCreateRequestDto, imageUrl: String?, owner: Member): Store =
        Store(
            name = dto.name,
            market = dto.market,
            address = dto.address,
            openTime = dto.openTime,
            closeTime = dto.closeTime,
            imageUrl = imageUrl,
            category = dto.category,
            owner = owner,
        )

    fun toDetailDto(store: Store): StoreDetailResponseDto = StoreDetailResponseDto(
        id = store.id,
        ownerId = store.owner.id,
        name = store.name,
        market = store.market,
        address = store.address,
        openTime = store.openTime,
        closeTime = store.closeTime,
        category = store.category,
        imageUrl = store.imageUrl,
        flyers = store.flyers.map { FlyerMapper.toDto(it) },
        likeCount = store.likeCount
    )

    fun toSimpleDto(store: Store): StoreSimpleResponseDto = StoreSimpleResponseDto(
        id = store.id,
        ownerId = store.owner.id,
        name = store.name,
        market = store.market,
        address = store.address,
        openTime = store.openTime,
        closeTime = store.closeTime,
        imageUrl = store.imageUrl,
        category = store.category,
        likeCount = store.likeCount
    )
}