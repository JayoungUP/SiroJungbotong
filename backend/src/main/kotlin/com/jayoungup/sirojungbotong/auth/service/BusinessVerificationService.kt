package com.jayoungup.sirojungbotong.auth.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class BusinessVerificationService(
    @Value("\${business-verification.service-key}")
    private val apiKey: String,

    @Value("\${business-verification.api-url}")
    private val apiUrl: String
) {

    private val webClient: WebClient = WebClient.builder()
        .baseUrl(apiUrl)
        .build()

    fun verify(bNo: String, startDt: String, pNm: String, pNm2: String): Boolean {

        val requestBody = mapOf(
            "businesses" to listOf(
                mapOf(
                    "b_no" to bNo,
                    "start_dt" to startDt,
                    "p_nm" to pNm,
                    "p_nm2" to pNm2
                )
            )
        )

        val response = webClient.post()
            .uri("?serviceKey=$apiKey")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(BusinessValidateResponse::class.java)
            .block() ?: throw IllegalArgumentException("사업자 등록정보 확인 실패 (응답 없음)")

        val data = response.data.firstOrNull()
            ?: throw IllegalArgumentException("사업자 등록정보 확인 실패 (응답 데이터 없음)")

        if (data.valid != "01") {
            throw IllegalArgumentException("사업자 등록정보 불일치 (${data.valid_msg})")
        }

        return true
    }

    data class BusinessValidateResponse(
        val status_code: String,
        val request_cnt: Int,
        val valid_cnt: Int,
        val data: List<BusinessValidateData>
    )

    data class BusinessValidateData(
        val b_no: String,
        val valid: String,
        val valid_msg: String?
    )
}
