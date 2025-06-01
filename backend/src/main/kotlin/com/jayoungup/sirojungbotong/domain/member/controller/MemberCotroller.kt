package com.jayoungup.sirojungbotong.domain.member.controller


import com.jayoungup.sirojungbotong.domain.member.dto.request.LoginRequest
import com.jayoungup.sirojungbotong.domain.member.dto.request.OwnerSignupRequest
import com.jayoungup.sirojungbotong.domain.member.dto.request.UserSignupRequest
import com.jayoungup.sirojungbotong.domain.member.dto.response.LoginResponse
import com.jayoungup.sirojungbotong.domain.member.dto.response.MemberInfoResponse
import com.jayoungup.sirojungbotong.domain.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("/signup/user")
    fun signupUser(@RequestBody request: UserSignupRequest): ResponseEntity<Void> {
        memberService.signupUser(request)
        return ResponseEntity.status(201).build()
    }

    @PostMapping("/signup/owner")
    fun signupOwner(@RequestBody request: OwnerSignupRequest): ResponseEntity<Void> {
        memberService.signupOwner(request)
        return ResponseEntity.status(201).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = memberService.login(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/info")
    fun getMemberInfo(@RequestAttribute memberId: Long): ResponseEntity<MemberInfoResponse> {
        val response = memberService.getMemberInfo(memberId)
        return ResponseEntity.ok(response)
    }
}
