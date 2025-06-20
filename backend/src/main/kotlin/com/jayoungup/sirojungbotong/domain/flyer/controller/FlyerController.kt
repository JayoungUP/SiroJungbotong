package com.jayoungup.sirojungbotong.domain.flyer.controller

import com.jayoungup.sirojungbotong.domain.flyer.dto.*
import com.jayoungup.sirojungbotong.domain.flyer.mapper.FlyerMapper
import com.jayoungup.sirojungbotong.domain.flyer.service.FlyerService
import com.jayoungup.sirojungbotong.domain.flyer.service.ItemService
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Tag(name = "전단지 API", description = "전단지 생성, 조회, 수정, 삭제 기능 제공")
@RestController
@RequestMapping("/api/flyers")
class FlyerController(
    private val flyerService: FlyerService,
    private val itemService: ItemService
) {

    @Operation(summary = "전단지 등록", description = "전단지를 새로 등록합니다.")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @PostMapping(consumes = ["multipart/form-data"])
    fun create(
        @AuthenticationPrincipal member: Member,
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

    @GetMapping("/{id}/translations")
    @Operation(
        summary = "전단지 단건 다국어 번역",
        description = "전단지 상세 내용을 한국어/영어/중국어로 번역해 반환합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "Success",
                        summary = "다국어 번역 응답 예시",
                        value = """
                        {
                            "status": 200,
                            "data": {
                                "ko": {
                                    "category": "분식",
                                    "description": "방문 포장 할인 행사 중!",
                                    "items": [
                                        {
                                            "name": "라면",
                                            "description": "특별 할인"
                                        }
                                    ]
                                },
                                "en": {
                                    "category": "Expression",
                                    "description": "Door-to-door packaging on sale!",
                                    "items": [
                                        {
                                            "name": "Ramen",
                                            "description": "Special discounts"
                                        }
                                    ]
                                },
                                "zh": {
                                    "category": "表达",
                                    "description": "上门包装销售！",
                                    "items": [
                                        {
                                            "name": "拉面",
                                            "description": "特别折扣"
                                        }
                                    ]
                                }
                            }
                        }
                    """
                    )]
                )]
            )
        ]
    )
    fun getTranslatedFlyer(@PathVariable id: Long): ResponseEntity<Map<String, Any>> {
        val flyer = flyerService.getFlyer(id)
        val result = flyerService.getTranslatedFlyerMap(flyer)
        return ResponseEntity.ok(result)
    }

    @Operation(
        summary = "전단지 전체 조회",
        description = "모든 전단지를 조회합니다. 시장명으로 필터링하거나, 정렬 기준을 설정할 수 있습니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "Success",
                        summary = "성공 응답 예시",
                        value = """
                            {
                                "status": 200,
                                "data": [
                                    {
                                        "id": 1,
                                        "storeId": 1,
                                        "category": "분식",
                                        "description": "김밥 2줄 3천원",
                                        "expireAt": "2025-06-10T23:59:59",
                                        "usesSiro": true,
                                        "imageUrl": "backend/uploads/flyers/20250619_154814_성현이네 분식.jpg",
                                        "createdAt": "2025-06-19T15:48:14.796874",
                                        "updatedAt": "2025-06-19T15:48:36.967433",
                                        "items": [
                                            {
                                                "id": 1,
                                                "flyerId": 1,
                                                "name": "김밥",
                                                "description": "마감 할인",
                                                "price": 3000,
                                                "validFrom": "2025-06-01",
                                                "validUntil": "2025-06-30",
                                                "imageUrl": "backend/uploads/items/20250619_154854_성현이네 김밥.jpg"
                                            }
                                        ],
                                        "scrapCount": 1
                                    },
                                    {
                                        "id": 2,
                                        "storeId": 1,
                                        "category": "분식",
                                        "description": "방문 포장 할인 행사 중!",
                                        "expireAt": "2025-06-05T23:59:59",
                                        "usesSiro": true,
                                        "imageUrl": "backend/uploads/flyers/20250619_232445_성현이네 분식.jpg",
                                        "createdAt": "2025-06-19T23:24:45.372323",
                                        "updatedAt": "2025-06-19T23:24:45.372332",
                                        "items": [],
                                        "scrapCount": 3
                                    }
                                ]
                            }
                        """
                    )]
                )]
            )
        ]
    )
    @GetMapping
    fun getAll(
        @Parameter(
            `in` = ParameterIn.QUERY,
            description = "시장 이름 (예: 정왕시장)",
            schema = Schema(type = "string")
        )
        @RequestParam(required = false) market: String?,

        @Parameter(
            `in` = ParameterIn.QUERY,
            description = "정렬 기준 (default: 최신순) - latest | scrap",
            schema = Schema(type = "string", allowableValues = ["latest", "scrap"], defaultValue = "latest")
        )
        @RequestParam(required = false, defaultValue = "latest") sort: String,

        @ParameterObject pageable: Pageable
    ): ResponseEntity<Page<FlyerResponseDto>> {
        val flyers = flyerService.getFlyersFiltered(market, sort, pageable)
        val result = flyers.map { FlyerMapper.toDto(it) }
        return ResponseEntity.ok(result)
    }

    @Operation(summary = "전단지 텍스트 수정", description = "전단지의 텍스트 내용을 수정합니다.")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @PutMapping("/{id}")
    fun updateText(
        @AuthenticationPrincipal member: Member,
        @PathVariable id: Long,
        @RequestBody updated: FlyerUpdateRequestDto
    ): ResponseEntity<FlyerResponseDto> =
        ResponseEntity.ok(FlyerMapper.toDto(flyerService.updateFlyerText(member, id, updated)))

    @Operation(summary = "전단지 삭제", description = "ID로 전단지를 삭제합니다.")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @DeleteMapping("/{id}")
    fun delete(@AuthenticationPrincipal member: Member, @PathVariable id: Long): ResponseEntity<Void> {
        flyerService.deleteFlyer(member, id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "전단지 이미지 수정", description = "전단지의 이미지를 새 파일로 교체합니다.")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @PatchMapping("/{id}/image")
    fun updateImage(
        @AuthenticationPrincipal member: Member,
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

    // 전단지 품목

    @PostMapping("/{flyerId}/items", consumes = ["multipart/form-data"])
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "전단지에 품목 추가")
    fun addItem(
        @AuthenticationPrincipal member: Member,
        @PathVariable flyerId: Long,
        @ParameterObject data: ItemCreateRequestDto,
        @RequestPart("image", required = false)
        @Parameter(
            description = "품목 이미지 파일 (선택)",
            content = [Content(mediaType = "application/octet-stream", schema = Schema(type = "string", format = "binary"))]
        )
        image: MultipartFile?
    ): ResponseEntity<ItemResponseDto> {
        val imagePath = image?.let {
            val uploadDir = File("${System.getProperty("user.dir")}/backend/uploads/items")
            if (!uploadDir.exists()) uploadDir.mkdirs()

            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            val filename = "${timestamp}_${it.originalFilename}"
            val file = File(uploadDir, filename)
            it.transferTo(file)
            "backend/uploads/items/$filename"
        }

        return ResponseEntity.ok(itemService.addItemToFlyer(member, flyerId, data, imagePath))
    }

    @GetMapping("/{flyerId}/items")
    @Operation(
        summary = "전단지 품목 목록 조회",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "Success",
                        summary = "성공 응답 예시",
                        value = """
                            {
                                "status": 200,
                                "data": [
                                    {
                                        "id": 1,
                                        "flyerId": 1,
                                        "name": "김밥",
                                        "description": "마감 할인",
                                        "price": 3000,
                                        "validFrom": "2025-06-01",
                                        "validUntil": "2025-06-30",
                                        "imageUrl": "backend/uploads/items/20250619_154854_성현이네 김밥.jpg"
                                    },
                                    {
                                        "id": 2,
                                        "flyerId": 1,
                                        "name": "라면",
                                        "description": "특별 할인",
                                        "price": 1000,
                                        "validFrom": "2025-06-01",
                                        "validUntil": "2025-06-30",
                                        "imageUrl": "backend/uploads/items/20250620_031458_성현이네 김밥.jpg"
                                    }
                                ]
                            }
                        """
                    )]
                )]
            )
        ]
    )
    fun getItems(@PathVariable flyerId: Long): ResponseEntity<List<ItemResponseDto>> =
        ResponseEntity.ok(itemService.getItems(flyerId))

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @Operation(summary = "전단지 품목 삭제")
    fun deleteItem(@AuthenticationPrincipal member: Member, @PathVariable itemId: Long): ResponseEntity<Void> {
        itemService.deleteItem(member, itemId)
        return ResponseEntity.noContent().build()
    }

}