package com.Biodex.interfaces.routes

import com.Biodex.application.services.CollectionService
import com.Biodex.domain.models.renewCollection
import com.Biodex.interfaces.controllers.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.* // Importante para call
import io.ktor.server.request.* // Importante para receive
import io.ktor.server.response.* // Importante para respond
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDateTime

// Asegúrate de importar tu DTO CreateCollectionRequest donde lo hayas creado.
// Si lo pusiste en este mismo archivo, no hace falta import.
import com.Biodex.interfaces.controllers.CreateCollectionRequest

fun Route.collectionRoutes(collectionService: CollectionService) {

    // YA NO NECESITAMOS CREAR CARPETAS LOCALES AQUÍ

    route("/collections") {

        // --- GET BY ID ---
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                return@get
            }
            val collectionWithSpecimens = collectionService.getCollectionByIdWithSpecimens(id)
            if (collectionWithSpecimens != null) {
                call.respond(HttpStatusCode.OK, collectionWithSpecimens)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // --- GET ALL ---
        get {
            val collectionsResponse = collectionService.getAllCollectionsWithSpecimens()
            call.respond(HttpStatusCode.OK, collectionsResponse)
        }

        // --- POST (CREAR) ---
        post {
            try {
                // 1. Recibimos el JSON limpio (Adiós Multipart complejo)
                val request = call.receive<CreateCollectionRequest>()

                // Fecha actual
                val createdAt: LocalDate = LocalDateTime.now().toLocalDate().toKotlinLocalDate()

                // 2. Preparamos el objeto para el servicio
                // request.imageUrl ya contiene la URL de Cloudinary (ej: "https://res.cloudinary...")
                val collectionData = renewCollection(
                    idManager = request.idManager,
                    name = request.name,
                    description = request.description,
                    category = request.category,
                    createdAt = createdAt,
                    imageUrl = request.imageUrl
                )

                // 3. Llamamos al servicio
                val newCollection = collectionService.createCollection(collectionData)

                if (newCollection != null) {
                    call.respond(HttpStatusCode.Created, newCollection.toResponse())
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo crear la colección.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Error en los datos enviados: ${e.localizedMessage}")
            }
        }

        // --- PUT (ACTUALIZAR) ---
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id inválido")
                return@put
            }

            try {
                // También actualizamos el PUT para recibir JSON
                val request = call.receive<CreateCollectionRequest>()
                val createdAt: LocalDate = LocalDateTime.now().toLocalDate().toKotlinLocalDate()

                val collectionData = renewCollection(
                    idManager = request.idManager,
                    name = request.name,
                    description = request.description,
                    category = request.category,
                    createdAt = createdAt, // Normalmente en update no se cambia la fecha de creación, pero respeto tu lógica original
                    imageUrl = request.imageUrl
                )

                val updatedCollection = collectionService.updateCollection(id, collectionData)

                if (updatedCollection != null) {
                    call.respond(HttpStatusCode.OK, updatedCollection.toResponse())
                } else {
                    call.respond(HttpStatusCode.NotFound, "Colección no encontrada")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Error al actualizar: ${e.localizedMessage}")
            }
        }

        // --- DELETE ---
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id inválido")
                return@delete
            }
            val success = collectionService.deleteCollection(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}