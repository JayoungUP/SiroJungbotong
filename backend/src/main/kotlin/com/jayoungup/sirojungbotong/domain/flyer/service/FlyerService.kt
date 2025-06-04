package com.jayoungup.sirojungbotong.domain.flyer.service

import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerCreateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerUpdateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import com.jayoungup.sirojungbotong.domain.flyer.exception.FlyerNotFoundException
import com.jayoungup.sirojungbotong.domain.flyer.exception.NoFlyerPermissionException
import com.jayoungup.sirojungbotong.domain.flyer.mapper.FlyerMapper
import com.jayoungup.sirojungbotong.domain.flyer.repository.FlyerRepository
import com.jayoungup.sirojungbotong.domain.store.exception.StoreNotFoundException
import com.jayoungup.sirojungbotong.domain.store.repository.StoreRepository
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class FlyerService(
    private val flyerRepository: FlyerRepository,
    private val storeRepository: StoreRepository
) {

    fun createFlyer(member: Member, dto: FlyerCreateRequestDto, imageUrl: String?): Flyer {
        val store = storeRepository.findById(dto.storeId)
            .orElseThrow { StoreNotFoundException() }

        if (store.owner.id != member.id) throw NoFlyerPermissionException()

        val flyer = FlyerMapper.toEntity(dto, imageUrl, store)
        return flyerRepository.save(flyer)
    }

    @Transactional(readOnly = true)
    fun getFlyer(id: Long): Flyer =
        flyerRepository.findById(id).orElseThrow { FlyerNotFoundException(id) }

    @Transactional(readOnly = true)
    fun getAllFlyers(): List<Flyer> = flyerRepository.findAll()

    fun updateFlyerText(member: Member, id: Long, dto: FlyerUpdateRequestDto): Flyer {
        val flyer = flyerRepository.findById(id).orElseThrow { FlyerNotFoundException(id) }

        if (flyer.store.owner.id != member.id) throw NoFlyerPermissionException()

        flyer.category = dto.category
        flyer.description = dto.description
        flyer.expireAt = LocalDateTime.parse(dto.expireAt)
        flyer.usesSiro = dto.usesSiro
        flyer.updatedAt = LocalDateTime.now()

        return flyerRepository.save(flyer)
    }

    fun updateFlyerImage(member: Member, id: Long, imageUrl: String): Flyer {
        val flyer = flyerRepository.findById(id).orElseThrow { FlyerNotFoundException(id) }

        if (flyer.store.owner.id != member.id) throw NoFlyerPermissionException()

        flyer.imageUrl = imageUrl
        flyer.updatedAt = LocalDateTime.now()

        return flyerRepository.save(flyer)
    }

    fun deleteFlyer(member: Member, id: Long) {
        val flyer = flyerRepository.findById(id).orElseThrow { FlyerNotFoundException(id) }

        if (flyer.store.owner.id != member.id) throw NoFlyerPermissionException()

        flyerRepository.delete(flyer)
    }
}