package com.jayoungup.sirojungbotong.domain.store.entity

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(name = "stores")
class Store(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var address: String,

    @Column(nullable = false)
    var openTime: LocalTime,

    @Column(nullable = false)
    var closeTime: LocalTime,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @Column(nullable = true)
    var businessDocumentUrl: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: Member
)