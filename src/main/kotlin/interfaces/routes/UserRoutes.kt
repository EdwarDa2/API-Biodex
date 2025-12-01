package com.Biodex.interfaces.routes

import com.Biodex.application.services.UserService
import com.Biodex.infrastructure.security.userId
import com.Biodex.interfaces.controllers.*
//import com.Biodex.interfaces.controllers.RegisterRequest
//import com.Biodex.interfaces.controllers.UpdateProfileRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService) {

    route("/auth") {
        post("/register") {
            try {
                val request = call.receive<com.Biodex.interfaces.controllers.UserRegister>()
                val user = userService.register(request)
                call.respond(HttpStatusCode.Created, user)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al registrar usuario"))
            }
        }

        post("/login") {
            try {
                val request = call.receive<com.Biodex.interfaces.controllers.UserLogin>()
                val response = userService.login(request)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al iniciar sesión"))
            }
        }
    }

    authenticate("auth-jwt") {
        route("/me") {
            get {
                try {
                    val userId = call.userId
                    val user = userService.getProfile(userId)
                    call.respond(HttpStatusCode.OK, user)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener perfil"))
                }
            }

            put {
                try {
                    val userId = call.userId
                    val request = call.receive<com.Biodex.interfaces.controllers.UpdateUser>()
                    val user = userService.updateProfile(userId, request)
                    call.respond(HttpStatusCode.OK, user)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al actualizar perfil"))
                }
            }
        }
    }
}