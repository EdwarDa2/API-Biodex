package com.Biodex.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.Biodex.infrastructure.config.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureJWTAuth() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT.require(Algorithm.HMAC256(JWTConfig.getSecret()))
                    .withAudience(JWTConfig.getAudience())
                    .withIssuer(JWTConfig.getIssuer())
                    .build()
            )

            validate { credential ->
                if (credential.payload.getClaim("userId").asInt() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token inválido o expirado"))
            }
        }
    }
}

// Extension para obtener userId del token
val ApplicationCall.userId: Int
    get() = principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("userId")
        ?.asInt() ?: throw IllegalStateException("Usuario no autenticado")