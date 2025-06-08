package com.jayoungup.sirojungbotong.domain.member.repository

import com.jayoungup.sirojungbotong.domain.member.entity.EmailOwner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailOwnerRepository : JpaRepository<EmailOwner, Long> {
    fun findByLoginId(loginId: String): EmailOwner?
    fun findByEmail(email: String): EmailOwner?
    fun existsByLoginId(loginId: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun existsByBNo(bNo: String): Boolean
}
