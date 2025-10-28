package com.Biodex.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Exhibition(
    val id: Int,
    val idManager: Int,
    val title: String,
    val description: String,
    val category: String,
    val createdAt: LocalDate,
)
@Serializable
data class renewExhibition(
    val idManager: Int,
    val title: String,
    val description: String,
    val category: String,
    val createdAt: LocalDate
)