package com.BankSystem.UserService.service

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.domain.repository.UserRepository
import com.BankSystem.UserService.dto.JoinRequest
import com.BankSystem.UserService.util.JwtTokenProvider
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class UserServiceTest{

    @MockK
    lateinit var userRepository: UserRepository

    lateinit var userService: UserService
    private val passwordEncoder = BCryptPasswordEncoder()

    @BeforeEach
    fun setUp() {
        val jwtTokenProvider = JwtTokenProvider(
            secretKey = "12345678901234567890123456789012",
            accessTokenExpiration = 3600L,
            refreshTokenExpiration = 604800L,
            header = "Authorization",
            prefix = "Bearer "
        )

        userService = UserServiceImpl(userRepository, passwordEncoder, jwtTokenProvider)
    }

    @Test
    fun joinTest() {
        val name = "hanif"
        val phone = "010-1234-5678"
        val password = "1234"

        //save()에 전달되는 User 객체를 캡처하기 위한 용도.
        val userSlot = slot<User>()

        //시나리오 설정
        every { userRepository.findByPhoneNumber(phone) } returns null
        every { userRepository.save(capture(userSlot)) } returns mockk()

        //when
        userService.join(JoinRequest(
            accountId = name,
            accountPassword = "1234",
            username = name,
            phoneNumber = phone))

        //then
        verify(exactly = 1) { userRepository.save(any()) }
        userSlot.captured.username shouldBe name
        userSlot.captured.accountId shouldBe name
        userSlot.captured.phoneNumber shouldBe phone

        assertNotEquals(password, userSlot.captured.accountPassword)
        assertTrue(passwordEncoder.matches(password, userSlot.captured.accountPassword))
    }

//    @Test
//    fun getUserTest() {
//        val name = "hanif"
//        val user = User(accountId = name, accountPassword = "1234", username = name, phoneNumber = "010-1234-5678")
//
//        //시나리오 설정
//        every { userRepository.findByUsername(name) } returns user
//
//        //when
//        val result = userService.getUsername(name)
//
//        //then
//        result.accountId shouldBe 1
//        result.username shouldBe name
//        verify(exactly = 1) { userRepository.findByUsername(name) }
//    }
}