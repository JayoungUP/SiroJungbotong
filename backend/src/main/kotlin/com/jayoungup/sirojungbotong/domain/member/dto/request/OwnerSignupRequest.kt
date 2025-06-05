package com.jayoungup.sirojungbotong.domain.member.dto.request

data class OwnerEmailSignupRequest(
    val loginId: String,
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    val b_no: String,
    val start_dt: String,
    val p_nm: String,
    val p_nm2: String
)


data class OwnerKakaoSignupRequest(
    val kakaoAccessToken: String,
    val name: String,
    val nickname: String,
    val b_no: String,
    val start_dt: String,
    val p_nm: String,
    val p_nm2: String
)

