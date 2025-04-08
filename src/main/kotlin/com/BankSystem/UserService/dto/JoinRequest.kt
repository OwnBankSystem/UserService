package com.BankSystem.UserService.dto

class JoinRequest(
    val accountId: String,
    val accountPassword: String,
    val username: String,
    val phoneNumber: String
)