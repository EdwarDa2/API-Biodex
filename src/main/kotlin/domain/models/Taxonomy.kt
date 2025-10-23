package com.Biodex.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Taxonomy(
    val id: Int,
    val family: String,
    val genus: String,
    val species: String,
    val category: String
)
@Serializable
data class NewTaxonomyData(
    val family: String,
    val genus: String,
    val species: String,
    val category: String
)