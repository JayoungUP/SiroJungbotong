package com.jayoungup.sirojungbotong.domain.member.repository

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findByLoginId(username: String): Member?

    fun existsByLoginId(loginId: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean

}