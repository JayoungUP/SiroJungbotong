package com.jayoungup.sirojungbotong.domain.member.entity

import jakarta.persistence.*

enum class Role {
    USER,
    OWNER,
    ADMIN
}

@Entity
@Table(name = "members")
@Inheritance(strategy = InheritanceType.JOINED)
open class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role
)

@Entity
@Table(name = "email_users")
class EmailUser(
    name: String,
    nickname: String,
    role: Role = Role.USER,

    @Column(nullable = false, unique = true)
    val loginId: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    var password: String

) : Member(
    name = name,
    nickname = nickname,
    role = role
)
@Entity
@Table(name = "kakao_users")
class KakaoUser(
    name: String,
    nickname: String,
    role: Role = Role.USER,

    @Column(nullable = false, unique = true)
    val kakaoId: String // 보통 email 사용 or 카카오 고유 id

) : Member(
    name = name,
    nickname = nickname,
    role = role
)
@Entity
@Table(name = "email_owners")
class EmailOwner(
    name: String,
    nickname: String,
    role: Role = Role.OWNER,

    @Column(nullable = false, unique = true)
    val loginId: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    val bNo: String

) : Member(
    name = name,
    nickname = nickname,
    role = role
)
@Entity
@Table(name = "kakao_owners")
class KakaoOwner(
    name: String,
    nickname: String,
    role: Role = Role.OWNER,

    @Column(nullable = false, unique = true)
    val kakaoId: String,

    @Column(nullable = false)
    val bNo: String

) : Member(
    name = name,
    nickname = nickname,
    role = role
)




@Entity
@Table(name = "admins")
class Admin(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val loginId: String,

    @Column(nullable = false)
    val password: String

)
