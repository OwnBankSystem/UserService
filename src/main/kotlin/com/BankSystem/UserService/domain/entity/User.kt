package com.BankSystem.UserService.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "account_id", nullable = false, unique = true)
    val accountId: String,

    @Column(name = "account_password", nullable = false)
    val accountPassword: String,

    @Column(name = "user_name", nullable = false)
    val username: String,

    @Column(name = "phone_number", nullable = false, unique = true)
    val phoneNumber: String
) {
    //JPA에서 기본 생성자가 없어서 생기는 문제 해결을 위한 코드
    protected constructor() : this(0, "", "", "", "")
}