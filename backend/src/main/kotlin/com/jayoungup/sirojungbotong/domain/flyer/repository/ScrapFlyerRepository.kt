package com.jayoungup.sirojungbotong.domain.flyer.repository

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import com.jayoungup.sirojungbotong.domain.flyer.entity.ScrapFlyer
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface ScrapFlyerRepository : JpaRepository<ScrapFlyer, Long> {
    fun existsByMemberAndFlyer(member: Member, flyer: Flyer): Boolean
    fun deleteByMemberAndFlyer(member: Member, flyer: Flyer)
    fun findByMember(member: Member): List<ScrapFlyer>
}