package com.jayoungup.sirojungbotong.domain.store.service

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import com.jayoungup.sirojungbotong.domain.store.dto.*
import com.jayoungup.sirojungbotong.domain.store.entity.Store
import com.jayoungup.sirojungbotong.domain.store.exception.NoStorePermissionException
import com.jayoungup.sirojungbotong.domain.store.exception.StoreNotFoundException
import com.jayoungup.sirojungbotong.domain.store.repository.StoreRepository
import com.jayoungup.sirojungbotong.global.config.app.AppProperties
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

    fun createStore(owner: Member, dto: StoreCreateRequestDto, image: MultipartFile?, businessDocument: MultipartFile?): StoreResponseDto {
        val imagePath = image?.let { saveFile(it, appProperties.uploadPath.store) }
        val docPath = businessDocument?.let { saveFile(it, appProperties.uploadPath.store) }

        val store = Store(
            name = dto.name,
            address = dto.address,
            openTime = dto.openTime,
            closeTime = dto.closeTime,
            imageUrl = imagePath,
            businessDocumentUrl = docPath,
            owner = owner
        )
        return StoreResponseDto.from(storeRepository.save(store))
    }

    fun updateStore(owner: Member, id: Long, dto: StoreUpdateRequestDto, image: MultipartFile?, businessDocument: MultipartFile?): StoreResponseDto {
        val store = storeRepository.findById(id).orElseThrow { StoreNotFoundException() }

        if (store.owner.id != owner.id) throw NoStorePermissionException()

        store.name = dto.name
        store.address = dto.address
        store.openTime = dto.openTime
        store.closeTime = dto.closeTime
        image?.let { store.imageUrl = saveFile(it, appProperties.uploadPath.store) }
        businessDocument?.let { store.businessDocumentUrl = saveFile(it, appProperties.uploadPath.store) }

        return StoreResponseDto.from(storeRepository.save(store))
    }

    @Transactional(readOnly = true)
    fun getStore(id: Long): StoreResponseDto =
        StoreResponseDto.from(storeRepository.findById(id).orElseThrow { StoreNotFoundException() })

    @Transactional(readOnly = true)
    fun getAllStores(): List<StoreResponseDto> =
        storeRepository.findAll().map { StoreResponseDto.from(it) }

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