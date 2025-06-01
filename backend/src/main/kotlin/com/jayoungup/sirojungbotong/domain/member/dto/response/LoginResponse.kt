package com.jayoungup.sirojungbotong.domain.member.dto.response

import com.jayoungup.sirojungbotong.domain.member.entity.Role
import io.swagger.v3.oas.annotations.media.Schema


data class LoginResponse(
    @Schema(description = "사용자 닉네임", example = "길동이")
    val nickname: String,

    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    val accessToken: String,

    @Schema(description = "JWT Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR...R2")
    val refreshToken: String,

    @Schema(description = "사용자 역할", example = "USER")
    val role: Role
)