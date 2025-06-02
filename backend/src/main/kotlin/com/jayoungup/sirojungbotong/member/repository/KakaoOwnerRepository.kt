package com.jayoungup.sirojungbotong.member.repository

import com.jayoungup.sirojungbotong.member.entity.KakaoOwner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KakaoOwnerRepository : JpaRepository<KakaoOwner, Long> {
    fun findByKakaoId(kakaoId: String): KakaoOwner?
    fun existsByKakaoId(kakaoId: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun existsByBNo(bNo: String): Boolean
}
