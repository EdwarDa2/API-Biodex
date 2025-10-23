package com.Biodex.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SpecimenImage(
    val id: Int,
    val idSpecimen: Int,
    val fileName: String,
    val fileUrl: String,
    val displayOrder: Int
)
