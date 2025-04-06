package com.BankSystem.UserService.controller

import com.BankSystem.UserService.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(@Autowired val userService: UserService) {

    @GetMapping
    fun Test(): String {
        return "Hello, World"
    }
}