package com.jayoungup.sirojungbotong.auth.dto

data class ResetPasswordRequest(
    val email: String
)

data class PasswordVerifyRequest(
    val email: String,
    val code: String
)

data class ResetPasswordConfirmRequest(
    val email: String,
    val newPassword: String
)

