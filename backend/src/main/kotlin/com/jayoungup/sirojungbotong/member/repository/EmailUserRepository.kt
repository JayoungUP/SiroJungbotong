package com.jayoungup.sirojungbotong.member.repository

import com.jayoungup.sirojungbotong.member.entity.EmailOwner
import com.jayoungup.sirojungbotong.member.entity.EmailUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailUserRepository : JpaRepository<EmailUser, Long> {
    fun findByLoginId(loginId: String): EmailUser?
    fun findByEmail(email: String): EmailUser?
    fun existsByLoginId(loginId: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}

