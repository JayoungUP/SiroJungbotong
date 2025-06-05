package com.jayoungup.sirojungbotong.domain.member.dto.response

import com.jayoungup.sirojungbotong.domain.member.entity.Role

data class MemberInfoResponse(
    val loginId: String?,
    val email: String?,
    val name: String,
    val nickname: String,
    val role: Role,
    val bNo: String?
)
