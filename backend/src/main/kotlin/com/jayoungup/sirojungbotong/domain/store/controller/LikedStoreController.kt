package com.jayoungup.sirojungbotong.domain.store.controller

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.store.dto.LikedStoreResponse
import com.jayoungup.sirojungbotong.domain.store.service.LikedStoreService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "가게 즐겨찾기", description = "가게 즐겨찾기 관련 API")
@RestController
@RequestMapping("/stores/liked")
class LikedStoreController(
    private val likedStoreService: LikedStoreService
) {

    @Operation(summary = "가게 즐겨찾기 추가", description = "사용자가 가게를 즐겨찾기에 추가합니다.")
    @PostMapping("/{storeId}")
    fun addLikedStore(
        @AuthenticationPrincipal member: Member,
        @PathVariable storeId: Long
    ) {
        likedStoreService.add(member.id, storeId)
    }

    @Operation(summary = "가게 즐겨찾기 삭제", description = "사용자가 가게 즐겨찾기를 제거합니다.")
    @DeleteMapping("/{storeId}")
    fun removeLikedStore(
        @AuthenticationPrincipal member: Member,
        @PathVariable storeId: Long
    ) {
        likedStoreService.remove(member.id, storeId)
    }

    @Operation(summary = "즐겨찾기한 가게 목록 조회", description = "사용자가 즐겨찾기한 가게 목록을 조회합니다.")
    @GetMapping
    fun getLikedStores(
        @AuthenticationPrincipal member: Member
    ): List<LikedStoreResponse> {
        return likedStoreService.list(member.id)
    }
}