package com.jayoungup.sirojungbotong.domain.store.repository

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.store.entity.LikedStore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikedStoreRepository : JpaRepository<LikedStore, Long> {
    fun findByMember(member: Member): List<LikedStore>
    fun existsByMemberAndStoreId(member: Member, storeId: Long): Boolean
    fun deleteByMemberAndStoreId(member: Member, storeId: Long)
}