package com.Biodex.interfaces.routes

import com.Biodex.application.services.RequestService
import com.Biodex.infrastructure.security.userId
import com.Biodex.interfaces.dto.CreateRequestDTO
import com.Biodex.interfaces.dto.UpdateRequestStatusDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.requestRoutes(requestService: RequestService) {

    authenticate("auth-jwt") {
        route("/requests") {

            // Crear solicitud
            post {
                try {
                    val userId = call.userId
                    val dto = call.receive<CreateRequestDTO>()
                    val request = requestService.createRequest(userId, dto)
                    call.respond(HttpStatusCode.Created, request)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al crear solicitud" + e.message))
                }
            }

            // Ver solicitudes enviadas por mí
            get("/sent") {
                try {
                    val userId = call.userId
                    val requests = requestService.getMySentRequests(userId)
                    call.respond(HttpStatusCode.OK, requests)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener solicitudes" + e.message))
                }
            }

            // Ver solicitudes recibidas (solo managers)
            get("/received") {
                try {
                    val userId = call.userId
                    val requests = requestService.getMyReceivedRequests(userId)
                    call.respond(HttpStatusCode.OK, requests)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener solicitudes"))
                }
            }

            // Filtrar solicitudes recibidas por estado (solo managers)
            get("/received/{status}") {
                try {
                    val userId = call.userId
                    val status = call.parameters["status"]
                        ?: throw IllegalArgumentException("Estado requerido")
                    val requests = requestService.getRequestsByStatus(userId, status)
                    call.respond(HttpStatusCode.OK, requests)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener solicitudes"))
                }
            }

            // Ver una solicitud específica
            get("/{id}") {
                try {
                    val userId = call.userId
                    val requestId = call.parameters["id"]?.toIntOrNull()
                        ?: throw IllegalArgumentException("ID inválido")
                    val request = requestService.getRequestById(requestId, userId)
                    call.respond(HttpStatusCode.OK, request)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener solicitud"))
                }
            }

            // Actualizar estado de solicitud (solo managers)
            put("/{id}/status") {
                try {
                    val userId = call.userId
                    val requestId = call.parameters["id"]?.toIntOrNull()
                        ?: throw IllegalArgumentException("ID inválido")
                    val dto = call.receive<UpdateRequestStatusDTO>()
                    val request = requestService.updateRequestStatus(requestId, userId, dto)
                    call.respond(HttpStatusCode.OK, request)
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al actualizar solicitud"))
                }
            }
        }
    }
}