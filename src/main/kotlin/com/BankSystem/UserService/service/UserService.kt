package com.BankSystem.UserService.service

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.dto.JoinRequest
import com.BankSystem.UserService.dto.LoginRequest
import com.BankSystem.UserService.dto.LoginResponse

interface UserService {

    fun getUsername(): User

    fun join(joinRequest: JoinRequest)

    fun login(loginRequest: LoginRequest): LoginResponse
}