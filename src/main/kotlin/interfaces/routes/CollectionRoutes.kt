package com.Biodex.interfaces.routes

import com.Biodex.application.services.CollectionService
import com.Biodex.domain.models.renewCollection
import com.Biodex.interfaces.controllers.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.core.readBytes
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.UUID

fun Route.collectionRoutes(collectionService: CollectionService) {
    val uploadDirPath = "uploads/collections"
    val uploadDir = File(uploadDirPath)
    if (!uploadDir.exists()) {
        uploadDir.mkdirs()
    }

    route("/collections") {
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

        get {
            val collectionsResponse = collectionService.getAllCollectionsWithSpecimens()
            call.respond(HttpStatusCode.OK, collectionsResponse)
        }

        post {
            println("📥 POST /collections - Recibiendo multipart...")
            val multipart = call.receiveMultipart()
            var name: String? = null
            var description: String? = null
            var category: String? = null
            var idManager: Int? = null
            var imageUrl: String? = null
            val createdAt: LocalDate = LocalDateTime.now().toLocalDate().toKotlinLocalDate()

            multipart.forEachPart { part ->
                println("🔍 Part recibida: ${part.name}, tipo: ${part::class.simpleName}")
                when (part) {
                    is PartData.FormItem -> {
                        println("📝 Campo: ${part.name} = ${part.value}")
                        when (part.name) {
                            "name" -> name = part.value
                            "description" -> description = part.value
                            "category" -> category = part.value
                            "idManager" -> idManager = part.value.toIntOrNull()
                        }
                    }
                    is PartData.FileItem -> {
                        if (part.name == "image") {
                            val fileName = part.originalFileName ?: "image"
                            val fileExtension = fileName.substringAfterLast(".", "jpg")
                            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"

                            println("📁 Archivo recibido: $fileName")
                            println("   ContentType: ${part.contentType}")

                            // ✅ CORRECTO: usar streamProvider()
                            val fileBytes = part.provider().readRemaining().readBytes()
                            val filePath = Paths.get(uploadDirPath, uniqueFileName)
                            Files.write(filePath, fileBytes)
                            imageUrl = "/$uploadDirPath/$uniqueFileName"

                            println("✅ Imagen guardada: $imageUrl (${fileBytes.size} bytes)")
                        }
                    }
                    else -> {
                        println("❓ Tipo desconocido: ${part::class.simpleName}")
                    }
                }
                part.dispose()
            }

            println("📊 Datos recibidos: name=$name, desc=$description, cat=$category, idMgr=$idManager, img=$imageUrl")

            if (name == null || description == null || category == null || idManager == null) {
                call.respond(HttpStatusCode.BadRequest, "Faltan campos obligatorios.")
                return@post
            }

            try {
                val collectionData = renewCollection(
                    idManager = idManager!!,
                    name = name!!,
                    description = description!!,
                    category = category!!,
                    createdAt = createdAt,
                    imageUrl = imageUrl
                )
                val newCollection = collectionService.createCollection(collectionData)
                if (newCollection != null) {
                    println("✅ Colección creada exitosamente: ID=${newCollection.id}")
                    call.respond(HttpStatusCode.Created, newCollection.toResponse())
                } else {
                    println("❌ Error: createCollection retornó null")
                    call.respond(HttpStatusCode.InternalServerError, "No se pudo crear la colección.")
                }
            } catch (e: Exception) {
                println("❌ Excepción al crear colección: ${e.message}")
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error al crear la colección: ${e.localizedMessage}")
            }
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            println("▶ INTENTANDO ACTUALIZAR. ID RECIBIDO: $id")
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id inválido")
                return@put
            }

            val multipart = call.receiveMultipart()
            var name: String? = null
            var description: String? = null
            var category: String? = null
            var idManager: Int? = null
            var imageUrl: String? = null
            val createdAt: LocalDate = LocalDateTime.now().toLocalDate().toKotlinLocalDate()

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "name" -> name = part.value
                            "description" -> description = part.value
                            "category" -> category = part.value
                            "idManager" -> idManager = part.value.toIntOrNull()
                        }
                    }
                    is PartData.FileItem -> {
                        if (part.name == "image") {
                            val fileName = part.originalFileName ?: "image"
                            val fileExtension = fileName.substringAfterLast(".", "jpg")
                            val uniqueFileName = "${UUID.randomUUID()}.$fileExtension"

                            // ✅ Usar streamProvider() consistentemente
                            val fileBytes = part.provider().readRemaining().readBytes()
                            val filePath = Paths.get(uploadDirPath, uniqueFileName)
                            Files.write(filePath, fileBytes)
                            imageUrl = "/$uploadDirPath/$uniqueFileName"
                        }
                    }
                    else -> {}
                }
                part.dispose()
            }

            if (name == null || description == null || category == null || idManager == null) {
                call.respond(HttpStatusCode.BadRequest, "Faltan campos obligatorios.")
                return@put
            }

            try {
                val collectionData = renewCollection(
                    idManager = idManager!!,
                    name = name!!,
                    description = description!!,
                    category = category!!,
                    createdAt = createdAt,
                    imageUrl = imageUrl
                )
                val updateCollection = collectionService.updateCollection(id, collectionData)

                println("◀ RESULTADO DEL REPOSITORIO: $updateCollection")
                if (updateCollection != null) {
                    call.respond(HttpStatusCode.OK, updateCollection.toResponse())
                } else {
                    println("❌ NO SE ENCONTRÓ EL ID $id. ENVIANDO 404.")
                    call.respond(HttpStatusCode.NotFound)
                }
            } catch (e: Exception) {
                println("❌ Error al actualizar: ${e.message}")
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, "Datos inválidos: ${e.localizedMessage}")
            }
        }

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