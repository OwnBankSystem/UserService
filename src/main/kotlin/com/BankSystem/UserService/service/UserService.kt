package com.BankSystem.UserService.service

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.dto.JoinRequest

interface UserService {

    fun getUsername(username: String): User

    fun join(joinRequest: JoinRequest)
}