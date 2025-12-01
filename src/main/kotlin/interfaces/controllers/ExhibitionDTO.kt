package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.Exhibition
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateExhibitionRequest(
    val idManager: Int,
    val title: String,       // En Angular usas 'title', así que aquí también
    val description: String,
    val category: String,
    val createdAt: String,   // Recibimos String ("2025-01-01") para evitar errores de formato
    val coverImageUrl: String? = null // Aquí llega la URL de Cloudinary como texto
)

// --- RESPONSE: Lo que devuelves a Angular para MOSTRAR ---
@Serializable
data class ExhibitionResponse(
    val id: Int,             // ¡IMPORTANTE! Agregamos el ID
    val idManager: Int,
    val title: String,
    val description: String,
    val category: String,
    val createdAt: LocalDate,
    val coverImageUrl: String?    // ¡IMPORTANTE! Agregamos la URL de la portada
)

// --- MAPPER: Convierte tu Modelo de Base de Datos a Response ---
fun Exhibition.toResponse(): ExhibitionResponse = ExhibitionResponse(
    id = this.id,
    idManager = this.idManager,
    title = this.title,
    description = this.description,
    category = this.category,
    createdAt = this.createdAt,
    coverImageUrl = this.coverImageUrl // Asegúrate de que tu modelo 'Exhibition' tenga este campo
)