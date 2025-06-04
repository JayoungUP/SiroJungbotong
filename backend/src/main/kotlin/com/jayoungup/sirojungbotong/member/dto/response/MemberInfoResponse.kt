package com.jayoungup.sirojungbotong.member.dto.response

import com.jayoungup.sirojungbotong.member.entity.Role

data class MemberInfoResponse(
    val loginId: String?,
    val email: String?,
    val name: String,
    val nickname: String,
    val role: Role,
    val bNo: String?
)
