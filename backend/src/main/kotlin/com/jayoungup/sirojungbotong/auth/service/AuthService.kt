package com.jayoungup.sirojungbotong.auth.service

import com.jayoungup.sirojungbotong.JwtTokenProvider
import com.jayoungup.sirojungbotong.member.dto.request.LoginRequest
import com.jayoungup.sirojungbotong.member.dto.response.LoginResponse
import com.jayoungup.sirojungbotong.member.entity.Member
import com.jayoungup.sirojungbotong.member.mapper.MemberMapper
import com.jayoungup.sirojungbotong.auth.dto.ResetPasswordRequest
import com.jayoungup.sirojungbotong.auth.dto.ResetPasswordConfirmRequest
import com.jayoungup.sirojungbotong.auth.dto.FindPasswordRequest
import com.jayoungup.sirojungbotong.auth.dto.PasswordVerifyRequest
import com.jayoungup.sirojungbotong.member.repository.EmailUserRepository
import com.jayoungup.sirojungbotong.member.repository.KakaoUserRepository
import com.jayoungup.sirojungbotong.member.repository.EmailOwnerRepository
import com.jayoungup.sirojungbotong.member.repository.KakaoOwnerRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


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

    @Transactional
    fun sendPasswordResetEmail(email: String) {
        val member = emailUserRepository.findByEmail(email)
            ?: throw IllegalArgumentException("존재하지 않는 이메일입니다.")

        val resetToken = UUID.randomUUID().toString()

        tokenService.savePasswordResetToken(email, resetToken)

        emailService.sendVerificationEmail(email, resetToken)
    }


    private val verifiedEmails = mutableSetOf<String>()
    private val verificationCodes = mutableMapOf<String, String>()


    fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }

    // 인증번호 이메일 발송
    @Transactional
    fun sendVerificationCode(request: ResetPasswordRequest) {
        val emailUser = emailUserRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("등록되지 않은 이메일입니다.")

        val code = generateVerificationCode()
        verificationCodes[emailUser.email] = code

        emailService.sendVerificationEmail(emailUser.email, code)
    }

    // 인증번호 검증
    fun verifyCode(request: PasswordVerifyRequest) {
        val savedCode = verificationCodes[request.email]
            ?: throw IllegalArgumentException("인증번호가 존재하지 않습니다.")

        if (savedCode != request.code) {
            throw IllegalArgumentException("인증번호가 일치하지 않습니다.")
        }

        // 인증 성공 시, 인증 상태 저장 (간단히 인증된 이메일 목록 저장)
        verifiedEmails.add(request.email)

        // 인증번호 사용 후 삭제 (원칙)
        verificationCodes.remove(request.email)
    }

    // 비밀번호 재설정
    @Transactional
    fun resetPassword(request: ResetPasswordConfirmRequest) {
        if (!verifiedEmails.contains(request.email)) {
            throw IllegalArgumentException("이메일 인증이 필요합니다.")
        }

        val emailUser = emailUserRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("등록되지 않은 이메일입니다.")

        emailUser.password = passwordEncoder.encode(request.newPassword)
        verifiedEmails.remove(request.email)
    }



    fun findMemberById(memberId: Long): Member {
        emailUserRepository.findById(memberId).orElse(null)?.let { return it }
        kakaoUserRepository.findById(memberId).orElse(null)?.let { return it }
        emailOwnerRepository.findById(memberId).orElse(null)?.let { return it }
        kakaoOwnerRepository.findById(memberId).orElse(null)?.let { return it }

        throw IllegalArgumentException("존재하지 않는 사용자입니다.")
    }

}
