package com.jayoungup.sirojungbotong.domain.store.repository

import com.jayoungup.sirojungbotong.domain.store.entity.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StoreRepository : JpaRepository<Store, Long> {

    @Query("""
        SELECT s FROM Store s
        WHERE (:market IS NULL OR s.market = :market)
        ORDER BY s.createdAt DESC
    """)
    fun findAllByMarketOrderByCreatedAtDesc(
        @Param("market") market: String?,
        pageable: Pageable
    ): Page<Store>

    @Query("""
        SELECT s FROM Store s
        LEFT JOIN s.likedStores l
        WHERE (:market IS NULL OR s.market = :market)
        GROUP BY s
        ORDER BY COUNT(l.id) DESC
    """)
    fun findAllByMarketOrderByLikeCountDesc(
        @Param("market") market: String?,
        pageable: Pageable
    ): Page<Store>
}