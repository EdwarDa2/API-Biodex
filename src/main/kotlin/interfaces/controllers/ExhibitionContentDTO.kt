package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.ExhibitionContent
import kotlinx.serialization.Serializable

@Serializable
data class ExhibitionContentResponse(
    val idExhibition: Int,
    val contentType: String,
    val textContent: String?,
    val imageUrl: String?,
    val displayOrder: Int
)
fun ExhibitionContent.toResponse(): ExhibitionContentResponse = ExhibitionContentResponse(
    idExhibition = this.idExhibition,
    contentType = this.contentType,
    textContent = this.textContent,
    imageUrl = this.imageUrl,
    displayOrder = this.displayOrder
)
