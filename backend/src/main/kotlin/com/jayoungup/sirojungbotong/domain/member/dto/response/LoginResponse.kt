package com.jayoungup.sirojungbotong.domain.member.dto.response

import com.jayoungup.sirojungbotong.domain.member.entity.Role

data class LoginResponse(
    val nickname: String,
    val accessToken: String,
    val refreshToken: String,
    val role: Role
)