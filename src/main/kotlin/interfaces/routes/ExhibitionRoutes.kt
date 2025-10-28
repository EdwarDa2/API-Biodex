package com.Biodex.interfaces.routes

import com.Biodex.application.services.ExhibitionService
import com.Biodex.domain.models.renewExhibition
import com.Biodex.domain.models.renewExhibitionContent
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

fun Route.exhibitionRoutes(exhibitionService: ExhibitionService) {
    route("/exhibitions") {
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID  inválido.")
                return@get
            }
            val exhibition = exhibitionService.getExhibitionId(id) ?: return@get

            if (exhibition != null) {
                call.respond(HttpStatusCode.OK, exhibition)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post {
            try {
                val exhibitionData = call.receive<renewExhibition>()
                val newExhibition = exhibitionService.crateExhibition(exhibitionData)
                call.respond(HttpStatusCode.Created, "Contemnido Guardo: ${newExhibition?.id}")
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
                val exhibitionData = call.receive<renewExhibition>()
                val updateExhibition = exhibitionService.updateExhibition(id, exhibitionData)

                println("◀️ RESULTADO DEL REPOSITORIO: $updateExhibition")
                if (updateExhibition != null) {
                    call.respond(HttpStatusCode.OK, updateExhibition.toResponse())
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
                call.respond(HttpStatusCode.BadRequest, "Id invalido")
                return@delete
            }
            val exhibition = exhibitionService.deleteExhibition(id)
            if (exhibition) {
                call.respond(HttpStatusCode.NoContent, "Specimen eliminado")
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}