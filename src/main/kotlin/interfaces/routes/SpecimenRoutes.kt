package com.Biodex.interfaces.routes

import application.services.SpecimenService
import domain.models.NewSpecimenData
import domain.models.UpdateSpecimenData
import interfaces.controllers.toResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.content.*
import java.io.File
import java.util.UUID

fun Route.specimenRoutes(specimenService: SpecimenService) {
    route("/specimens") {
        get {
            val specimens = specimenService.findAll()
            call.respond(HttpStatusCode.OK, specimens.map { it.toResponse() })
        }
        get("{id}") {

            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de espécimen inválido.")
                return@get
            }
            val specimen = specimenService.findSpecimenById(id) ?: return@get

            if (specimen != null) {
                call.respond(HttpStatusCode.OK, specimen)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post {
            val multipart = call.receiveMultipart()
            var mainPhotoPath: String? = null
            val formFields = mutableMapOf<String, String>()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        if (part.name == "mainPhoto") {
                            val fileName = part.originalFileName ?: "random_file_name"
                            val fileExtension = fileName.substringAfterLast('.', "")
                            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
                            val uploadDir = File("uploads/specimens")
                            if (!uploadDir.exists()) {
                                uploadDir.mkdirs()
                            }
                            val file = File(uploadDir, uniqueFileName)
                            part.streamProvider().use { input ->
                                file.outputStream().buffered().use { output ->
                                    input.copyTo(output)
                                }
                            }
                            mainPhotoPath = "/uploads/specimens/$uniqueFileName"
                        }
                    }
                    is PartData.FormItem -> {
                        formFields[part.name!!] = part.value
                    }
                    else -> part.dispose()
                }
                part.dispose()
            }

            try {
                val specimenData = NewSpecimenData(
                    idCollection = formFields["idCollection"]?.toInt() ?: throw IllegalArgumentException("idCollection is missing"),
                    commonName = formFields["commonName"] ?: throw IllegalArgumentException("commonName is missing"),
                    idTaxonomy = formFields["idTaxonomy"]?.toInt() ?: throw IllegalArgumentException("idTaxonomy is missing"),
                    collectionDate = kotlinx.datetime.LocalDate.parse(formFields["collectionDate"] ?: throw IllegalArgumentException("collectionDate is missing")),
                    mainPhoto = mainPhotoPath,
                    collector = formFields["collector"] ?: throw IllegalArgumentException("collector is missing"),
                    idLocation = formFields["idLocation"]?.toInt() ?: throw IllegalArgumentException("idLocation is missing"),
                    individualsCount = formFields["individualsCount"]?.toInt() ?: throw IllegalArgumentException("individualsCount is missing"),
                    determinationYear = formFields["determinationYear"]?.toInt() ?: throw IllegalArgumentException("determinationYear is missing"),
                    determinador = formFields["determinador"] ?: throw IllegalArgumentException("determinador is missing"),
                    sex = formFields["sex"] ?: throw IllegalArgumentException("sex is missing"),
                    vegetationType = formFields["vegetationType"] ?: throw IllegalArgumentException("vegetationType is missing"),
                    collectionMethod = formFields["collectionMethod"] ?: throw IllegalArgumentException("collectionMethod is missing"),
                    notes = formFields["notes"]
                )
                val newSpecimen = specimenService.createNewSpecimen(specimenData)
                call.respond(HttpStatusCode.Created, "New specimen creada: ${newSpecimen.id}")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al crear espécimen: ${e.localizedMessage}")
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
                val specimenData = call.receive<UpdateSpecimenData>()
                val updatedSpecimen = specimenService.updateSpecimen(id, specimenData)

                println("◀️ RESULTADO DEL REPOSITORIO: $updatedSpecimen")
                if (updatedSpecimen != null) {
                    call.respond(HttpStatusCode.OK, updatedSpecimen.toResponse())
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
            val success = specimenService.deleteSpecimen(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent, "Specimen eliminado")
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
