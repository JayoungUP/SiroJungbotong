package com.jayoungup.sirojungbotong.Member.mapper

import com.jayoungup.sirojungbotong.Member.dto.request.OwnerSignupRequest
import com.jayoungup.sirojungbotong.Member.dto.request.UserSignupRequest
import com.jayoungup.sirojungbotong.Member.dto.response.LoginResponse
import com.jayoungup.sirojungbotong.Member.dto.response.MemberInfoResponse
import com.jayoungup.sirojungbotong.Member.entity.Member
import com.jayoungup.sirojungbotong.Member.entity.Owner
import com.jayoungup.sirojungbotong.Member.entity.User
import org.springframework.stereotype.Component

@Component
class MemberMapper {

    fun toUserEntity(request: UserSignupRequest): User {
        return User(
            loginId = request.loginId,
            email = request.email,
            name = request.name,
            password = request.password,
            nickname = request.nickname
        )
    }

    fun toOwnerEntity(request: OwnerSignupRequest): Owner {
        return Owner(
            loginId = request.loginId,
            email = request.email,
            name = request.name,
            password = request.password,
            nickname = request.nickname,
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
            b_no = if (member is Owner) member.bNo else null
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

