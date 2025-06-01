package com.jayoungup.sirojungbotong.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class UserSignupRequest (
    @Schema(description = "로그인 ID", example = "user123")
    val loginId: String,

    @Schema(description = "이메일 주소", example = "user@example.com")
    val email: String,

    @Schema(description = "이름", example = "홍길동")
    val name: String,

    @Schema(description = "비밀번호", example = "password")
    val password: String,

    @Schema(description = "닉네임", example = "길동이")
    val nickname: String,
)