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
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    ): ResponseEntity<StoreDetailResponseDto> {
        return ResponseEntity.ok(storeService.createStore(member, dto, image))
    }

    @Operation(summary = "업장 단건 조회", description = "업장 ID를 통해 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<StoreDetailResponseDto> =
        ResponseEntity.ok(storeService.getStore(id))

    @Operation(
        summary = "업장 전체 목록 조회",
        description = "전체 업장 목록을 조회합니다. 정렬 기준과 시장명을 기준으로 필터링할 수 있습니다.",
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
                            "data": {
                                "content": [
                                    {
                                        "id": 1,
                                        "ownerId": 1,
                                        "name": "새마을식당",
                                        "market": "정왕시장",
                                        "address": "경기도 시흥시 발동동",
                                        "openTime": "09:00:00",
                                        "closeTime": "21:00:00",
                                        "category": "음식점",
                                        "imageUrl": "/backend/uploads/stores/1750488969332_새마을식당.jpg",
                                        "likeCount": 0
                                    },
                                    {
                                        "id": 2,
                                        "ownerId": 1,
                                        "name": "백다방",
                                        "market": "정왕시장",
                                        "address": "경기도 시흥시 발동동",
                                        "openTime": "09:00:00",
                                        "closeTime": "21:00:00",
                                        "category": "음식점",
                                        "imageUrl": null,
                                        "likeCount": 0
                                    }
                                ],
                                "page": {
                                    "size": 10,
                                    "number": 0,
                                    "totalElements": 2,
                                    "totalPages": 1
                                }
                            }
                        }
                    """
                    )]
                )]
            )
        ]
    )
    @GetMapping
    fun getAll(
        @RequestParam(required = false) market: String?,
        @RequestParam(defaultValue = "recent") sort: String,
        @ParameterObject pageable: Pageable
    ): ResponseEntity<Page<StoreSimpleResponseDto>> {
        val result = storeService.getStoresFiltered(market, sort, pageable)
        return ResponseEntity.ok(result)
    }

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
        ResponseEntity.ok(storeService.updateStore(member, id, dto, image))

    @Operation(summary = "업장 삭제", description = "사업자가 특정 업장을 삭제합니다.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    fun delete(@AuthenticationPrincipal member: Member, @PathVariable id: Long): ResponseEntity<Void> {
        storeService.deleteStore(member, id)
        return ResponseEntity.noContent().build()
    }
}