package com.jayoungup.sirojungbotong.auth.dto

data class FindIdRequest(
        val email: String
)
data class FindIdResponse(
        val loginId: String
)
