package com.jayoungup.sirojungbotong.member.dto.response

import com.jayoungup.sirojungbotong.member.entity.Role

data class MemberInfoResponse(
    val loginId: String?,  // nullable
    val email: String?,    // nullable
    val name: String,
    val nickname: String,
    val phoneNumber: String,
    val role: Role,
    val bNo: String?       // 원래 nullable → OK
)
