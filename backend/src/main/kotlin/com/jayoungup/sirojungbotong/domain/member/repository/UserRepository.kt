package com.jayoungup.sirojungbotong.domain.member.repository

import com.jayoungup.sirojungbotong.domain.member.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

}