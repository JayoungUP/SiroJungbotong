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
    """)
    fun findAllByMarket(
        @Param("market") market: String?,
        pageable: Pageable
    ): Page<Flyer>

    @Query("""
        SELECT f FROM Flyer f
        WHERE (:market IS NULL OR f.store.market = :market)
    """)
    fun findAllByMarketWithoutPage(
        @Param("market") market: String?
    ): List<Flyer>
}