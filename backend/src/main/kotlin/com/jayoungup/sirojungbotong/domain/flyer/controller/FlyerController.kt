package com.jayoungup.sirojungbotong.domain.flyer.controller

import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation

import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerCreateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerUpdateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerResponseDto
import com.jayoungup.sirojungbotong.domain.flyer.service.FlyerService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Tag(name = "전단지 API", description = "전단지 생성, 조회, 수정, 삭제 기능 제공")
@RestController
@RequestMapping("/api/flyers")
class FlyerController(
    private val flyerService: FlyerService
) {
    @Operation(summary = "전단지 등록", description = "전단지를 새로 등록합니다.")
    @PostMapping
    fun create(
        @RequestParam storeName: String,
        @RequestParam category: String,
        @RequestParam description: String,
        @RequestParam expireAt: String,
        @RequestParam usesSiro: Boolean,
        @RequestParam(required = false) image: MultipartFile?
    ): FlyerResponseDto {
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

        val dto = FlyerCreateRequestDto(
            storeName = storeName,
            category = category,
            description = description,
            expireAt = expireAt,
            usesSiro = usesSiro
        )
        return FlyerResponseDto.from(
            flyerService.createFlyer(dto, uploadedImagePath)
        )
    }

    @Operation(summary = "전단지 단건 조회", description = "전단지 ID로 단건 조회합니다.")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): FlyerResponseDto =
        FlyerResponseDto.from(flyerService.getFlyer(id))

    @Operation(summary = "전단지 전체 조회", description = "모든 전단지를 조회합니다.")
    @GetMapping
    fun getAll(): List<FlyerResponseDto> =
        flyerService.getAllFlyers().map { FlyerResponseDto.from(it) }

    @Operation(summary = "전단지 텍스트 수정", description = "전단지의 텍스트 내용을 수정합니다.")
    @PutMapping("/{id}")
    fun updateText(
        @PathVariable id: Long,
        @RequestBody updated: FlyerUpdateRequestDto
    ): FlyerResponseDto =
        FlyerResponseDto.from(flyerService.updateFlyerText(id, updated))

    @Operation(summary = "전단지 삭제", description = "ID로 전단지를 삭제합니다.")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Map<String, String> {
        flyerService.deleteFlyer(id)
        return mapOf("message" to "전단지가 삭제되었습니다.")
    }

    @Operation(summary = "전단지 이미지 수정", description = "전단지의 이미지를 새 파일로 교체합니다.")
    @PatchMapping("/{id}/image")
    fun updateImage(
        @PathVariable id: Long,
        @RequestParam image: MultipartFile
    ): FlyerResponseDto {
        val uploadDir = File("${System.getProperty("user.dir")}/backend/uploads")
        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val timestamp = LocalDateTime.now().format(formatter)
        val filename = "${timestamp}_${image.originalFilename}"
        val file = File(uploadDir, filename)
        image.transferTo(file)

        return FlyerResponseDto.from(
            flyerService.updateFlyerImage(id, "backend/uploads/$filename")
        )
    }
}