package com.jayoungup.sirojungbotong.domain.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class OwnerSignupRequest (
    @field:Schema(description = "로그인 ID", example = "owner123")
    val loginId: String,

    @field:Schema(description = "이메일 주소", example = "owner@example.com")
    val email: String,

    @field:Schema(description = "이름", example = "김사장")
    val name: String,

    @field:Schema(description = "비밀번호", example = "password")
    val password: String,

    @field:Schema(description = "닉네임", example = "사장님")
    val nickname: String,

    @field:Schema(description = "사업자 등록번호", example = "123-45-67890")
    val b_no: String,
)
