package com.jayoungup.sirojungbotong.Member.dto.response

import com.jayoungup.sirojungbotong.Member.entity.Role

data class MemberInfoResponse(
    val loginId: String,
    val email: String,
    val name: String,
    val nickname: String,
    val role: Role,
    val b_no: String?
)