package com.jayoungup.sirojungbotong.Member.repository

import com.jayoungup.sirojungbotong.Member.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

}