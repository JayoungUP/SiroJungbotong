package com.jayoungup.sirojungbotong.domain.flyer.service

import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerCreateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerUpdateRequestDto

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import com.jayoungup.sirojungbotong.domain.flyer.exception.FlyerNotFoundException
import com.jayoungup.sirojungbotong.domain.flyer.repository.FlyerRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FlyerService(
    private val flyerRepository: FlyerRepository
) {

    fun createFlyer(dto: FlyerCreateRequestDto, imageUrl: String?): Flyer {
        val flyer = Flyer(
            storeName = dto.storeName,
            category = dto.category,
            description = dto.description,
            expireAt = LocalDateTime.parse(dto.expireAt),
            usesSiro = dto.usesSiro,
            imageUrl = imageUrl
        )
        return flyerRepository.save(flyer)
    }

    fun getFlyer(id: Long): Flyer =
        flyerRepository.findById(id).orElseThrow {
            FlyerNotFoundException(id)
        }

    fun getAllFlyers(): List<Flyer> = flyerRepository.findAll()

    fun updateFlyerText(id: Long, dto: FlyerUpdateRequestDto): Flyer {
        val flyer = flyerRepository.findById(id).orElseThrow {
            FlyerNotFoundException(id)
        }

        flyer.storeName = dto.storeName
        flyer.category = dto.category
        flyer.description = dto.description
        flyer.expireAt = LocalDateTime.parse(dto.expireAt)
        flyer.usesSiro = dto.usesSiro
        flyer.updatedAt = LocalDateTime.now()

        return flyerRepository.save(flyer)
    }

    fun updateFlyerImage(id: Long, imageUrl: String): Flyer {
        val flyer = flyerRepository.findById(id).orElseThrow {
            FlyerNotFoundException(id)
        }

        flyer.imageUrl = imageUrl
        flyer.updatedAt = LocalDateTime.now()

        return flyerRepository.save(flyer)
    }

    fun deleteFlyer(id: Long) {
        flyerRepository.findById(id).orElseThrow {
            FlyerNotFoundException(id)
        }
        flyerRepository.deleteById(id)
    }
}