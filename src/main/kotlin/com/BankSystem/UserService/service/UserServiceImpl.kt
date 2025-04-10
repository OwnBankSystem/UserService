package com.BankSystem.UserService.service

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.domain.repository.UserRepository
import com.BankSystem.UserService.dto.JoinRequest
import com.BankSystem.UserService.exception.UserAlreadyExistException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun getUsername(username: String): User {
        return userRepository.findByUsername(username)
    }

    override fun join(joinRequest: JoinRequest) {
        userRepository.findByPhoneNumber(joinRequest.phoneNumber)?.let {
            if(it.accountId == joinRequest.accountId)
                throw UserAlreadyExistException("${joinRequest.accountId} Already Joined")

            throw UserAlreadyExistException("PhoneNumber Already Joined")
        }

        val encodePassword = passwordEncoder.encode(joinRequest.accountPassword)
        userRepository.save(User(
            accountId = joinRequest.accountId,
            accountPassword = encodePassword,
            username = joinRequest.username,
            phoneNumber = joinRequest.phoneNumber
        ))
    }
}