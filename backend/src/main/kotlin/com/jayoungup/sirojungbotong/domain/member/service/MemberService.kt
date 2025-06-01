package com.jayoungup.sirojungbotong.domain.member.service

import com.jayoungup.sirojungbotong.domain.member.dto.request.LoginRequest
import com.jayoungup.sirojungbotong.domain.member.dto.request.OwnerSignupRequest
import com.jayoungup.sirojungbotong.domain.member.dto.request.UserSignupRequest
import com.jayoungup.sirojungbotong.domain.member.dto.response.LoginResponse
import com.jayoungup.sirojungbotong.domain.member.dto.response.MemberInfoResponse
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.member.mapper.MemberMapper
import com.jayoungup.sirojungbotong.domain.member.repository.MemberRepository
import com.jayoungup.sirojungbotong.domain.member.repository.OwnerRepository
import com.jayoungup.sirojungbotong.domain.member.repository.UserRepository
import com.jayoungup.sirojungbotong.domain.member.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val userRepository: UserRepository,
    private val ownerRepository: OwnerRepository,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberMapper: MemberMapper
) {


    @Transactional
    fun signupUser(request: UserSignupRequest) {
        if (memberRepository.existsByLoginId(request.loginId)){
            throw IllegalArgumentException("이미 사용 중인 아이디입니다.")
        }
        if (memberRepository.existsByEmail(request.email)){
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }
        if (memberRepository.existsByNickname(request.nickname)){
            throw IllegalArgumentException("이미 사용 중인 닉네임입니다.")
        }

        val encodedPassword = passwordEncoder.encode(request.password)
        val user = memberMapper.toUserEntity(request.copy(password = encodedPassword))
        userRepository.save(user)

    }
    @Transactional
    fun signupOwner(request: OwnerSignupRequest) {
        if (memberRepository.existsByLoginId(request.loginId)){
            throw IllegalArgumentException("이미 사용 중인 아이디입니다.")
        }
        if (memberRepository.existsByEmail(request.email)){
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }
        if (memberRepository.existsByNickname(request.nickname)){
            throw IllegalArgumentException("이미 사용 중인 닉네임입니다.")
        }
        if (ownerRepository.existsByBNo(request.b_no)){
            throw IllegalArgumentException("이미 등록된 사업자등록번호입니다.")
        }

        val encodedPassword = passwordEncoder.encode(request.password)
        val owner = memberMapper.toOwnerEntity(request.copy(password = encodedPassword))
        ownerRepository.save(owner)
    }
    @Transactional(readOnly = true)
    fun login(request: LoginRequest): LoginResponse {
        val member : Member = memberRepository.findByLoginId(request.loginId)
            ?: throw IllegalArgumentException("존재하지 않는 사용하입니다..")
        if (!passwordEncoder.matches(request.password, member.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        val accessToken = jwtTokenProvider.createAccessToken(member.id, member.role)
        val refreshToken = jwtTokenProvider.createRefreshToken(member.id, member.role)

        return memberMapper.toLoginResponse(member, accessToken, refreshToken)
    }

    fun getMemberInfo(memberId: Long): MemberInfoResponse {
        val member = memberRepository.findById(memberId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 사용자입니다.") }

        return memberMapper.toMemberInfoResponse(member)
    }

}
