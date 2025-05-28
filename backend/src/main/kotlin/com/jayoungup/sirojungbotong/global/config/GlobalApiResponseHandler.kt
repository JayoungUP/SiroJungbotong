package com.jayoungup.sirojungbotong.global.config

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class GlobalApiResponseHandler : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        // 이미 ApiResponse인 경우에는 중복 래핑하지 않음
        return returnType.parameterType != BaseResponse::class.java
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any {

        val path = request.uri.path

        // Swagger 요청은 래핑하지 않음
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") || path.startsWith("/swagger-config")) {
            return body ?: mapOf("message" to "No content")
        }

        // 바이너리 또는 파일 응답 타입은 그대로 반환
        if (
            selectedContentType.includes(MediaType.IMAGE_JPEG) ||
            selectedContentType.includes(MediaType.IMAGE_PNG) ||
            selectedContentType.includes(MediaType.APPLICATION_OCTET_STREAM) ||
            selectedContentType.includes(MediaType.APPLICATION_PDF) ||
            selectedConverterType == org.springframework.http.converter.ByteArrayHttpMessageConverter::class.java
        ) {
            return body ?: ByteArray(0)
        }

        // JSON 응답만 래핑
        return BaseResponse(
            status = 200,
            data = body ?: mapOf("message" to "응답 본문 없음")
        )
    }
}