package com.jayoungup.sirojungbotong.domain.store.repository

import com.jayoungup.sirojungbotong.domain.store.entity.Store
import org.springframework.data.jpa.repository.JpaRepository

interface StoreRepository : JpaRepository<Store, Long>