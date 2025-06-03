package com.jayoungup.sirojungbotong.member.dto.response

import com.jayoungup.sirojungbotong.member.entity.Role

data class LoginResponse(
    val nickname: String,
    val accessToken: String,
    val refreshToken: String,
    val role: Role
)