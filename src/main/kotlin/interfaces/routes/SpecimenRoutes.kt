package com.Biodex.interfaces.routes

import domain.models.NewSpecimenData
import domain.models.UpdateSpecimenData
import interfaces.controllers.CreateSpecimenRequest
import interfaces.controllers.UpdateSpecimenRequest
import application.services.SpecimenService
import interfaces.controllers.toResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.content.*
import java.io.File
import java.util.UUID
import kotlinx.datetime.LocalDate

fun Route.specimenRoutes(specimenService: SpecimenService) {

    route("/specimens") {

        // --- GET: OBTENER TODOS ---
        get {
            val specimens = specimenService.findAll()
            // Mapeamos cada espécimen a su DTO de respuesta
            call.respond(HttpStatusCode.OK, specimens.map { it.toResponse() })
        }

        // --- GET BY ID: OBTENER UNO ---
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de espécimen inválido.")
                return@get
            }

            // Usamos findSpecimenById (asegúrate que así se llama en tu servicio)
            val specimen = specimenService.findSpecimenById(id)

            if (specimen != null) {
                call.respond(HttpStatusCode.OK, specimen.toResponse())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // --- POST: CREAR (NUEVO FLUJO CLOUDINARY) ---
        post {
            try {
                // 1. Recibimos el JSON limpio (Adiós Multipart)
                val request = call.receive<CreateSpecimenRequest>()

                // 2. Convertimos la fecha (String -> LocalDate)
                val parsedDate = LocalDate.parse(request.collectionDate)

                // 3. Preparamos los datos para guardar
                val specimenData = NewSpecimenData(
                    idCollection = request.idCollection,
                    commonName = request.commonName,
                    idTaxonomy = request.idTaxonomy,
                    collectionDate = parsedDate,
                    collector = request.collector,
                    idLocation = request.idLocation,
                    individualsCount = request.individualsCount,
                    determinationYear = request.determinationYear,
                    determinador = request.determinador,
                    sex = request.sex,
                    vegetationType = request.vegetationType,
                    collectionMethod = request.collectionMethod,
                    notes = request.notes,

                    // Aquí asignamos las URLs que llegaron de Cloudinary
                    mainPhoto = request.mainPhoto,
                    additionalPhoto1 = request.additionalPhoto1,
                    additionalPhoto2 = request.additionalPhoto2,
                    additionalPhoto3 = request.additionalPhoto3,
                    additionalPhoto4 = request.additionalPhoto4,
                    additionalPhoto5 = request.additionalPhoto5,
                    additionalPhoto6 = request.additionalPhoto6
                )

                // 4. Guardamos en BD
                // (Asegúrate que tu servicio tenga este método, o usa createNewSpecimen como tenías antes)
                val newSpecimen = specimenService.createNewSpecimen(specimenData)

                // 5. Respondemos
                call.respond(HttpStatusCode.Created, newSpecimen.toResponse())

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Error al crear: ${e.localizedMessage}")
            }
        }

        // --- PUT: ACTUALIZAR ---
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                return@put
            }

            try {
                // 1. Recibimos JSON con campos opcionales
                val request = call.receive<UpdateSpecimenRequest>()

                // 2. Parseamos fecha solo si viene en la petición
                val parsedDate = request.collectionDate?.let { LocalDate.parse(it) }

                // 3. Preparamos datos de actualización
                val updateData = UpdateSpecimenData(
                    idCollection = request.idCollection,
                    commonName = request.commonName,
                    idTaxonomy = request.idTaxonomy,
                    collectionDate = parsedDate,
                    collector = request.collector,
                    idLocation = request.idLocation,
                    individualsCount = request.individualsCount,
                    determinationYear = request.determinationYear,
                    determinador = request.determinador,
                    sex = request.sex,
                    vegetationType = request.vegetationType,
                    collectionMethod = request.collectionMethod,
                    notes = request.notes,

                    // URLs de fotos (si se actualizaron)
                    mainPhoto = request.mainPhoto,
                    additionalPhoto1 = request.additionalPhoto1,
                    additionalPhoto2 = request.additionalPhoto2,
                    additionalPhoto3 = request.additionalPhoto3,
                    additionalPhoto4 = request.additionalPhoto4,
                    additionalPhoto5 = request.additionalPhoto5,
                    additionalPhoto6 = request.additionalPhoto6
                )

                // 4. Actualizamos
                val updatedSpecimen = specimenService.updateSpecimen(id, updateData)

                if (updatedSpecimen != null) {
                    call.respond(HttpStatusCode.OK, updatedSpecimen.toResponse())
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error al actualizar: ${e.localizedMessage}")
            }
        }

        // --- DELETE ---
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@delete
            }
            val success = specimenService.deleteSpecimen(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}