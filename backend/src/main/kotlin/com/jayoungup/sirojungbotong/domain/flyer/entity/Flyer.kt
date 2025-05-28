package com.jayoungup.sirojungbotong.domain.flyer.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Flyer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var storeName: String,
    var category: String,
    var description: String,
    var expireAt: LocalDateTime,
    var usesSiro: Boolean,
    var imageUrl: String? = null,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)