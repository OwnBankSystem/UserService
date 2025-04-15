package com.BankSystem.UserService.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(

    @Value("\${auth.jwt.secret}")
    private val secretKey: String,

    @Value("\${auth.jwt.exp.access}")
    private val accessTokenExpiration: Long,

    @Value("\${auth.jwt.exp.refresh}")
    private val refreshTokenExpiration: Long,

    @Value("\${auth.jwt.header}")
    private val header: String,

    @Value("\${auth.jwt.prefix}")
    private val prefix: String
) {
    val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createAccessToken(id: String): String {
        val now = Date()
        val expiry = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .subject(id)
            .claim("type", "access_token")
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createRefreshToken(id: String): String {
        val now = Date()
        val expiry = Date(now.time + refreshTokenExpiration)

        return Jwts.builder()
            .subject(id)
            .claim("type", "refresh_token")
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}