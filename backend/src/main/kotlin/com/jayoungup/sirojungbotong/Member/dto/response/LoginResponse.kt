package com.jayoungup.sirojungbotong.Member.dto.response

import com.jayoungup.sirojungbotong.Member.entity.Role

data class LoginResponse(
    val nickname: String,
    val accessToken: String,
    val refreshToken: String,
    val role: Role
)