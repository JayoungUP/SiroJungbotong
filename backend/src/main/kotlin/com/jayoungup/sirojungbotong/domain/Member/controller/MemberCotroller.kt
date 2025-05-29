package com.jayoungup.sirojungbotong.Member.controller

import com.jayoungup.sirojungbotong.Member.dto.request.*
import com.jayoungup.sirojungbotong.Member.dto.response.*
import com.jayoungup.sirojungbotong.Member.service.MemberService
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
