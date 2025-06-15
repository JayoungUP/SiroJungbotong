package com.jayoungup.sirojungbotong.domain.flyer.mapper

import com.jayoungup.sirojungbotong.domain.flyer.dto.ItemCreateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.ItemResponseDto
import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import com.jayoungup.sirojungbotong.domain.flyer.entity.Item
import java.time.LocalDate

object ItemMapper {
    fun toEntity(dto: ItemCreateRequestDto, imageUrl: String?, flyer: Flyer): Item =
        Item(
            name = dto.name,
            description = dto.description,
            price = dto.price,
            validFrom = LocalDate.parse(dto.validFrom),
            validUntil = LocalDate.parse(dto.validUntil),
            imageUrl = imageUrl,
            flyer = flyer
        )

    fun toDto(item: Item): ItemResponseDto =
        ItemResponseDto(
            id = item.id,
            flyerId = item.flyer.id,
            name = item.name,
            description = item.description,
            price = item.price,
            validFrom = item.validFrom.toString(),
            validUntil = item.validUntil.toString(),
            imageUrl = item.imageUrl
        )
}