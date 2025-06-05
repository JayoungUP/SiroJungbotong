package com.jayoungup.sirojungbotong.domain.member.controller

import com.jayoungup.sirojungbotong.domain.member.dto.request.LoginRequest
import com.jayoungup.sirojungbotong.domain.member.dto.request.SignupRequest
import com.jayoungup.sirojungbotong.domain.member.dto.response.LoginResponse
import com.jayoungup.sirojungbotong.domain.member.dto.response.MemberInfoResponse
import com.jayoungup.sirojungbotong.domain.member.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
@Tag(name = "Member", description = "회원 관련 API")
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "일반 사용자 또는 사장님 회원가입")
    fun signup(@RequestBody request: SignupRequest): ResponseEntity<Void> {
        memberService.signup(request)
        return ResponseEntity.status(201).build()
    }

    @Operation(summary = "로그인", description = "아이디/비밀번호로 로그인")
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = memberService.login(request)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "회원 정보 조회", description = "현재 로그인된 회원의 정보를 조회합니다.")
    @GetMapping("/info")
    fun getMemberInfo(@RequestAttribute memberId: Long): ResponseEntity<MemberInfoResponse> {
        val response = memberService.getMemberInfo(memberId)
        return ResponseEntity.ok(response)
    }
}