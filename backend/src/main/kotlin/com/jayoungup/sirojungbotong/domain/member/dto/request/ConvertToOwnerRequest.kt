package com.jayoungup.sirojungbotong.domain.member.dto.request

data class ConvertToOwnerRequest(
    val bNo: String,
    val startDt: String,
    val pNm: String,
    val pNm2: String? = null
)
