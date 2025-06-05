package com.jayoungup.sirojungbotong.domain.member.repository

import com.jayoungup.sirojungbotong.domain.member.entity.KakaoUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KakaoUserRepository : JpaRepository<KakaoUser, Long> {
    fun findByKakaoId(kakaoId: String): KakaoUser?
    fun existsByKakaoId(kakaoId: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}
