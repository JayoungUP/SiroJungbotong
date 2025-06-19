package com.jayoungup.sirojungbotong.domain.store.controller

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.store.dto.*
import com.jayoungup.sirojungbotong.domain.store.service.StoreService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "업장 API")
@RestController
@RequestMapping("/api/stores")
class StoreController(
    private val storeService: StoreService
) {

    @Operation(summary = "업장 생성", description = "사업자가 업장을 생성합니다.")
    @PostMapping(consumes = ["multipart/form-data"])
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    fun create(
        @AuthenticationPrincipal member: Member,
        @ModelAttribute @Valid dto: StoreCreateRequestDto,
        @RequestPart(required = false) image: MultipartFile?,
        @RequestPart(required = false) businessDocument: MultipartFile?
    ): ResponseEntity<StoreDetailResponseDto> {
        return ResponseEntity.ok(storeService.createStore(member, dto, image, businessDocument))
    }

    @Operation(summary = "업장 단건 조회", description = "업장 ID를 통해 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<StoreDetailResponseDto> =
        ResponseEntity.ok(storeService.getStore(id))

    @Operation(
        summary = "업장 전체 목록 조회",
        description = "전체 업장 목록을 조회합니다.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "성공",
                content = [Content(
                    mediaType = "application/json",
                    examples = [ExampleObject(
                        name = "Success",
                        summary = "즐겨찾기 가게 목록 조회 성공",
                        value = """
                        {
                            "status": 200,
                            "data": [
                                {
                                    "id": 1,
                                    "ownerId": 1,
                                    "name": "새마을식당",
                                    "address": "경기도 시흥시 발동동",
                                    "openTime": "09:00:00",
                                    "closeTime": "21:00:00",
                                    "imageUrl": null,
                                    "businessDocumentUrl": null
                                },
                                {
                                    "id": 2,
                                    "ownerId": 1,
                                    "name": "새마을식당",
                                    "address": "경기도 시흥시 발동동",
                                    "openTime": "09:00:00",
                                    "closeTime": "21:00:00",
                                    "imageUrl": null,
                                    "businessDocumentUrl": null
                                },
                                {
                                    "id": 3,
                                    "ownerId": 1,
                                    "name": "새마을식당",
                                    "address": "경기도 시흥시 발동동",
                                    "openTime": "09:00:00",
                                    "closeTime": "21:00:00",
                                    "imageUrl": null,
                                    "businessDocumentUrl": null
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
    fun getAll(): ResponseEntity<List<StoreSimpleResponseDto>> =
        ResponseEntity.ok(storeService.getAllStores())

    @Operation(summary = "업장 수정", description = "사업자가 업장 정보를 수정합니다.")
    @PutMapping("/{id}", consumes = ["multipart/form-data"])
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    fun update(
        @AuthenticationPrincipal member: Member,
        @PathVariable id: Long,
        @ModelAttribute dto: StoreUpdateRequestDto,
        @RequestPart(required = false) image: MultipartFile?,
        @RequestPart(required = false) businessDocument: MultipartFile?
    ): ResponseEntity<StoreDetailResponseDto> =
        ResponseEntity.ok(storeService.updateStore(member, id, dto, image, businessDocument))

    @Operation(summary = "업장 삭제", description = "사업자가 특정 업장을 삭제합니다.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    fun delete(@AuthenticationPrincipal member: Member, @PathVariable id: Long): ResponseEntity<Void> {
        storeService.deleteStore(member, id)
        return ResponseEntity.noContent().build()
    }
}