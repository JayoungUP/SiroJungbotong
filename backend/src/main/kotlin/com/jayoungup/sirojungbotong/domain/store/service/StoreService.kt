package com.jayoungup.sirojungbotong.domain.store.service

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.store.dto.*
import com.jayoungup.sirojungbotong.domain.store.exception.NoStorePermissionException
import com.jayoungup.sirojungbotong.domain.store.exception.StoreNotFoundException
import com.jayoungup.sirojungbotong.domain.store.mapper.StoreMapper
import com.jayoungup.sirojungbotong.domain.store.repository.StoreRepository
import com.jayoungup.sirojungbotong.global.config.app.AppProperties
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
@Transactional
class StoreService(
    private val storeRepository: StoreRepository,
    private val appProperties: AppProperties
) {

    fun createStore(owner: Member, dto: StoreCreateRequestDto, image: MultipartFile?, businessDocument: MultipartFile?): StoreDetailResponseDto {
        val imagePath = image?.let { saveFile(it, appProperties.uploadPath.store) }
        val docPath = businessDocument?.let { saveFile(it, appProperties.uploadPath.store) }

        val store = StoreMapper.toEntity(dto, imagePath, docPath, owner)
        return StoreMapper.toDetailDto(storeRepository.save(store))
    }

    fun updateStore(owner: Member, id: Long, dto: StoreUpdateRequestDto, image: MultipartFile?, businessDocument: MultipartFile?): StoreDetailResponseDto {
        val store = storeRepository.findById(id).orElseThrow { StoreNotFoundException() }

        if (store.owner.id != owner.id) throw NoStorePermissionException()

        store.name = dto.name
        store.address = dto.address
        store.openTime = dto.openTime
        store.closeTime = dto.closeTime
        image?.let { store.imageUrl = saveFile(it, appProperties.uploadPath.store) }
        businessDocument?.let { store.businessDocumentUrl = saveFile(it, appProperties.uploadPath.store) }

        return StoreMapper.toDetailDto(storeRepository.save(store))
    }

    @Transactional(readOnly = true)
    fun getStore(id: Long): StoreDetailResponseDto {
        val store = storeRepository.findById(id).orElseThrow { StoreNotFoundException() }
        return StoreMapper.toDetailDto(store)
    }

    @Transactional(readOnly = true)
    fun getStoresFiltered(market: String?, sort: String, pageable: Pageable): Page<StoreSimpleResponseDto> {
        val stores = when (sort) {
            "likeCount" -> storeRepository.findAllByMarketOrderByLikeCountDesc(market, pageable)
            else -> storeRepository.findAllByMarketOrderByCreatedAtDesc(market, pageable)
        }
        return stores.map { StoreMapper.toSimpleDto(it) }
    }

    fun deleteStore(owner: Member, id: Long) {
        val store = storeRepository.findById(id).orElseThrow { StoreNotFoundException() }
        if (store.owner.id != owner.id) throw NoStorePermissionException()
        storeRepository.delete(store)
    }

    private fun saveFile(file: MultipartFile, basePath: String): String {
        val absoluteBasePath = File(System.getProperty("user.dir"), basePath)
        if (!absoluteBasePath.exists()) absoluteBasePath.mkdirs()

        val filename = "${System.currentTimeMillis()}_${file.originalFilename}"
        val savedFile = File(absoluteBasePath, filename)
        file.transferTo(savedFile)
        return "${absoluteBasePath.path}/$filename"
    }
}