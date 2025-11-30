package com.Biodex.interfaces.routes

import com.Biodex.application.services.ExhibitionContentService
import com.Biodex.application.services.ExhibitionService
import com.Biodex.domain.models.renewExhibitionContent
import com.Biodex.domain.models.renewLocation
import com.Biodex.interfaces.controllers.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlin.text.toIntOrNull

fun Route.exhibitionContentRoutes(exhibitionContentService: ExhibitionContentService) {
    route("/exhibitionContent") {
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID  inválido.")
                return@get
            }
            val content = exhibitionContentService.getExhibitionContentId(id) ?: return@get

            if (content != null) {
                call.respond(HttpStatusCode.OK, content)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post {
            try {
                val contentData = call.receive<renewExhibitionContent>()
                val newContentData = exhibitionContentService.createExhibitionContent(contentData)
                call.respond(HttpStatusCode.Created, "Contemnido Guardo: ${newContentData?.id}")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Datos invaldos: ${e.localizedMessage}")
            }
        }
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            println("▶️ INTENTANDO ACTUALIZAR. ID RECIBIDO: $id")
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id invalido")
                return@put
            }
            try {
                val contentData = call.receive<renewExhibitionContent>()
                val updateContent = exhibitionContentService.updateExhibitionContent(id, contentData)

                println("◀️ RESULTADO DEL REPOSITORIO: $updateContent")
                if (updateContent != null) {
                    call.respond(HttpStatusCode.OK, updateContent.toResponse())
                } else {
                    println("❌ NO SE ENCONTRÓ EL ID $id. ENVIANDO 404.")
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos invaldos: ${e.localizedMessage}")
            }
        }
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                return@delete
            }
            // Llama a la función renombrada y CORREGIDA del servicio
            val success = exhibitionContentService.deleteContent(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent) // Éxito
            } else {
                call.respond(HttpStatusCode.NotFound) // No encontrado
            }
        }
    }

}