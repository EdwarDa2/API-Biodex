package interfaces.controllers

import domain.models.Specimen
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
data class SpecimenResponse(
    val id: Int,
    val commonName: String,
    val location: Int,
    val collector: String,
    val scientificName: String
)

fun Specimen.toResponse(): SpecimenResponse {
    return SpecimenResponse(
        id = this.id,
        commonName = this.commonName,
        location = this.location,
        collector = this.collector,
        scientificName = "${this.taxonomy.genus} ${this.taxonomy.species}"
    )
}