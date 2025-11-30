package com.Biodex.domain.models

import domain.models.Specimen
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Collection(
    val id: Int,
    val idManager: Int,
    val name: String,
    val description: String,
    val category: String,
    val createdAt: LocalDate,
    val imageUrl: String? = null,
)
@Serializable
data class renewCollection(
    val idManager: Int,
    val name: String,
    val description: String,
    val category: String,
    val createdAt: LocalDate,
    val imageUrl: String? = null,
)