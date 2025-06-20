package com.jayoungup.sirojungbotong.global.config.security

import com.jayoungup.sirojungbotong.domain.member.exception.MemberNotFoundException
import com.jayoungup.sirojungbotong.domain.member.repository.MemberRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI

        // 화이트리스트 경로: JWT 검사 건너뛰기
        if (requestURI.startsWith("/api/auth/password/findById") ||
            requestURI.startsWith("/api/auth/password/findByEmail") ||
            requestURI.startsWith("/api/auth/password/reset") ||
            requestURI.startsWith("/api/auth/login") ||
            requestURI.startsWith("/api/auth/login/kakao") ||
            requestURI.startsWith("/api/auth/token/refresh") ||
            requestURI.startsWith("/api/member/signup/")
        ) {
            filterChain.doFilter(request, response)
            return
        }

        val token = resolveToken(request)

        if (token != null && jwtTokenProvider.isValidToken(token)) {
            val memberId = jwtTokenProvider.extractMemberId(token)
            val role = jwtTokenProvider.extractRole(token)

            val member = memberRepository.findById(memberId)
                .orElseThrow { MemberNotFoundException(memberId) }

            val authorities = listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
            val authentication = UsernamePasswordAuthenticationToken(member, null, authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.removePrefix("Bearer ")
        } else {
            null
        }
    }
}
