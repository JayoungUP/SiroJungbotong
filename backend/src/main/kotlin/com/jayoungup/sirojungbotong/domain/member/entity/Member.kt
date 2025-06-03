package com.jayoungup.sirojungbotong.domain.member.entity

import jakarta.persistence.*

enum class Role{
    USER,
    OWNER,
    ADMIN
}
@Entity
@Table(name = "members")
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false,unique = true)
    val loginId: String = "",

    @Column(nullable = false)
    val email: String = "",

    @Column(nullable = false)
    val name: String = "",

    @Column(nullable = false)
    val password: String = "",

    @Column(nullable = false)
    val nickname: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @Column(name ="b_no",nullable = true)
    val bNo : String? = null
)