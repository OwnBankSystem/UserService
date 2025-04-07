package com.BankSystem.UserService.controller

import com.BankSystem.UserService.dto.JoinRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `POST join - 유저 등록 성공`() {
        // given
        val request = JoinRequest("hanif")
        val json = objectMapper.writeValueAsString(request)

        // when + then
        mockMvc.post("/user/join") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `GET user - 유저 조회 성공`() {
        // given
        val username = "hanif"
        // 먼저 가입
        val json = objectMapper.writeValueAsString(JoinRequest(username))
        mockMvc.post("/user/join") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }

        // when + then
        mockMvc.get("/user") {
            param("username", username)
        }.andExpect {
            status { isOk() }
            jsonPath("$.username") { value(username) }
        }
    }
}