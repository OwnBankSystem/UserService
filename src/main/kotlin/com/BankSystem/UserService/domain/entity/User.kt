package com.BankSystem.UserService.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "user")
class User(
    @Id
    @Column(name = "user_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val key: Long = 0,
    val username: String
)