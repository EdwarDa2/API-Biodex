package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.SpecimenImage
import kotlinx.serialization.Serializable

@Serializable
data class SpecimenImageResponse(
    val id: Int,
    val fileName: String,
    val fileUrl: String,
    val displayOrder: Int
)

fun SpecimenImage.toResponse(): SpecimenImageResponse {
    return SpecimenImageResponse(
        id = this.id,
        fileName = this.fileName,
        fileUrl = this.fileUrl,
        displayOrder = this.displayOrder
    )
}
