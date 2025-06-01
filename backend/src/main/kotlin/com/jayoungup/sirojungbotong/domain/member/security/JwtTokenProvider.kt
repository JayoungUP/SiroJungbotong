package com.jayoungup.sirojungbotong.domain.member.security

import com.jayoungup.sirojungbotong.domain.member.entity.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(

    @Value("\${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long,

    @Value("\${jwt.secret}")
    secretKey: String
) {

    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createAccessToken(memberId: Long, role: Role): String {
        val now = Date()
        val expiry = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .setSubject(memberId.toString())
            .claim("role", role.name)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createRefreshToken(memberId: Long, role: Role): String {
        val now = Date()
        val expiry = Date(now.time + refreshTokenExpiration)

        return Jwts.builder()
            .setSubject(memberId.toString())
            .claim("role", role.name)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractMemberId(token: String): Long {
        return parseClaims(token).subject.toLong()
    }

    fun extractRole(token: String): Role {
        val roleString = parseClaims(token)["role"] as String
        return Role.valueOf(roleString)
    }

    fun isValidToken(token: String): Boolean {
        return try {
            parseClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun parseClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
