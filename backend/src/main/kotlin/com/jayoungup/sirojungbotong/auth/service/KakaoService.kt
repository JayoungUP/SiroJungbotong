package com.jayoungup.sirojungbotong.auth.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class KakaoService {

    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://kapi.kakao.com")
        .build()

    data class KakaoUserInfo(
        val kakaoId: String,
        val nickname: String
    )

    fun getUserInfo(accessToken: String): KakaoUserInfo {
        val response = webClient.get()
            .uri("/v2/user/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono(KakaoUserResponse::class.java)
            .block() ?: throw IllegalArgumentException("카카오 사용자 정보 조회 실패")

        val nickname = response.properties?.nickname ?: "카카오사용자"

        return KakaoUserInfo(
            kakaoId = response.id.toString(),
            nickname = nickname
        )
    }

    data class KakaoUserResponse(
        val id: Long,
        val properties: Properties?
    )

    data class Properties(
        val nickname: String?
    )
}
