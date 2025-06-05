package com.jayoungup.sirojungbotong.global.config

import com.jayoungup.sirojungbotong.global.config.app.AppProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File
import java.nio.file.Paths

@Configuration
class WebConfig(private val appProperties: AppProperties) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val flyerPath = File(appProperties.uploadPath.flyer).absoluteFile.toURI().toString()
        val storePath = File(appProperties.uploadPath.store).absoluteFile.toURI().toString()

        registry.addResourceHandler("/uploads/flyers/**")
            .addResourceLocations(flyerPath)

        registry.addResourceHandler("/uploads/stores/**")
            .addResourceLocations(storePath)
    }
}