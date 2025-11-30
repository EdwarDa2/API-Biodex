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
import kotlinx.datetime.LocalDate

// Helper function to save image files
suspend fun saveImageFile(part: PartData.FileItem, uploadDir: File): String {
    val fileName = part.originalFileName ?: "random_file_name"
    val fileExtension = fileName.substringAfterLast('.', "")
    val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
    if (!uploadDir.exists()) {
        uploadDir.mkdirs()
    }
    val file = File(uploadDir, uniqueFileName)
    part.streamProvider().use { input ->
        file.outputStream().buffered().use { output ->
            input.copyTo(output)
        }
    }
    return "/uploads/specimens/$uniqueFileName"
}

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
            val additionalPhotoPaths = mutableListOf<String>()
            val formFields = mutableMapOf<String, String>()
            val uploadDir = File("uploads/specimens")

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        when (part.name) {
                            "mainPhoto" -> mainPhotoPath = saveImageFile(part, uploadDir)
                            "additionalPhoto1", "additionalPhoto2", "additionalPhoto3", "additionalPhoto4", "additionalPhoto5", "additionalPhoto6" -> {
                                additionalPhotoPaths.add(saveImageFile(part, uploadDir))
                            }
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
                    collectionDate = LocalDate.parse(formFields["collectionDate"] ?: throw IllegalArgumentException("collectionDate is missing")),
                    mainPhoto = mainPhotoPath,
                    collector = formFields["collector"] ?: throw IllegalArgumentException("collector is missing"),
                    idLocation = formFields["idLocation"]?.toInt() ?: throw IllegalArgumentException("idLocation is missing"),
                    individualsCount = formFields["individualsCount"]?.toInt() ?: throw IllegalArgumentException("individualsCount is missing"),
                    determinationYear = formFields["determinationYear"]?.toInt() ?: throw IllegalArgumentException("determinationYear is missing"),
                    determinador = formFields["determinador"] ?: throw IllegalArgumentException("determinador is missing"),
                    sex = formFields["sex"] ?: throw IllegalArgumentException("sex is missing"),
                    vegetationType = formFields["vegetationType"] ?: throw IllegalArgumentException("vegetationType is missing"),
                    collectionMethod = formFields["collectionMethod"] ?: throw IllegalArgumentException("collectionMethod is missing"),
                    notes = formFields["notes"],
                    additionalPhoto1 = additionalPhotoPaths.getOrNull(0),
                    additionalPhoto2 = additionalPhotoPaths.getOrNull(1),
                    additionalPhoto3 = additionalPhotoPaths.getOrNull(2),
                    additionalPhoto4 = additionalPhotoPaths.getOrNull(3),
                    additionalPhoto5 = additionalPhotoPaths.getOrNull(4),
                    additionalPhoto6 = additionalPhotoPaths.getOrNull(5)
                )
                val newSpecimen = specimenService.createNewSpecimen(specimenData)
                call.respond(HttpStatusCode.Created, "New specimen creada: ${newSpecimen.id}")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al crear espécimen: ${e.localizedMessage}")
            }
        }
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de espécimen inválido.")
                return@put
            }

            val multipart = call.receiveMultipart()
            var mainPhotoPath: String? = null
            val additionalPhotoPaths = mutableListOf<String>()
            val formFields = mutableMapOf<String, String>()
            val uploadDir = File("uploads/specimens")

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        when (part.name) {
                            "mainPhoto" -> mainPhotoPath = saveImageFile(part, uploadDir)
                            "additionalPhoto1", "additionalPhoto2", "additionalPhoto3", "additionalPhoto4", "additionalPhoto5", "additionalPhoto6" -> {
                                additionalPhotoPaths.add(saveImageFile(part, uploadDir))
                            }
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
                val specimenData = UpdateSpecimenData(
                    idCollection = formFields["idCollection"]?.toInt(),
                    commonName = formFields["commonName"],
                    idTaxonomy = formFields["idTaxonomy"]?.toInt(),
                    collectionDate = formFields["collectionDate"]?.let { LocalDate.parse(it) },
                    mainPhoto = mainPhotoPath,
                    collector = formFields["collector"],
                    idLocation = formFields["idLocation"]?.toInt(),
                    individualsCount = formFields["individualsCount"]?.toInt(),
                    determinationYear = formFields["determinationYear"]?.toInt(),
                    determinador = formFields["determinador"],
                    sex = formFields["sex"],
                    vegetationType = formFields["vegetationType"],
                    collectionMethod = formFields["collectionMethod"],
                    notes = formFields["notes"],
                    additionalPhoto1 = additionalPhotoPaths.getOrNull(0),
                    additionalPhoto2 = additionalPhotoPaths.getOrNull(1),
                    additionalPhoto3 = additionalPhotoPaths.getOrNull(2),
                    additionalPhoto4 = additionalPhotoPaths.getOrNull(3),
                    additionalPhoto5 = additionalPhotoPaths.getOrNull(4),
                    additionalPhoto6 = additionalPhotoPaths.getOrNull(5)
                )
                val updatedSpecimen = specimenService.updateSpecimen(id, specimenData)

                if (updatedSpecimen != null) {
                    call.respond(HttpStatusCode.OK, updatedSpecimen.toResponse())
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error al actualizar espécimen: ${e.localizedMessage}")
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