package com.BankSystem.UserService.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
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
    private val prefix: String,

    private val authService: AuthDetailsService
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

    fun resolveToken(servletRequest: HttpServletRequest): String? {
        var token = servletRequest.getHeader(header)
        return if(null != token && token.startsWith(prefix))
            token.substring(7)
        else
            null
    }

    fun validationToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getAuthentication(token: String): Authentication {
        val auth = authService.loadUserByUsername(getUserEmail(token))
        return UsernamePasswordAuthenticationToken(auth, "", auth.authorities)
    }

    fun getUserEmail(token: String?): String {
        return Jwts.parser().setSigningKey(key).build()
            .parseClaimsJws(token).body.subject
    }
}