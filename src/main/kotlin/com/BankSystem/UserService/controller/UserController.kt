package com.BankSystem.UserService.controller

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.dto.JoinRequest
import com.BankSystem.UserService.dto.LoginRequest
import com.BankSystem.UserService.dto.LoginResponse
import com.BankSystem.UserService.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(@Autowired val userService: UserService) {

    @GetMapping("/getUser")
    fun getUser(): User {
        return userService.getUsername()
    }

    @GetMapping("/login")
    fun login(@RequestBody @Validated loginRequest: LoginRequest): LoginResponse {
        return userService.login(loginRequest)
    }

    @PostMapping("/join")
    fun join(@RequestBody @Validated joinRequest: JoinRequest) {
        userService.join(joinRequest)
    }
}