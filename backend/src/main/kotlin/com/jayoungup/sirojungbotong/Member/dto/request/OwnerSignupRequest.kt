package com.jayoungup.sirojungbotong.Member.dto.request

data class OwnerSignupRequest (
    val loginId: String,
    val email: String,
    val name: String,
    val password: String,
    val nickname: String,
    val b_no: String,
)