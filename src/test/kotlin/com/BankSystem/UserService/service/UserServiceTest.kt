package com.BankSystem.UserService.service

import com.BankSystem.UserService.domain.entity.User
import com.BankSystem.UserService.domain.repository.UserRepository
import com.BankSystem.UserService.dto.JoinRequest
import com.BankSystem.UserService.dto.LoginRequest
import com.BankSystem.UserService.exception.UserAlreadyExistException
import com.BankSystem.UserService.exception.UserNotFoundException
import com.BankSystem.UserService.util.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
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

    @Test
    fun `회원가입 실패 - 이미 있는 전화번호`() {
        joinTest()
        val name = "hanif"
        val encodedPassword = passwordEncoder.encode("1234")
        val user = User(id = 1, accountId = name, accountPassword = encodedPassword, username = name, phoneNumber = "010-1234-5678")
        val request = JoinRequest(
            accountId = "hanif2",
            accountPassword = "1234",
            username = "hanif2",
            phoneNumber = "010-1234-5678")

        //시나리오 설정
        every { userRepository.findByPhoneNumber(user.phoneNumber) } returns user

        //when + then
        shouldThrow<UserAlreadyExistException> {
            userService.join(request)
        }.message shouldBe "PhoneNumber Already Joined"
    }

    @Test
    fun `회원가입 실패 - 이미 가입된 유저`() {
        joinTest()
        val name = "hanif"
        val encodedPassword = passwordEncoder.encode("1234")
        val user = User(id = 1, accountId = name, accountPassword = encodedPassword, username = name, phoneNumber = "010-1234-5678")
        val request = JoinRequest(
            accountId = name,
            accountPassword = "1234",
            username = name,
            phoneNumber = "010-1234-5678")

        //시나리오 설정
        every { userRepository.findByPhoneNumber(user.phoneNumber) } returns user

        //when + then
        shouldThrow<UserAlreadyExistException> {
            userService.join(request)
        }.message shouldBe "${name} Already Joined"
    }

    @Test
    fun loginTest() {
        val request = LoginRequest("hanif", "1234")
        val encodedPassword = passwordEncoder.encode("1234")
        val user = User(id = 1, accountId = request.accountId, accountPassword = encodedPassword, username = request.accountId, phoneNumber = "010-1234-5678")

        //시나리오 설정
        every { userRepository.findByAccountId("hanif") } returns user

        //when
        val result = userService.login(request)

        //then
        result.accessToken shouldNotBe null
        result.refreshToken shouldNotBe null
        verify(exactly = 1) { userRepository.findByAccountId(request.accountId) }
    }

    @Test
    fun `로그인 실패 - id가 없음`() {
        val request = LoginRequest("hanif2", "1234")

        //시나리오 설정
        every { userRepository.findByAccountId("hanif2") } returns null

        //when + then
        shouldThrow<UserNotFoundException> {
            userService.login(request)
        }.message shouldBe "User Not Found"
    }

    @Test
    fun `로그인 실패 - password가 다름`() {
        val request = LoginRequest("hanif", "12345")
        val encodedPassword = passwordEncoder.encode("1234")
        val user = User(id = 1, accountId = request.accountId, accountPassword = encodedPassword, username = request.accountId, phoneNumber = "010-1234-5678")

        //시나리오 설정
        every { userRepository.findByAccountId("hanif") } returns user

        //when + then
        shouldThrow<UserNotFoundException> {
            userService.login(request)
        }.message shouldBe "Password Not Match"
    }
}