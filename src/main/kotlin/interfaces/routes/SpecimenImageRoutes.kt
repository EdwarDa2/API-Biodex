package com.Biodex.interfaces.routes

import com.Biodex.application.services.SpecimenImageService
import com.Biodex.domain.models.SpecimenImage
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.util.*

fun Route.specimenImageRoutes(specimenImageService: SpecimenImageService) {


    route("/specimen_images") {

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de Imagen inválido.")
                return@get
            }


            val specimenImage = specimenImageService.getImage(id)

            if (specimenImage != null) {
                call.respond(HttpStatusCode.OK, specimenImage)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("{id}/images") {
            val idSpecimen = call.parameters["id"]?.toIntOrNull()
            if (idSpecimen == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de Espécimen inválido")
                return@post
            }


            val uploadDir = File("uploads")
            if (!uploadDir.exists()) {
                uploadDir.mkdirs()
            }


            var fileName = ""
            var fileUrl = ""
            var displayOrder = 1


            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                when (part) {

                    is PartData.FileItem -> {
                        val originalFileName = part.originalFileName ?: "unknown.tmp"
                        val fileExtension = File(originalFileName).extension
                        val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
                        val file = File(uploadDir, uniqueFileName)


                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }


                        fileName = originalFileName
                        fileUrl = "/uploads/$uniqueFileName"
                    }


                    is PartData.FormItem -> {
                        if (part.name == "display_order") {
                            displayOrder = part.value.toIntOrNull() ?: 1
                        }
                    }


                    else -> part.dispose()
                }
                part.dispose()
            }


            if (fileName.isNotBlank() && fileUrl.isNotBlank()) {


                val newSpecimenImage = SpecimenImage(
                    id = 0,
                    idSpecimen = idSpecimen,
                    fileName = fileName,
                    fileUrl = fileUrl,
                    displayOrder = displayOrder
                )


                val imageGuardada = specimenImageService.addImage(newSpecimenImage)

                if (imageGuardada != null) {
                    call.respond(HttpStatusCode.Created, imageGuardada)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Error al guardar en la BD")
                }

            } else {
                call.respond(HttpStatusCode.BadRequest, "No se recibió el archivo")
            }
        }


        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id inválido")
                return@delete
            }

            // (Lógica opcional: eliminar el archivo físico de la carpeta "uploads")
            val image = specimenImageService.getImage(id)
            if (image != null) {
                File("uploads/${image.fileName}").delete()
            }


            val success = specimenImageService.deleteImage(id)

            if (success) {
                call.respond(HttpStatusCode.NoContent, "Imagen eliminada")
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}