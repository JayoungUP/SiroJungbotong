package com.jayoungup.sirojungbotong.domain.flyer.repository

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FlyerRepository : JpaRepository<Flyer, Long> {

    @Query("""
        SELECT f FROM Flyer f
        WHERE (:market IS NULL OR f.store.market = :market)
        AND (:category IS NULL OR f.category = :category)
        AND (:usesSiro IS NULL OR f.usesSiro = :usesSiro)
    """)
    fun findAllFilteredForSorting(
        @Param("market") market: String?,
        @Param("category") category: String?,
        @Param("usesSiro") usesSiro: Boolean?,
        pageable: Pageable
    ): Page<Flyer>

    @Query("""
        SELECT f FROM Flyer f
        WHERE (:market IS NULL OR f.store.market = :market)
        AND (:category IS NULL OR f.category = :category)
        AND (:usesSiro IS NULL OR f.usesSiro = :usesSiro)
    """)
    fun findAllFilteredForManualSorting(
        @Param("market") market: String?,
        @Param("category") category: String?,
        @Param("usesSiro") usesSiro: Boolean?
    ): List<Flyer>
}