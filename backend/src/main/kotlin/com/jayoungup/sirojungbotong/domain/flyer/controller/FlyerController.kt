package com.jayoungup.sirojungbotong.domain.flyer.controller

import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerCreateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerUpdateRequestDto
import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerResponseDto
import com.jayoungup.sirojungbotong.domain.flyer.mapper.FlyerMapper
import com.jayoungup.sirojungbotong.domain.flyer.service.FlyerService
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
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
    @PostMapping(consumes = ["multipart/form-data"])
    fun create(
        @RequestAttribute member: Member,
        @ParameterObject data: FlyerCreateRequestDto,
        @RequestPart(required = false, name = "image")
        @Parameter(
            description = "전단지 이미지 파일 (선택)",
            content = [Content(mediaType = "application/octet-stream", schema = Schema(type = "string", format = "binary"))]
        )
        image: MultipartFile?
    ): ResponseEntity<FlyerResponseDto> {
        val uploadDir = File("${System.getProperty("user.dir")}/backend/uploads/flyers")
        if (!uploadDir.exists()) uploadDir.mkdirs()

        val uploadedImagePath = image?.let {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            val filename = "${timestamp}_${it.originalFilename}"
            val file = File(uploadDir, filename)
            it.transferTo(file)
            "backend/uploads/flyers/$filename"
        }

        val createdFlyer = flyerService.createFlyer(member, data, uploadedImagePath)
        return ResponseEntity.ok(FlyerMapper.toDto(createdFlyer))
    }

    @Operation(summary = "전단지 단건 조회", description = "전단지 ID로 단건 조회합니다.")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<FlyerResponseDto> =
        ResponseEntity.ok(FlyerMapper.toDto(flyerService.getFlyer(id)))

    @Operation(summary = "전단지 전체 조회", description = "모든 전단지를 조회합니다.")
    @GetMapping
    fun getAll(): ResponseEntity<List<FlyerResponseDto>> =
        ResponseEntity.ok(flyerService.getAllFlyers().map { FlyerMapper.toDto(it) })

    @Operation(summary = "전단지 텍스트 수정", description = "전단지의 텍스트 내용을 수정합니다.")
    @PutMapping("/{id}")
    fun updateText(
        @RequestAttribute member: Member,
        @PathVariable id: Long,
        @RequestBody updated: FlyerUpdateRequestDto
    ): ResponseEntity<FlyerResponseDto> =
        ResponseEntity.ok(FlyerMapper.toDto(flyerService.updateFlyerText(member, id, updated)))

    @Operation(summary = "전단지 삭제", description = "ID로 전단지를 삭제합니다.")
    @DeleteMapping("/{id}")
    fun delete(@RequestAttribute member: Member, @PathVariable id: Long): ResponseEntity<Void> {
        flyerService.deleteFlyer(member, id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "전단지 이미지 수정", description = "전단지의 이미지를 새 파일로 교체합니다.")
    @PatchMapping("/{id}/image")
    fun updateImage(
        @RequestAttribute member: Member,
        @PathVariable id: Long,
        @RequestParam image: MultipartFile
    ): ResponseEntity<FlyerResponseDto> {
        val uploadDir = File("${System.getProperty("user.dir")}/backend/uploads/flyers")
        if (!uploadDir.exists()) uploadDir.mkdirs()

        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        val filename = "${timestamp}_${image.originalFilename}"
        val file = File(uploadDir, filename)
        image.transferTo(file)

        val updatedFlyer = flyerService.updateFlyerImage(member, id, "backend/uploads/flyers/$filename")
        return ResponseEntity.ok(FlyerMapper.toDto(updatedFlyer))
    }
}