package com.jayoungup.sirojungbotong.global.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.http.*
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Component
class DeeplTranslator(
    @Value("\${deepl.api-key}") private val apiKey: String
) {
    private val restTemplate = RestTemplate()
    private val uri = URI.create("https://api-free.deepl.com/v2/translate")

    fun translate(text: String, targetLang: String): String {
        if (text.isBlank()) return ""

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString())
        val body = "auth_key=$apiKey&text=$encodedText&target_lang=$targetLang"
        val request = HttpEntity(body, headers)

        val response = restTemplate.postForEntity(uri, request, Map::class.java)
        val translations = (response.body?.get("translations") as? List<*>) ?: return text
        return (translations.firstOrNull() as? Map<*, *>)?.get("text")?.toString() ?: text
    }
}