package interfaces.controllers

import domain.models.Specimen
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
data class SpecimenResponse(
    val id: Int,
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
)

fun Specimen.toResponse(): SpecimenResponse {
    return SpecimenResponse(
        id = this.id,
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
        scientificName = "${this.taxonomy.genus} ${this.taxonomy.species}"
    )
}