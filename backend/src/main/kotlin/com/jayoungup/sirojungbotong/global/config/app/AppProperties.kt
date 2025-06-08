package com.jayoungup.sirojungbotong.global.config.app

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app")
class AppProperties {
    lateinit var baseUrl: String
    lateinit var uploadPath: UploadPath

    class UploadPath {
        lateinit var flyer: String
        lateinit var store: String
    }
}