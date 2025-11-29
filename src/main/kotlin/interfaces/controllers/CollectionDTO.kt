package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.Collection
import com.Biodex.domain.models.SpecimenImage
import domain.models.Specimen
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CollectionResponse(
    val id: Int,
    val idManager: Int,
    val name: String,
    val description: String,
    val category: String,
    val createdAt : LocalDate,
    val imageUrl: String?,
    val specimens: List<SpecimenResponse>

)
@Serializable
data class SpecimenResponse(
    val id: Int,
    val idCollection: Int?,
    val commonName: String,
    val collectionDate: LocalDate,
    val mainPhoto: String?,
    val collector: String,
    val individualsCount: Int,
    val determinationYear: Int,
    val determinador: String,
    val sex: String,
    val vegetationType: String,
    val collectionMethod: String,
    val notes: String?,
    val scientificName: String,
    val images: List<SpecimenImage>
)

fun Specimen.toResponse(): SpecimenResponse = SpecimenResponse(
    id = this.id,
    idCollection = this.idCollection,
    commonName = this.commonName,
    collectionDate = this.collectionDate,
    mainPhoto = this.mainPhoto,
    collector = this.collector,
    individualsCount = this.individualsCount,
    determinationYear = this.determinationYear,
    determinador = this.determinador,
    sex = this.sex,
    vegetationType = this.vegetationType,
    collectionMethod = this.collectionMethod,
    notes = this.notes,
    scientificName = this.taxonomy.species,
    images = this.images
)
fun Collection.toResponse(): CollectionResponse = CollectionResponse(
    id = this.id,
    idManager = this.idManager,
    name = this.name,
    description = this.description,
    category = this.category,
    createdAt = this.createdAt,
    imageUrl = this.imageUrl,
    specimens = emptyList()
)




