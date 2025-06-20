package com.jayoungup.sirojungbotong.domain.flyer.service

import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerResponseDto
import com.jayoungup.sirojungbotong.domain.flyer.entity.ScrapFlyer
import com.jayoungup.sirojungbotong.domain.flyer.exception.AlreadyScrappedFlyerException
import com.jayoungup.sirojungbotong.domain.flyer.exception.FlyerNotFoundException
import com.jayoungup.sirojungbotong.domain.flyer.mapper.FlyerMapper
import com.jayoungup.sirojungbotong.domain.flyer.repository.FlyerRepository
import com.jayoungup.sirojungbotong.domain.flyer.repository.ScrapFlyerRepository
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ScrapFlyerService(
    private val flyerRepository: FlyerRepository,
    private val scrapFlyerRepository: ScrapFlyerRepository
) {

    fun scrap(member: Member, flyerId: Long) {
        val flyer = flyerRepository.findById(flyerId)
            .orElseThrow { FlyerNotFoundException(flyerId) }

        if (scrapFlyerRepository.existsByMemberAndFlyer(member, flyer)) {
            throw AlreadyScrappedFlyerException()
        }

        scrapFlyerRepository.save(ScrapFlyer(member = member, flyer = flyer))
    }

    fun unscrap(member: Member, flyerId: Long) {
        val flyer = flyerRepository.findById(flyerId)
            .orElseThrow { FlyerNotFoundException(flyerId) }

        scrapFlyerRepository.deleteByMemberAndFlyer(member, flyer)
    }

    fun getScrappedFlyers(member: Member): List<FlyerResponseDto> {
        return scrapFlyerRepository.findByMember(member)
            .map { FlyerMapper.toDto(it.flyer) }
    }
}