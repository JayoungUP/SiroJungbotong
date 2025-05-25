package com.jayoungup.sirojungbotong.domain.flyer.controller

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import com.jayoungup.sirojungbotong.domain.flyer.service.FlyerService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/flyers")
class FlyerController(
    private val flyerService: FlyerService
) {
    @PostMapping
    fun create(
        @RequestParam storeName: String,
        @RequestParam category: String,
        @RequestParam description: String,
        @RequestParam expireAt: String,
        @RequestParam usesSiro: Boolean,
        @RequestParam(required = false) image: MultipartFile?
    ): Flyer {
        val uploadDir = File("${System.getProperty("user.dir")}/backend/uploads")
        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }

        val uploadedImagePath = image?.let {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            val timestamp = LocalDateTime.now().format(formatter)
            val filename = "${timestamp}_${image.originalFilename}"
            val file = File(uploadDir, filename)
            it.transferTo(file)
            "backend/uploads/$filename"
        }

        return flyerService.createFlyer(
            storeName, category, description,
            LocalDateTime.parse(expireAt), usesSiro, uploadedImagePath
        )
    }
}