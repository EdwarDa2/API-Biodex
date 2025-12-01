package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.Exhibition
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateExhibitionRequest(
    val idManager: Int,
    val title: String,
    val description: String,
    val category: String,
    val createdAt: String,
    val coverImageUrl: String? = null
)

@Serializable
data class ExhibitionResponse(
    val id: Int,
    val idManager: Int,
    val title: String,
    val description: String,
    val category: String,
    val createdAt: LocalDate,
    val coverImageUrl: String?,
    val content: List<ExhibitionContentResponse> = emptyList()
)

fun Exhibition.toResponse(): ExhibitionResponse = ExhibitionResponse(
    id = this.id,
    idManager = this.idManager,
    title = this.title,
    description = this.description,
    category = this.category,
    createdAt = this.createdAt,
    coverImageUrl = this.coverImageUrl,
    content = this.content.map { it.toResponse() }
)