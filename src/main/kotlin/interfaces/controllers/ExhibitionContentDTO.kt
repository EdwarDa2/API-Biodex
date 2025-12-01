package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.ExhibitionContent
import kotlinx.serialization.Serializable

@Serializable
data class CreateExhibitionContentRequest(
    val idExhibition: Int,
    val contentType: String,       // "TEXT" o "IMAGE"
    val textContent: String? = null,
    val imageUrl: String? = null,  // La URL de Cloudinary como texto
    val imageDescription: String? = null,
    val displayOrder: Int
)

@Serializable
data class ExhibitionContentResponse(
    val id: Int,                   // Agregamos ID
    val idExhibition: Int,
    val contentType: String,
    val textContent: String?,
    val imageUrl: String?,
    val imageDescription: String?,
    val displayOrder: Int
)

fun ExhibitionContent.toResponse(): ExhibitionContentResponse = ExhibitionContentResponse(
    id = this.id,
    idExhibition = this.idExhibition,
    contentType = this.contentType,
    textContent = this.textContent,
    imageUrl = this.imageUrl,
    imageDescription = this.imageDescription,
    displayOrder = this.displayOrder
)