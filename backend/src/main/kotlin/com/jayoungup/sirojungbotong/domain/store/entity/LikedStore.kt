package com.jayoungup.sirojungbotong.domain.store.entity


import com.jayoungup.sirojungbotong.domain.member.entity.Member
import jakarta.persistence.*

@Entity
@Table(name = "liked_stores")
class LikedStore(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    val store: Store,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)