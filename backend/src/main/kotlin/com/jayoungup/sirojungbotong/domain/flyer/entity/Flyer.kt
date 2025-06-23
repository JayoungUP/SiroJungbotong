package com.jayoungup.sirojungbotong.domain.flyer.entity

import com.jayoungup.sirojungbotong.domain.store.entity.Store
import jakarta.persistence.*
import org.hibernate.annotations.Formula
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "flyers")
class Flyer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    @Column(nullable = false)
    var category: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var expireAt: LocalDate,

    @Column(nullable = false)
    var usesSiro: Boolean,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "flyer", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<Item> = mutableListOf(),

    @OneToMany(mappedBy = "flyer", cascade = [CascadeType.ALL], orphanRemoval = true)
    val scrapFlyers: MutableList<ScrapFlyer> = mutableListOf(),

    @Formula("(SELECT COUNT(sf.id) FROM scrap_flyers sf WHERE sf.flyer_id = id)")
    val scrapCount: Int = 0
)