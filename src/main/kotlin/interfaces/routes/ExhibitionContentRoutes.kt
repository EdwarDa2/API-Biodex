package com.Biodex.interfaces.routes

import com.Biodex.application.services.ExhibitionContentService
import com.Biodex.domain.models.renewExhibitionContent
import com.Biodex.interfaces.controllers.CreateExhibitionContentRequest
import com.Biodex.interfaces.controllers.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.exhibitionContentRoutes(exhibitionContentService: ExhibitionContentService) {

    route("/exhibitionContent") {

        // GET BY ID
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                return@get
            }
            val content = exhibitionContentService.getExhibitionContentId(id)

            if (content != null) {
                call.respond(HttpStatusCode.OK, content.toResponse())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // POST (CREAR)
        post {
            try {
                // 1. Recibir DTO
                val request = call.receive<CreateExhibitionContentRequest>()

                // 2. Construir modelo interno (renewExhibitionContent)
                val contentData = renewExhibitionContent(
                    idExhibition = request.idExhibition,
                    contentType = request.contentType,
                    textContent = request.textContent,
                    imageUrl = request.imageUrl, // URL de Cloudinary
                    imageDescription = request.imageDescription,
                    displayOrder = request.displayOrder
                )

                // 3. Llamar servicio
                val newContent = exhibitionContentService.createExhibitionContent(contentData)

                // 4. Responder objeto JSON
                if (newContent != null) {
                    call.respond(HttpStatusCode.Created, newContent.toResponse())
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo guardar el contenido.")
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
                call.respond(HttpStatusCode.BadRequest, "Id invalido")
                return@put
            }
            try {
                val request = call.receive<CreateExhibitionContentRequest>()

                val contentData = renewExhibitionContent(
                    idExhibition = request.idExhibition,
                    contentType = request.contentType,
                    textContent = request.textContent,
                    imageUrl = request.imageUrl,
                    imageDescription = request.imageDescription,
                    displayOrder = request.displayOrder
                )

                val updateContent = exhibitionContentService.updateExhibitionContent(id, contentData)

                if (updateContent != null) {
                    call.respond(HttpStatusCode.OK, updateContent.toResponse())
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
                call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                return@delete
            }
            val success = exhibitionContentService.deleteContent(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}