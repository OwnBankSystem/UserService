package com.BankSystem.UserService.domain.repository

import com.BankSystem.UserService.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User

    fun findByAccountId(accountId: String): User?

    fun findByPhoneNumber(phoneNumber: String): User?
}