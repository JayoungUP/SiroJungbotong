package com.jayoungup.sirojungbotong.member.service


import com.jayoungup.sirojungbotong.member.dto.request.*
import com.jayoungup.sirojungbotong.member.dto.response.*
import com.jayoungup.sirojungbotong.member.entity.Member
import com.jayoungup.sirojungbotong.member.mapper.MemberMapper
import com.jayoungup.sirojungbotong.member.repository.*
import com.jayoungup.sirojungbotong.auth.service.BusinessVerificationService
import com.jayoungup.sirojungbotong.auth.service.KakaoService
import com.jayoungup.sirojungbotong.member.entity.EmailOwner
import com.jayoungup.sirojungbotong.member.entity.EmailUser
import com.jayoungup.sirojungbotong.member.entity.KakaoOwner
import com.jayoungup.sirojungbotong.member.entity.KakaoUser
import com.jayoungup.sirojungbotong.member.entity.Role
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val emailUserRepository: EmailUserRepository,
    private val kakaoUserRepository: KakaoUserRepository,
    private val emailOwnerRepository: EmailOwnerRepository,
    private val kakaoOwnerRepository: KakaoOwnerRepository,
    private val businessVerificationService: BusinessVerificationService,
    private val passwordEncoder: PasswordEncoder,
    private val kakaoService: KakaoService,
    private val memberMapper: MemberMapper
) {
    fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#\$%^&*()_+=-]{8,}$".toRegex()
        return regex.matches(password)
    }

    @Transactional
    fun signupUserEmail(request: UserEmailSignupRequest) {
        if (emailUserRepository.existsByLoginId(request.loginId) || emailUserRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일/아이디입니다.")
        }
        if (!isValidPassword(request.password)) {
            throw IllegalArgumentException("비밀번호는 8자 이상, 영문자와 숫자, 특수문자를 포함해야 합니다.")
        }

        val user = EmailUser(
            loginId = request.loginId,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            nickname = request.nickname,
        )
        emailUserRepository.save(user)
    }

    @Transactional
    fun signupUserKakao(request: UserKakaoSignupRequest) {
        val kakaoUserInfo = kakaoService.getUserInfo(request.kakaoAccessToken)

        if (kakaoUserRepository.existsByKakaoId(kakaoUserInfo.kakaoId)) {
            throw IllegalArgumentException("이미 가입된 카카오 사용자입니다.")
        }

        val user = KakaoUser(
            kakaoId = kakaoUserInfo.kakaoId,
            name = request.name,
            nickname = request.nickname,
        )
        kakaoUserRepository.save(user)
    }

    @Transactional
    fun signupOwnerEmail(request: OwnerEmailSignupRequest) {
        if (emailOwnerRepository.existsByLoginId(request.loginId) || emailOwnerRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일/아이디입니다.")
        }
        if (!isValidPassword(request.password)) {
            throw IllegalArgumentException("비밀번호는 8자 이상, 영문자와 숫자, 특수문자를 포함해야 합니다.")
        }

        businessVerificationService.verify(
            bNo = request.b_no,
            startDt = request.start_dt,
            pNm = request.p_nm,
            pNm2 = request.p_nm2
        )

        val owner = EmailOwner(
            loginId = request.loginId,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            nickname = request.nickname,
            bNo = request.b_no
        )
        emailOwnerRepository.save(owner)
    }

    @Transactional
    fun signupOwnerKakao(request: OwnerKakaoSignupRequest) {
        val kakaoUserInfo = kakaoService.getUserInfo(request.kakaoAccessToken)

        if (kakaoOwnerRepository.existsByKakaoId(kakaoUserInfo.kakaoId)) {
            throw IllegalArgumentException("이미 가입된 카카오 사용자입니다.")
        }

        businessVerificationService.verify(
            bNo = request.b_no,
            startDt = request.start_dt,
            pNm = request.p_nm,
            pNm2 = request.p_nm2
        )

        val owner = KakaoOwner(
            kakaoId = kakaoUserInfo.kakaoId,
            name = request.name,
            nickname = request.nickname,
            bNo = request.b_no
        )
        kakaoOwnerRepository.save(owner)
    }

    fun getMemberInfo(memberId: Long): MemberInfoResponse {
        val member = findMemberById(memberId)
        return memberMapper.toMemberInfoResponse(member)
    }

    fun findMemberById(memberId: Long): Member {
        emailUserRepository.findById(memberId).orElse(null)?.let { return it }
        kakaoUserRepository.findById(memberId).orElse(null)?.let { return it }
        emailOwnerRepository.findById(memberId).orElse(null)?.let { return it }
        kakaoOwnerRepository.findById(memberId).orElse(null)?.let { return it }

        throw IllegalArgumentException("존재하지 않는 사용자입니다.")
    }


}

