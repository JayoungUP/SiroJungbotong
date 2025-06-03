package com.jayoungup.sirojungbotong.member.mapper

import com.jayoungup.sirojungbotong.member.dto.request.*
import com.jayoungup.sirojungbotong.member.dto.response.LoginResponse
import com.jayoungup.sirojungbotong.member.dto.response.MemberInfoResponse
import com.jayoungup.sirojungbotong.member.entity.Member
import com.jayoungup.sirojungbotong.member.entity.EmailOwner
import com.jayoungup.sirojungbotong.member.entity.EmailUser
import com.jayoungup.sirojungbotong.member.entity.KakaoOwner
import com.jayoungup.sirojungbotong.member.entity.KakaoUser
import org.springframework.stereotype.Component

@Component
class MemberMapper {

    fun toLoginResponse(member: Member, accessToken: String, refreshToken: String): LoginResponse {
        return LoginResponse(
            nickname = member.nickname,
            role = member.role,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun toMemberInfoResponse(member: Member): MemberInfoResponse {
        return when (member) {
            is EmailUser -> MemberInfoResponse(
                name = member.name,
                nickname = member.nickname,
                phoneNumber = member.phoneNumber,
                role = member.role,
                loginId = member.loginId,
                email = member.email,
                bNo = null
            )
            is KakaoUser -> MemberInfoResponse(
                name = member.name,
                nickname = member.nickname,
                phoneNumber = member.phoneNumber,
                role = member.role,
                loginId = null,
                email = null,
                bNo = null
            )
            is EmailOwner -> MemberInfoResponse(
                name = member.name,
                nickname = member.nickname,
                phoneNumber = member.phoneNumber,
                role = member.role,
                loginId = member.loginId,
                email = member.email,
                bNo = member.bNo
            )
            is KakaoOwner -> MemberInfoResponse(
                name = member.name,
                nickname = member.nickname,
                phoneNumber = member.phoneNumber,
                role = member.role,
                loginId = null,
                email = null,
                bNo = member.bNo
            )
            else -> throw IllegalArgumentException("지원하지 않는 Member 타입입니다.")
        }
    }
}
