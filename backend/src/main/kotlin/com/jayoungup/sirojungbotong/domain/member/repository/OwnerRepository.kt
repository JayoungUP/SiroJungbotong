package com.jayoungup.sirojungbotong.domain.member.repository

import com.jayoungup.sirojungbotong.domain.member.entity.Owner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : JpaRepository<Owner, Long> {
    fun existsByBNo(b_no: String): Boolean
}
