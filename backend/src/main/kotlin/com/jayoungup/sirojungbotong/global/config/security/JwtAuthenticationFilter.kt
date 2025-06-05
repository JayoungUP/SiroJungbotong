package com.jayoungup.sirojungbotong.global.config.security

import com.jayoungup.sirojungbotong.domain.member.repository.MemberRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            if (jwtTokenProvider.isValidToken(token)) {
                val memberId = jwtTokenProvider.extractMemberId(token)
                val role = jwtTokenProvider.extractRole(token)

                val member = memberRepository.findById(memberId).orElse(null)
                if (member != null) {
                    val authorities = listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
                    val auth = UsernamePasswordAuthenticationToken(member, null, authorities)
                    auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth
                    // RequestAttribute 로 @RequestAttribute로 주입 가능하게
                    request.setAttribute("member", member)
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}