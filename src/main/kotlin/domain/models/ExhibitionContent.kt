package com.Biodex.domain.models

import kotlinx.serialization.Serializable

enum class ContentTypeEnum { TEXT, IMAGE, VIDEO }
@Serializable
data class ExhibitionContent(
    val id: Int,
    val idExhibition: Int,
    val contentType: String,
    val textContent: String?,
    val imageUrl: String?,
    val displayOrder: Int
)
@Serializable
data class renewExhibitionContent(
    val idExhibition: Int,
    val contentType: String,
    val textContent: String?,
    val imageUrl: String?,
    val displayOrder: Int
)
