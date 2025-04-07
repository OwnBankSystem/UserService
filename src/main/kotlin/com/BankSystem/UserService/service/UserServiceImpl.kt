package com.BankSystem.UserService.service

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.domain.repository.UserRepository
import com.BankSystem.UserService.dto.JoinRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    @Autowired private val userRepository: UserRepository
) : UserService {

    override fun getUsername(username: String): User {
        return userRepository.findByUsername(username)
    }

    override fun join(joinRequest: JoinRequest) {
        userRepository.save(User(username = joinRequest.username))
    }
}