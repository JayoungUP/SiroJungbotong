package com.jayoungup.sirojungbotong.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class LoginRequest(
    @field:Schema(description = "로그인 ID", example = "user123")
    val loginId: String,

    @field:Schema(description = "비밀번호", example = "password1234")
    val password: String,
)