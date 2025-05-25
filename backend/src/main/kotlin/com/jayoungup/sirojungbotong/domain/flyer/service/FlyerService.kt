package com.jayoungup.sirojungbotong.domain.flyer.service

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import com.jayoungup.sirojungbotong.domain.flyer.repository.FlyerRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FlyerService(
    private val flyerRepository: FlyerRepository
) {
    fun createFlyer(
        storeName: String,
        category: String,
        description: String,
        expireAt: LocalDateTime,
        usesSiro: Boolean,
        imageUrl: String?
    ): Flyer {
        val flyer = Flyer(
            storeName = storeName,
            category = category,
            description = description,
            expireAt = expireAt,
            usesSiro = usesSiro,
            imageUrl = imageUrl
        )
        return flyerRepository.save(flyer)
    }
}