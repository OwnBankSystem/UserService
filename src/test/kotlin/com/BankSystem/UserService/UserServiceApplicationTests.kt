package com.BankSystem.UserService

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = [UserServiceApplication::class])
class UserServiceApplicationTests {

	@Test
	fun contextLoads() {
	}

}
