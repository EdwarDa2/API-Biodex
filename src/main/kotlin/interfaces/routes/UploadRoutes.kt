package com.Biodex.interfaces.routes

import com.Biodex.application.services.CloudinaryService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.uploadRoutes(cloudinaryService: CloudinaryService) {
    post("/upload") {
        // 1. Leemos el parámetro 'folder' de la URL.
        // Si no envían nada, por defecto se va a 'biodex_general'
        val type = call.request.queryParameters["folder"] ?: "general"

        // Creamos la ruta completa, ej: "biodex/specimens"
        val targetFolder = "biodex/$type"

        val multipart = call.receiveMultipart()
        var imageUrl = ""

        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                val fileBytes = part.streamProvider().readBytes()
                val fileName = part.originalFileName ?: "unknown"

                // 2. Pasamos la carpeta al servicio
                imageUrl = cloudinaryService.uploadFile(fileBytes, fileName, targetFolder)
            }
            part.dispose()
        }

        if (imageUrl.isNotEmpty()) {
            call.respond(mapOf("url" to imageUrl))
        } else {
            call.respond(HttpStatusCode.BadRequest, "Error al subir")
        }
    }
}