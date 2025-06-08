package com.jayoungup.sirojungbotong.domain.flyer.service

import com.jayoungup.sirojungbotong.domain.flyer.dto.ItemCreateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.ItemResponseDto
import com.jayoungup.sirojungbotong.domain.flyer.exception.FlyerNotFoundException
import com.jayoungup.sirojungbotong.domain.flyer.exception.ItemNotFoundException
import com.jayoungup.sirojungbotong.domain.flyer.exception.NoFlyerPermissionException
import com.jayoungup.sirojungbotong.domain.flyer.mapper.ItemMapper
import com.jayoungup.sirojungbotong.domain.flyer.repository.FlyerRepository
import com.jayoungup.sirojungbotong.domain.flyer.repository.ItemRepository
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemService(
    private val flyerRepository: FlyerRepository,
    private val itemRepository: ItemRepository
) {

    fun addItemToFlyer(
        member: Member,
        flyerId: Long,
        dto: ItemCreateRequestDto,
        imageUrl: String?
    ): ItemResponseDto {
        val flyer = flyerRepository.findById(flyerId).orElseThrow { FlyerNotFoundException(flyerId) }
        if (flyer.store.owner.id != member.id) throw NoFlyerPermissionException()

        val item = itemRepository.save(ItemMapper.toEntity(dto, imageUrl, flyer))
        return ItemMapper.toDto(item)
    }

    fun getItems(flyerId: Long): List<ItemResponseDto> =
        itemRepository.findAllByFlyerId(flyerId).map(ItemMapper::toDto)

    fun deleteItem(member: Member, itemId: Long) {
        val item = itemRepository.findById(itemId).orElseThrow { ItemNotFoundException(itemId) }
        if (item.flyer.store.owner.id != member.id) throw NoFlyerPermissionException()
        itemRepository.delete(item)
    }
}