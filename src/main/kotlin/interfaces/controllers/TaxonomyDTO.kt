package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.Taxonomy
import interfaces.controllers.SpecimenResponse
import kotlinx.serialization.Serializable

@Serializable
data class TaxonomyResponse(
    val family: String,
    val genus: String,
    val species: String,
    val category: String
)

fun Taxonomy.toResponse(): TaxonomyResponse {
    return TaxonomyResponse(
        family = family,
        genus = genus,
        species = species,
        category = category
    )
}