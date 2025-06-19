package com.jayoungup.sirojungbotong.domain.flyer.controller

import com.jayoungup.sirojungbotong.domain.flyer.dto.FlyerResponseDto
import com.jayoungup.sirojungbotong.domain.flyer.service.ScrapFlyerService
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "전단지 스크랩 API")
@RestController
@RequestMapping("/api/flyers/scrap")
class ScrapFlyerController(
    private val scrapFlyerService: ScrapFlyerService
) {

    @Operation(summary = "전단지 스크랩 추가", description = "사용자가 전단지를 스크랩합니다.")
    @PostMapping("/{flyerId}")
    fun scrapFlyer(
        @AuthenticationPrincipal member: Member,
        @PathVariable flyerId: Long
    ) {
        scrapFlyerService.scrap(member, flyerId)
    }

    @Operation(summary = "전단지 스크랩 삭제", description = "사용자가 전단지 스크랩을 해제합니다.")
    @DeleteMapping("/{flyerId}")
    fun unscrapFlyer(
        @AuthenticationPrincipal member: Member,
        @PathVariable flyerId: Long
    ) {
        scrapFlyerService.unscrap(member, flyerId)
    }

    @Operation(
        summary = "스크랩한 전단지 목록 조회",
        description = "사용자가 스크랩한 전단지 목록을 조회합니다.",
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
                                        "items": []
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
    fun getScrappedFlyers(
        @AuthenticationPrincipal member: Member
    ): List<FlyerResponseDto> {
        return scrapFlyerService.getScrappedFlyers(member)
    }
}