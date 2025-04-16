package com.BankSystem.UserService.service

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.domain.repository.UserRepository
import com.BankSystem.UserService.dto.JoinRequest
import com.BankSystem.UserService.dto.LoginRequest
import com.BankSystem.UserService.dto.LoginResponse
import com.BankSystem.UserService.exception.UserAlreadyExistException
import com.BankSystem.UserService.exception.UserNotFoundException
import com.BankSystem.UserService.security.AuthenticationFacade
import com.BankSystem.UserService.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordEncoder: PasswordEncoder,
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val authenticationFacade: AuthenticationFacade
) : UserService {

    override fun getUsername(): User {
        return userRepository.findByAccountId(authenticationFacade.getAccountId())
            ?: throw UserNotFoundException("User Not Found")
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

    override fun login(loginRequest: LoginRequest): LoginResponse {
        var user = userRepository.findByAccountId(loginRequest.accountId)?: throw UserNotFoundException("User Not Found")
        if(!passwordEncoder.matches(loginRequest.accountPassword, user.accountPassword)) throw UserNotFoundException("Password Not Match")

        return tokenResponse(loginRequest.accountId)
    }

    private fun tokenResponse(accountId: String): LoginResponse {
        val accessToken = jwtTokenProvider.createAccessToken(accountId)
        val refreshToken = jwtTokenProvider.createRefreshToken(accessToken)

        return LoginResponse(accessToken, refreshToken)
    }
}