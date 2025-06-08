package com.jayoungup.sirojungbotong.domain.member.dto.request

// 이메일 회원가입 용
data class UserEmailSignupRequest(
    val loginId: String,
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
)

// 카카오 회원가입 용
data class UserKakaoSignupRequest(
    val kakaoAccessToken: String,
    val name: String,
    val nickname: String,
)
