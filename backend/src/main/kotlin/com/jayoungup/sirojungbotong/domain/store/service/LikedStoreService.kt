package com.jayoungup.sirojungbotong.domain.store.service

import com.jayoungup.sirojungbotong.domain.member.exception.MemberNotFoundException
import com.jayoungup.sirojungbotong.domain.member.repository.MemberRepository
import com.jayoungup.sirojungbotong.domain.store.dto.LikedStoreResponse
import com.jayoungup.sirojungbotong.domain.store.entity.LikedStore
import com.jayoungup.sirojungbotong.domain.store.exception.StoreNotFoundException
import com.jayoungup.sirojungbotong.domain.store.repository.LikedStoreRepository
import com.jayoungup.sirojungbotong.domain.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LikedStoreService(
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
    private val likedStoreRepository: LikedStoreRepository
) {

    fun add(userId: Long, storeId: Long) {
        val member = memberRepository.findById(userId)
            .orElseThrow { MemberNotFoundException(userId) }
        val store = storeRepository.findById(storeId)
            .orElseThrow { StoreNotFoundException() }

        if (!likedStoreRepository.existsByMemberAndStoreId(member, storeId)) {
            likedStoreRepository.save(LikedStore(member, store))
        }
    }

    fun remove(userId: Long, storeId: Long) {
        val member = memberRepository.findById(userId)
            .orElseThrow { MemberNotFoundException(userId) }
        likedStoreRepository.deleteByMemberAndStoreId(member, storeId)
    }

    fun list(userId: Long): List<LikedStoreResponse> {
        val member = memberRepository.findById(userId)
            .orElseThrow { MemberNotFoundException(userId) }
        return likedStoreRepository.findByMember(member)
            .map { LikedStoreResponse(it.store.id, it.store.name) }
    }
}