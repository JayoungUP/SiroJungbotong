package com.jayoungup.sirojungbotong.domain.flyer.repository

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import org.springframework.data.jpa.repository.JpaRepository

interface FlyerRepository : JpaRepository<Flyer, Long>