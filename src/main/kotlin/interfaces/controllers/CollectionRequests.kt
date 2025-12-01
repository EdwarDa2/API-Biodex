package com.Biodex.interfaces.controllers // O el paquete que estés usando para los DTOs

import kotlinx.serialization.Serializable

@Serializable
data class CreateCollectionRequest(
    val idManager: Int,
    val name: String,
    val description: String,
    val category: String,
    val imageUrl: String? = null
)

@Serializable
data class UpdateCollectionRequest(
    val name: String,
    val description: String,
    val category: String,
    val imageUrl: String? = null
)