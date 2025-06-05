package com.jayoungup.sirojungbotong.auth.controller


import com.jayoungup.sirojungbotong.auth.dto.FindIdRequest
import com.jayoungup.sirojungbotong.auth.dto.FindIdResponse
import com.jayoungup.sirojungbotong.auth.dto.VerifyRequest
import com.jayoungup.sirojungbotong.auth.dto.ResetPasswordConfirmRequest
import com.jayoungup.sirojungbotong.auth.dto.ResetPasswordRequestByEmail
import com.jayoungup.sirojungbotong.auth.dto.ResetPasswordRequestById
import com.jayoungup.sirojungbotong.auth.service.AuthService
import com.jayoungup.sirojungbotong.member.dto.request.LoginRequest
import com.jayoungup.sirojungbotong.member.dto.response.LoginResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    /* 이메일 로그인 */
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }

    /* 카카오 로그인 */
    @PostMapping("/login/kakao")
    fun loginKakao(@RequestBody accessToken: String): ResponseEntity<LoginResponse> {
        val response = authService.loginKakao(accessToken)
        return ResponseEntity.ok(response)
    }

    /* 로그아웃 */
    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") bearer: String): ResponseEntity<Void> {
        authService.logout(bearer.removePrefix("Bearer "))
        return ResponseEntity.ok().build()
    }

    /* 토큰 재발급 */
    @PostMapping("/token/refresh")
    fun refreshToken(
        @RequestHeader("Authorization") bearer: String,
        @RequestBody refreshToken: String
    ): ResponseEntity<LoginResponse> {
        val response = authService.refreshToken(
            accessToken = bearer.removePrefix("Bearer "),
            refreshToken = refreshToken
        )
        return ResponseEntity.ok(response)
    }
    @PostMapping("/password/findByEmail")
    fun sendVerificationCode(@RequestBody request: ResetPasswordRequestByEmail): ResponseEntity<Void> {
        authService.sendVerificationCode(request)
        return ResponseEntity.ok().build()
    }
    @PostMapping("/password/findById")
    fun sendVerificationCodeById(@RequestBody request: ResetPasswordRequestById): ResponseEntity<Void> {
        authService.sendVerificationCode(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/password/verify")
    fun verifyCode(@RequestBody request: VerifyRequest): ResponseEntity<Void> {
        authService.verifyCode(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/password/reset")
    fun resetPassword(@RequestBody request: ResetPasswordConfirmRequest): ResponseEntity<Void> {
        authService.resetPassword(request)
        return ResponseEntity.ok().build()
    }
    @PostMapping("/id/find")
    fun sendVerificationCodeForIdFind(@RequestBody request: FindIdRequest): ResponseEntity<Void> {
        authService.sendVerificationCodeForIdFind(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/id/verify")
    fun verifyCodeForIdFind(@RequestBody request: VerifyRequest): ResponseEntity<FindIdResponse> {
        val response = authService.verifyCodeForIdFind(request)
        return ResponseEntity.ok(FindIdResponse(loginId = response))
    }



}
