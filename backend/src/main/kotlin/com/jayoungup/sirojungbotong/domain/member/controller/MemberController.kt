package com.jayoungup.sirojungbotong.domain.member.controller

import com.jayoungup.sirojungbotong.domain.member.dto.request.*
import com.jayoungup.sirojungbotong.domain.member.dto.response.MemberInfoResponse
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
class MemberController(

    private val memberService: MemberService
) {

    /* 사용자 이메일 회원가입 */
    @PostMapping("/signup/user/email")
    fun signupUserEmail(@RequestBody request: UserEmailSignupRequest): ResponseEntity<Void> {
        memberService.signupUserEmail(request)
        return ResponseEntity.status(201).build()
    }

    /* 사용자 카카오 회원가입 */
    @PostMapping("/signup/user/kakao")
    fun signupUserKakao(@RequestBody request: UserKakaoSignupRequest): ResponseEntity<Void> {
        memberService.signupUserKakao(request)
        return ResponseEntity.status(201).build()
    }   

    /* 점주 이메일 회원가입 */
    @PostMapping("/signup/owner/email")
    fun signupOwnerEmail(@RequestBody request: OwnerEmailSignupRequest): ResponseEntity<Void> {
        memberService.signupOwnerEmail(request)
        return ResponseEntity.status(201).build()
    }

    /* 점주 카카오 회원가입 */
    @PostMapping("/signup/owner/kakao")
    fun signupOwnerKakao(@RequestBody request: OwnerKakaoSignupRequest): ResponseEntity<Void> {
        memberService.signupOwnerKakao(request)
        return ResponseEntity.status(201).build()
    }

    /* 회원 정보 조회 (JWT 인증 필요) */
    @GetMapping("/info")
    fun getMemberInfo(@AuthenticationPrincipal member: Member): ResponseEntity<MemberInfoResponse> {
        val response = memberService.getMemberInfo(member)
        return ResponseEntity.ok(response)
    }
}
