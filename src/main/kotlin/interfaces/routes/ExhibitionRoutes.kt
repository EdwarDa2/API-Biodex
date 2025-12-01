package com.Biodex.interfaces.routes

import com.Biodex.application.services.ExhibitionService
import com.Biodex.domain.models.renewExhibition
import com.Biodex.interfaces.controllers.CreateExhibitionRequest
import com.Biodex.interfaces.controllers.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.* // Importante para call
import io.ktor.server.request.* // Importante para receive
import io.ktor.server.response.* // Importante para respond
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate

fun Route.exhibitionRoutes(exhibitionService: ExhibitionService) {

    route("/exhibitions") {

        // GET por ID
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                return@get
            }
            val exhibition = exhibitionService.getExhibitionId(id)

            if (exhibition != null) {
                call.respond(HttpStatusCode.OK, exhibition.toResponse())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // GET por Manager
        get("manager/{idManager}") {
            val idManager = call.parameters["idManager"]?.toIntOrNull()
            if (idManager == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de Manager inválido.")
                return@get
            }
            val exhibitions = exhibitionService.getExhibitionsByManagerId(idManager)
            // Mapeamos la lista a DTOs de respuesta
            call.respond(HttpStatusCode.OK, exhibitions.map { it.toResponse() })
        }

        // POST (CREAR) - AQUÍ ESTÁ LA MAGIA NUEVA
        post {
            try {
                // 1. Recibimos el DTO de Angular (JSON con Strings)
                val request = call.receive<CreateExhibitionRequest>()

                // 2. Convertimos la fecha String a LocalDate
                // Angular manda "YYYY-MM-DD", LocalDate.parse lo entiende directo
                val parsedDate = LocalDate.parse(request.createdAt)

                // 3. Construimos el objeto que tu servicio necesita (renewExhibition)
                // Asegúrate que 'renewExhibition' tenga el campo 'imageUrl'
                val exhibitionData = renewExhibition(
                    idManager = request.idManager,
                    title = request.title,
                    description = request.description,
                    category = request.category,
                    createdAt = parsedDate,
                    coverImageUrl = request.coverImageUrl
                )

                // 4. Llamamos al servicio
                val newExhibition = exhibitionService.crateExhibition(exhibitionData)

                // 5. Respondemos con el objeto JSON (Angular leerá el ID de aquí)
                if (newExhibition != null) {
                    call.respond(HttpStatusCode.Created, newExhibition.toResponse())
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Error al crear la exposición.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Datos inválidos: ${e.localizedMessage}")
            }
        }

        // PUT (ACTUALIZAR)
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }
            try {
                // También actualizamos el PUT para usar el DTO
                val request = call.receive<CreateExhibitionRequest>()
                val parsedDate = LocalDate.parse(request.createdAt)

                val exhibitionData = renewExhibition(
                    idManager = request.idManager,
                    title = request.title,
                    description = request.description,
                    category = request.category,
                    createdAt = parsedDate,
                    coverImageUrl = request.coverImageUrl
                )

                val updateExhibition = exhibitionService.updateExhibition(id, exhibitionData)

                if (updateExhibition != null) {
                    call.respond(HttpStatusCode.OK, updateExhibition.toResponse())
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos inválidos: ${e.localizedMessage}")
            }
        }

        // DELETE
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id invalido")
                return@delete
            }
            val success = exhibitionService.deleteExhibition(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}