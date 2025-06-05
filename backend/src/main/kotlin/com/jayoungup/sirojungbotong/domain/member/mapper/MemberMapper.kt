package com.jayoungup.sirojungbotong.domain.member.mapper


import com.jayoungup.sirojungbotong.domain.member.dto.request.SignupRequest
import com.jayoungup.sirojungbotong.domain.member.dto.response.LoginResponse
import com.jayoungup.sirojungbotong.domain.member.dto.response.MemberInfoResponse
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.member.entity.Role
import org.springframework.stereotype.Component

@Component
class MemberMapper {

    fun toSignupEntity(request: SignupRequest): Member {
        val role = if (request.b_no.isNullOrBlank()) Role.USER else Role.OWNER
        return Member(
            loginId = request.loginId,
            email = request.email,
            name = request.name,
            password = request.password,
            nickname = request.nickname,
            role = role,
            bNo = request.b_no
        )
    }

    fun toMemberInfoResponse(member: Member): MemberInfoResponse {
        return MemberInfoResponse(
            loginId = member.loginId,
            email = member.email,
            nickname = member.nickname,
            name = member.name,
            role = member.role,
            b_no = member.bNo
        )
    }

    fun toLoginResponse(member: Member, accessToken: String, refreshToken: String): LoginResponse {
        return LoginResponse(
            nickname = member.nickname,
            role = member.role,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}

