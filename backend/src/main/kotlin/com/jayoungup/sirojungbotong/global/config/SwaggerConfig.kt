package com.jayoungup.sirojungbotong.global.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(
                Info()
                    .title("시로정보통 API")
                    .description("시흥시장 전단지 서비스 API 문서입니다.")
                    .version("v1.0")
            )
    }
}