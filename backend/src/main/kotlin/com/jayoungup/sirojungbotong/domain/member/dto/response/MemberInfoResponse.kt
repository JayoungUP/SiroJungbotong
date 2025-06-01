package com.jayoungup.sirojungbotong.domain.member.dto.response

import com.jayoungup.sirojungbotong.domain.member.entity.Role
import io.swagger.v3.oas.annotations.media.Schema

data class MemberInfoResponse(
    @Schema(description = "로그인 ID", example = "user123")
    val loginId: String,

    @Schema(description = "이메일 주소", example = "user@example.com")
    val email: String,

    @Schema(description = "이름", example = "홍길동")
    val name: String,

    @Schema(description = "닉네임", example = "길동이")
    val nickname: String,

    @Schema(description = "회원 역할", example = "OWNER")
    val role: Role,

    @Schema(description = "사업자 등록번호 (일반 회원일 경우 null)", example = "123-45-67890", nullable = true)
    val b_no: String?
)