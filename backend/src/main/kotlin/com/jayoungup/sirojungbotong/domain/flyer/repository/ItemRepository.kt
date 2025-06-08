package com.jayoungup.sirojungbotong.domain.flyer.repository

import com.jayoungup.sirojungbotong.domain.flyer.entity.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    fun findAllByFlyerId(flyerId: Long): List<Item>
}