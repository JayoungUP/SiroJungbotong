package com.jayoungup.sirojungbotong.domain.store.entity

import com.jayoungup.sirojungbotong.domain.flyer.entity.Flyer
import com.jayoungup.sirojungbotong.domain.member.entity.Member
import jakarta.persistence.*
import org.hibernate.annotations.Formula
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "stores")
class Store(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false) // 시장
    var market: String,

    @Column(nullable = false)
    var address: String,

    @Column(nullable = false)
    var openTime: LocalTime,

    @Column(nullable = false)
    var closeTime: LocalTime,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @Column(nullable = false)
    var category: String, // 업종 카테고리


//    @Column(nullable = true)
//    var businessDocumentUrl: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: Member,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "store", cascade = [CascadeType.ALL], orphanRemoval = true)
    val flyers: MutableList<Flyer> = mutableListOf(),

    @OneToMany(mappedBy = "store", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likedStores: MutableList<LikedStore> = mutableListOf(),

    @Formula("(SELECT COUNT(ls.id) FROM liked_stores ls WHERE ls.store_id = id)")
    val likeCount: Int = 0
)