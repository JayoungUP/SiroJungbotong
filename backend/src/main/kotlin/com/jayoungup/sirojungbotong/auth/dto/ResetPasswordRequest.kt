package com.jayoungup.sirojungbotong.auth.dto

data class ResetPasswordRequestByEmail(
    val email: String
)
data class ResetPasswordRequestById(
    val loginId: String
)

data class VerifyRequest(
    val email: String,
    val code: String
)

data class ResetPasswordConfirmRequest(
    val email: String,
    val newPassword: String
)
