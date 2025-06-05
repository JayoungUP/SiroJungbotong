package com.jayoungup.sirojungbotong.domain.flyer.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "items")
class Item(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var price: Int,

    @Column(nullable = false)
    var validFrom: LocalDate,

    @Column(nullable = false)
    var validUntil: LocalDate,

    @Column
    var imageUrl: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flyer_id", nullable = false)
    var flyer: Flyer
)