package com.jayoungup.sirojungbotong.auth.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class TokenService {

    private val refreshTokenStore: MutableMap<Long, String> = ConcurrentHashMap()
    private val passwordResetTokenStore: MutableMap<String, String> = ConcurrentHashMap()

    fun saveRefreshToken(memberId: Long, refreshToken: String) {
        refreshTokenStore[memberId] = refreshToken
    }

    fun getRefreshToken(memberId: Long): String? {
        return refreshTokenStore[memberId]
    }

    fun deleteRefreshToken(memberId: Long) {
        refreshTokenStore.remove(memberId)
    }

    fun validateRefreshToken(memberId: Long, refreshToken: String): Boolean {
        return refreshTokenStore[memberId] == refreshToken
    }

    /* PasswordResetToken 저장 */
    fun savePasswordResetToken(email: String, resetToken: String) {
        passwordResetTokenStore[email] = resetToken
    }

    /* PasswordResetToken 검증 */
    fun validatePasswordResetToken(email: String, resetToken: String): Boolean {
        return passwordResetTokenStore[email] == resetToken
    }

    /* PasswordResetToken 삭제 */
    fun deletePasswordResetToken(email: String) {
        passwordResetTokenStore.remove(email)
    }
}
