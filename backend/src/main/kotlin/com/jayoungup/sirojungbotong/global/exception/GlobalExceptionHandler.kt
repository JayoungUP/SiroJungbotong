package com.jayoungup.sirojungbotong.global.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<Map<String, Any>> {
        val body: Map<String, Any> = mapOf(
            "error" to (e::class.simpleName ?: "UnknownError"),
            "message" to (e.message ?: "No message provided"),
            "status" to e.status.value().toInt()
        )
        return ResponseEntity(body, e.status)
    }
}