package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.Exhibition
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ExhibitionResponse(
    val idManager: Int,
    val title: String,
    val description: String,
    val category: String,
    val createdAt: LocalDate,
)
fun Exhibition.toResponse(): ExhibitionResponse = ExhibitionResponse(
    idManager = this.idManager,
    title = this.title,
    description = this.description,
    category = this.category,
    createdAt = this.createdAt
)