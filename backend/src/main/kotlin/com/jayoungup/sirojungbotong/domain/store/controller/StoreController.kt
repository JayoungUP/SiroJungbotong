package com.jayoungup.sirojungbotong.domain.store.controller

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.store.dto.*
import com.jayoungup.sirojungbotong.domain.store.service.StoreService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "업장 API")
@RestController
@RequestMapping("/api/stores")
class StoreController(
    private val storeService: StoreService
) {

    @PostMapping(consumes = ["multipart/form-data"])
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    fun create(
        @RequestAttribute member: Member,
        @ModelAttribute dto: StoreCreateRequestDto,
        @RequestPart(required = false) image: MultipartFile?,
        @RequestPart(required = false) businessDocument: MultipartFile?
    ): ResponseEntity<StoreDetailResponseDto> {
        return ResponseEntity.ok(storeService.createStore(member, dto, image, businessDocument))
    }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<StoreDetailResponseDto> =
        ResponseEntity.ok(storeService.getStore(id))

    @GetMapping
    fun getAll(): ResponseEntity<List<StoreSimpleResponseDto>> =
        ResponseEntity.ok(storeService.getAllStores())

    @PutMapping("/{id}", consumes = ["multipart/form-data"])
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    fun update(
        @RequestAttribute member: Member,
        @PathVariable id: Long,
        @ModelAttribute dto: StoreUpdateRequestDto,
        @RequestPart(required = false) image: MultipartFile?,
        @RequestPart(required = false) businessDocument: MultipartFile?
    ): ResponseEntity<StoreDetailResponseDto> =
        ResponseEntity.ok(storeService.updateStore(member, id, dto, image, businessDocument))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    fun delete(@RequestAttribute member: Member, @PathVariable id: Long): ResponseEntity<Void> {
        storeService.deleteStore(member, id)
        return ResponseEntity.noContent().build()
    }
}