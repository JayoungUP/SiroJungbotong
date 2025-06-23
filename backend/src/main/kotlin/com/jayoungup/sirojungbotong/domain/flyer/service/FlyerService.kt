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
import com.jayoungup.sirojungbotong.global.util.DeeplTranslator
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class FlyerService(
    private val flyerRepository: FlyerRepository,
    private val storeRepository: StoreRepository,
    private val deeplTranslator: DeeplTranslator
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

    fun getTranslatedFlyerMap(flyer: Flyer): Map<String, Any> {
        val langs = listOf("KO", "EN", "ZH")
        val result = mutableMapOf<String, Any>()

        langs.forEach { lang ->
            result[lang.lowercase()] = mapOf(
                "category" to deeplTranslator.translate(flyer.category, lang),
                "description" to deeplTranslator.translate(flyer.description, lang),
                "items" to flyer.items.map {
                    mapOf(
                        "name" to deeplTranslator.translate(it.name, lang),
                        "description" to deeplTranslator.translate(it.description, lang)
                    )
                }
            )
        }

        return result
    }


    @Transactional(readOnly = true)
    fun getFlyersFiltered(
        market: String?,
        category: String?,
        usesSiro: Boolean?,
        sort: String,
        pageable: Pageable
    ): Page<Flyer> {
        return when (sort) {
            "scrap" -> {
                val filtered = flyerRepository.findAllFilteredForManualSorting(market, category, usesSiro)
                val sorted = filtered.sortedByDescending { it.scrapCount }
                val start = pageable.offset.toInt()
                val end = (start + pageable.pageSize).coerceAtMost(sorted.size)
                val pageContent = if (start >= sorted.size) emptyList() else sorted.subList(start, end)
                PageImpl(pageContent, pageable, sorted.size.toLong())
            }

            else -> flyerRepository.findAllFilteredForSorting(market, category, usesSiro, pageable)
        }
    }

    fun updateFlyerText(member: Member, id: Long, dto: FlyerUpdateRequestDto): Flyer {
        val flyer = flyerRepository.findById(id).orElseThrow { FlyerNotFoundException(id) }

        if (flyer.store.owner.id != member.id) throw NoFlyerPermissionException()

        flyer.category = dto.category
        flyer.description = dto.description
        flyer.expireAt = LocalDate.parse(dto.expireAt)
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