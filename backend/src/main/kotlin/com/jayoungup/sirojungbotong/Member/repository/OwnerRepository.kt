package com.jayoungup.sirojungbotong.Member.repository

import com.jayoungup.sirojungbotong.Member.entity.Owner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository : JpaRepository<Owner, Long> {
    fun existsByBNo(b_no: String): Boolean
}
