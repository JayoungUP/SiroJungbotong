package com.jayoungup.sirojungbotong.Member.entity

import jakarta.persistence.*

enum class Role{
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
    val role: Role
)


@Entity
@Table(name = "owners")
class Owner(
    id: Long = 0,
    loginId: String,
    email: String,
    name: String,
    password: String,
    nickname: String,
    role: Role =  Role.OWNER,

    @Column(nullable = false)
    val b_no : String, // 사업자 등록 번호
) : Member(id, loginId, email, name, password, nickname, role)

@Entity
@Table(name = "users")
class User(
    id: Long = 0,
    loginId: String,
    email: String,
    name: String,
    password: String,
    nickname: String,
    role: Role =  Role.USER,
) : Member(id, loginId, email, name, password, nickname, role)

@Entity
@Table(name = "admins")
class Admin(
    id: Long = 0,
    loginId: String,
    email: String,
    name: String,
    password: String,
    nickname: String,
    role: Role =  Role.ADMIN,
) : Member(id, loginId, email, name, password, nickname, role)