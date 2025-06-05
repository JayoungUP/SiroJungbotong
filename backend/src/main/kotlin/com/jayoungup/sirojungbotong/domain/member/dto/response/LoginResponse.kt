package com.jayoungup.sirojungbotong.domain.member.dto.response

import com.jayoungup.sirojungbotong.domain.member.entity.Role
import io.swagger.v3.oas.annotations.media.Schema


data class LoginResponse(
    @field:Schema(description = "사용자 닉네임", example = "길동이")
    val nickname: String,

    @field:Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    val accessToken: String,

    @field:Schema(description = "JWT Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR...R2")
    val refreshToken: String,

    @field:Schema(description = "사용자 역할", example = "USER")
    val role: Role
)