package com.jayoungup.sirojungbotong.Member.dto.request

data class UserSignupRequest (
    val loginId: String,
    val email: String,
    val name: String,
    val password: String,
    val nickname: String,
)