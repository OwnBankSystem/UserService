package com.BankSystem.UserService.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationFacade {

    fun getAccountId(): String = getAuthentication().name

    private fun getAuthentication(): Authentication = SecurityContextHolder.getContext().authentication
}