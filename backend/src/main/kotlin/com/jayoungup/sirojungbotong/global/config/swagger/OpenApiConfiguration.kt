package com.jayoungup.sirojungbotong.global.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {

    @Bean
    fun globalOpenApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI ->
            // 모든 경로(path) 순회
            openApi.paths?.values?.forEach { pathItem ->
                pathItem.readOperations().forEach { operation ->
                    // 각 HTTP 메서드의 ApiResponses 순회
                    operation.responses?.forEach { (_, apiResponse) ->
                        // 기존 스키마를 가져온 뒤, 새로운 Wrapper 스키마 생성
                        val originalContent: Content? = apiResponse.content
                        originalContent?.forEach { (mediaTypeKey, mediaTypeObj) ->
                            val originalSchema: Schema<*>? = mediaTypeObj.schema
                            if (originalSchema != null) {
                                // BaseResponse<T> 형태로 래핑된 ObjectSchema 생성
                                val wrapperSchema = ObjectSchema().apply {
                                    // status 프로퍼티
                                    addProperties("status", Schema<Any>().apply {
                                        type("integer")
                                        example(200)
                                    })
                                    // data 프로퍼티 → 원래 스키마를 $ref로 연결
                                    addProperties("data", Schema<Any>().apply {
                                        // 원래 스키마가 $ref인 경우
                                        if (originalSchema.`$ref` != null) {
                                            `$ref`(originalSchema.`$ref`)
                                        } else {
                                            // inline 정의(복사)된 경우
                                            originalSchema as Schema<Any>
                                        }
                                    })
                                    nullable(false)
                                }
                                // 새 Content 객체에 wrapperSchema를 설정
                                val newContent = Content().addMediaType(
                                    mediaTypeKey,
                                    MediaType().schema(wrapperSchema)
                                )
                                // ApiResponse에 새 Content로 교체
                                apiResponse.content = newContent
                            }
                        }
                    }
                }
            }
        }
    }

    // schemaOf helper removed as it's no longer needed.
}