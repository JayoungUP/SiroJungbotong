package com.jayoungup.sirojungbotong.auth.service

import com.jayoungup.sirojungbotong.JwtTokenProvider
import com.jayoungup.sirojungbotong.member.dto.request.LoginRequest
import com.jayoungup.sirojungbotong.member.dto.response.LoginResponse
import com.jayoungup.sirojungbotong.member.entity.Member
import com.jayoungup.sirojungbotong.member.mapper.MemberMapper
import com.jayoungup.sirojungbotong.auth.dto.FindIdRequest
import com.jayoungup.sirojungbotong.auth.dto.FindIdResponse
import com.jayoungup.sirojungbotong.auth.dto.ResetPasswordRequestByEmail
import com.jayoungup.sirojungbotong.auth.dto.ResetPasswordConfirmRequest
import com.jayoungup.sirojungbotong.auth.dto.ResetPasswordRequestById
import com.jayoungup.sirojungbotong.auth.dto.VerifyRequest
import com.jayoungup.sirojungbotong.member.repository.EmailUserRepository
import com.jayoungup.sirojungbotong.member.repository.KakaoUserRepository
import com.jayoungup.sirojungbotong.member.repository.EmailOwnerRepository
import com.jayoungup.sirojungbotong.member.repository.KakaoOwnerRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val emailUserRepository: EmailUserRepository,
    private val kakaoUserRepository: KakaoUserRepository,
    private val emailOwnerRepository: EmailOwnerRepository,
    private val kakaoOwnerRepository: KakaoOwnerRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val tokenService: TokenService,
    private val emailService: EmailService,
    private val kakaoService: KakaoService,
    private val memberMapper: MemberMapper
) {
    fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#\$%^&*()_+=-]{8,}$".toRegex()
        return regex.matches(password)
    }

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val member = emailUserRepository.findByLoginId(request.loginId)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")

        if (!passwordEncoder.matches(request.password, member.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        val accessToken = jwtTokenProvider.createAccessToken(member.id, member.role)
        val refreshToken = jwtTokenProvider.createRefreshToken(member.id, member.role)

        tokenService.saveRefreshToken(member.id, refreshToken)

        return memberMapper.toLoginResponse(member, accessToken, refreshToken)
    }

    @Transactional
    fun loginKakao(accessToken: String): LoginResponse {
        val kakaoUserInfo = kakaoService.getUserInfo(accessToken)

        val member = kakaoUserRepository.findByKakaoId(kakaoUserInfo.kakaoId)
            ?: throw IllegalArgumentException("가입되지 않은 카카오 사용자입니다.")

        val accessTokenJwt = jwtTokenProvider.createAccessToken(member.id, member.role)
        val refreshTokenJwt = jwtTokenProvider.createRefreshToken(member.id, member.role)

        tokenService.saveRefreshToken(member.id, refreshTokenJwt)

        return LoginResponse(
            nickname = member.nickname,
            role = member.role,
            accessToken = accessTokenJwt,
            refreshToken = refreshTokenJwt
        )
    }

    @Transactional
    fun logout(accessToken: String) {
        val memberId = jwtTokenProvider.extractMemberId(accessToken)
        tokenService.deleteRefreshToken(memberId)
    }

    @Transactional
    fun refreshToken(accessToken: String, refreshToken: String): LoginResponse {
        val memberId = jwtTokenProvider.extractMemberId(accessToken)

        if (!tokenService.validateRefreshToken(memberId, refreshToken)) {
            throw IllegalArgumentException("유효하지 않은 RefreshToken 입니다.")
        }

        val member = findMemberById(memberId)

        val newAccessToken = jwtTokenProvider.createAccessToken(member.id, member.role)
        val newRefreshToken = jwtTokenProvider.createRefreshToken(member.id, member.role)

        tokenService.saveRefreshToken(member.id, newRefreshToken)

        return memberMapper.toLoginResponse(member, newAccessToken, newRefreshToken)
    }

    private val verifiedEmails = mutableSetOf<String>()
    private val verificationCodes = mutableMapOf<String, String>()

    fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }

    // 인증번호 이메일 발송
    @Transactional
    fun sendVerificationCode(request: ResetPasswordRequestByEmail) {
        val emailUser = emailUserRepository.findByEmail(request.email)
        val emailOwner = emailOwnerRepository.findByEmail(request.email)

        val email = when {
            emailUser != null -> emailUser.email
            emailOwner != null -> emailOwner.email
            else -> throw IllegalArgumentException("등록되지 않은 이메일입니다.")
        }

        val code = generateVerificationCode()
        verificationCodes[email] = code

        emailService.sendVerificationEmail(email, code)
    }
    @Transactional
    fun sendVerificationCode(request: ResetPasswordRequestById) {
        val emailUser = emailUserRepository.findByLoginId(request.loginId)
        val emailOwner = emailOwnerRepository.findByLoginId(request.loginId)

        val email = when {
            emailUser != null -> emailUser.email
            emailOwner != null -> emailOwner.email
            else -> throw IllegalArgumentException("등록되지 않은 아이디입니다.")
        }

        sendVerificationCode(ResetPasswordRequestByEmail(email))
    }

    // 인증번호 검증
    fun verifyCode(request: VerifyRequest) {
        val savedCode = verificationCodes[request.email]
            ?: throw IllegalArgumentException("인증번호가 존재하지 않습니다.")

        if (savedCode != request.code) {
            throw IllegalArgumentException("인증번호가 일치하지 않습니다.")
        }

        verifiedEmails.add(request.email)
        verificationCodes.remove(request.email)
    }

    @Transactional
    fun resetPassword(request: ResetPasswordConfirmRequest) {
        if (!verifiedEmails.contains(request.email)) {
            throw IllegalArgumentException("이메일 인증이 필요합니다.")
        }
        if (!isValidPassword(request.newPassword)) {
            throw IllegalArgumentException("비밀번호는 8자 이상, 영문자와 숫자, 특수문자를 포함해야 합니다.")
        }

        val emailUser = emailUserRepository.findByEmail(request.email)
        if (emailUser != null) {
            emailUser.password = passwordEncoder.encode(request.newPassword)
            verifiedEmails.remove(request.email)
            return
        }

        val emailOwner = emailOwnerRepository.findByEmail(request.email)
        if (emailOwner != null) {
            emailOwner.password = passwordEncoder.encode(request.newPassword)
            verifiedEmails.remove(request.email)
            return
        }

        throw IllegalArgumentException("등록되지 않은 이메일입니다.")
    }
    @Transactional
    fun sendVerificationCodeForIdFind(request: FindIdRequest) {
        val emailUser = emailUserRepository.findByEmail(request.email)
        val emailOwner = emailOwnerRepository.findByEmail(request.email)

        val email = when {
            emailUser != null -> emailUser.email
            emailOwner != null -> emailOwner.email
            else -> throw IllegalArgumentException("등록되지 않은 이메일입니다.")
        }

        val code = generateVerificationCode()
        verificationCodes[email] = code

        emailService.sendVerificationEmail(email, code)
    }

    fun verifyCodeForIdFind(request: VerifyRequest): String {
        val savedCode = verificationCodes[request.email]
            ?: throw IllegalArgumentException("인증번호가 존재하지 않습니다.")

        if (savedCode != request.code) {
            throw IllegalArgumentException("인증번호가 일치하지 않습니다.")
        }

        verificationCodes.remove(request.email)

        val emailUser = emailUserRepository.findByEmail(request.email)
        if (emailUser != null) return emailUser.loginId

        val emailOwner = emailOwnerRepository.findByEmail(request.email)
        if (emailOwner != null) return emailOwner.loginId

        throw IllegalArgumentException("등록되지 않은 이메일입니다.")
    }

    fun findMemberById(memberId: Long): Member {
        emailUserRepository.findById(memberId).orElse(null)?.let { return it }
        kakaoUserRepository.findById(memberId).orElse(null)?.let { return it }
        emailOwnerRepository.findById(memberId).orElse(null)?.let { return it }
        kakaoOwnerRepository.findById(memberId).orElse(null)?.let { return it }

        throw IllegalArgumentException("존재하지 않는 사용자입니다.")
    }
}
