package com.Biodex.interfaces.routes

import com.Biodex.application.services.CollectionService
import com.Biodex.domain.models.renewCollection
import com.Biodex.domain.repositorys.CollectionRepository
import com.Biodex.interfaces.controllers.toResponse
import domain.models.NewSpecimenData
import domain.models.UpdateSpecimenData
import interfaces.controllers.toResponse
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

fun Route.collectionRoutes(collectionService: CollectionService) {
    route("/collections") {
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                return@get
            }

            // ¡Llama al "Gerente" (el ensamblador), NO al mapper simple!
            val collectionWithSpecimens = collectionService.getCollectionByIdWithSpecimens(id)

            if (collectionWithSpecimens != null) {
                // Responde con el objeto COMPLETO que te dio el servicio
                call.respond(HttpStatusCode.OK, collectionWithSpecimens)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        get {
            val collectionsResponse = collectionService.getAllCollectionsWithSpecimens()
            call.respond(HttpStatusCode.OK, collectionsResponse)
        }
        post{
            try {
                val collectionData = call.receive<renewCollection>()
                val renewCollection = collectionService.createCollection(collectionData)
                call.respond(HttpStatusCode.Created, "New collection creada: ${renewCollection?.id}")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Datos invaldos: ${e.localizedMessage}")
            }
        }
        put("{id}"){
            val id = call.parameters["id"]?.toIntOrNull()

            println("▶️ INTENTANDO ACTUALIZAR. ID RECIBIDO: $id")
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id invalido")
                return@put
            }
            try {
                val collectionData = call.receive<renewCollection>()
                val updateCollection = collectionService.updateCollection(id, collectionData)

                println("◀️ RESULTADO DEL REPOSITORIO: $updateCollection")
                if (updateCollection != null) {
                    call.respond(HttpStatusCode.OK,updateCollection.toResponse())
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
            val success = collectionService.deleteCollection(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent, "collection eliminado")
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}