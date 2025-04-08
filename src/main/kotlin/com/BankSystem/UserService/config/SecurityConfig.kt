package com.BankSystem.UserService.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } //Cross-Site Request Forgery 보호를 비활성화
            .authorizeHttpRequests { //authorization 규칙 정의
                it
                    .requestMatchers("/user/join").permitAll()  // 회원가입은 인증 없이 접근 허용
                    .anyRequest().authenticated()                         // 나머지는 인증 필요
            }
        return http.build()
    }
}