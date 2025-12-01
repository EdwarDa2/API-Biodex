package com.Biodex.infrastructure.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.*

object JWTConfig {
    private const val SECRET = "biodex-secret-key-cambiar-en-produccion-2024"
    private const val ISSUER = "biodex-api"
    private const val AUDIENCE = "biodex-users"
    private const val EXPIRATION_TIME = 24 * 60 * 60 * 1000L // 24 horas

    private val algorithm = Algorithm.HMAC256(SECRET)

    fun generateToken(userId: Int, email: String, role: String): String {
        return JWT.create()
            .withAudience(AUDIENCE)
            .withIssuer(ISSUER)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(algorithm)
    }

    fun verifyToken(token: String): DecodedJWT? {
        return try {
            JWT.require(algorithm)
                .withAudience(AUDIENCE)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
        } catch (e: Exception) {
            null
        }
    }

    fun getSecret() = SECRET
    fun getIssuer() = ISSUER
    fun getAudience() = AUDIENCE
}