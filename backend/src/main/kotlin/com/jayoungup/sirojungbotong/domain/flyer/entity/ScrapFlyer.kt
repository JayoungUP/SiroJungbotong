package com.jayoungup.sirojungbotong.domain.flyer.entity

import com.jayoungup.sirojungbotong.domain.member.entity.Member
import jakarta.persistence.*

@Entity
@Table(name = "scrap_flyers", uniqueConstraints = [UniqueConstraint(columnNames = ["member_id", "flyer_id"])])
class ScrapFlyer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flyer_id", nullable = false)
    val flyer: Flyer
)