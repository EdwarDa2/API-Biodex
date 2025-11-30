package com.Biodex.interfaces.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.util.*

fun Route.uploadRoutes() {
    route("/upload") {
        post("/exhibition-cover-image") {
            val uploadDir = File("uploads/exhibition_covers")
            if (!uploadDir.exists()) {
                uploadDir.mkdirs()
            }

            val multipart = call.receiveMultipart()
            var fileUrl: String? = null

            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val originalFileName = part.originalFileName ?: "unknown.tmp"
                    val fileExtension = File(originalFileName).extension
                    val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
                    val file = File(uploadDir, uniqueFileName)

                    part.streamProvider().use { input ->
                        file.outputStream().buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                    fileUrl = "/uploads/exhibition_covers/$uniqueFileName"
                }
                part.dispose()
            }

            if (fileUrl != null) {
                call.respond(HttpStatusCode.Created, fileUrl!!)
            } else {
                call.respond(HttpStatusCode.BadRequest, "No se recibió el archivo de imagen.")
            }
        }

        post("/exhibition-content-image") {
            val uploadDir = File("uploads/exhibition_content_images")
            if (!uploadDir.exists()) {
                uploadDir.mkdirs()
            }

            val multipart = call.receiveMultipart()
            var fileUrl: String? = null

            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val originalFileName = part.originalFileName ?: "unknown.tmp"
                    val fileExtension = File(originalFileName).extension
                    val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"
                    val file = File(uploadDir, uniqueFileName)

                    part.streamProvider().use { input ->
                        file.outputStream().buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                    fileUrl = "/uploads/exhibition_content_images/$uniqueFileName"
                }
                part.dispose()
            }

            if (fileUrl != null) {
                call.respond(HttpStatusCode.Created, fileUrl!!)
            } else {
                call.respond(HttpStatusCode.BadRequest, "No se recibió el archivo de imagen.")
            }
        }
    }
}
