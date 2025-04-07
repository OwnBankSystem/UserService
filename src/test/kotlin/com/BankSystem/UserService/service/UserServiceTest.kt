package com.BankSystem.UserService.service

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.domain.repository.UserRepository
import com.BankSystem.UserService.dto.JoinRequest
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class UserServiceTest {

    @MockK
    lateinit var userRepository: UserRepository

    lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userService = UserServiceImpl(userRepository)
    }

    @Test
    fun joinTest() {
        val name = "hanif"
        //save()에 전달되는 User 객체를 캡처하기 위한 용도.
        val userSlot = slot<User>()

        //시나리오 설정
        every { userRepository.save(capture(userSlot)) } returns mockk()

        //when
        userService.join(JoinRequest(name))

        //then
        verify(exactly = 1) { userRepository.save(any()) }
        userSlot.captured.username shouldBe name
    }

    @Test
    fun getUserTest() {
        val name = "hanif"
        val user = User(1, name)

        //시나리오 설정
        every { userRepository.findByUsername(name) } returns user

        //when
        val result = userService.getUsername(name)

        //then
        result.key shouldBe 1
        result.username shouldBe name
        verify(exactly = 1) { userRepository.findByUsername(name) }
    }
}