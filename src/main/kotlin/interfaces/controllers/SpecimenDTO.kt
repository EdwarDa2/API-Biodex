package interfaces.controllers

import com.Biodex.domain.models.Collection
import com.Biodex.domain.models.SpecimenImage
import com.Biodex.interfaces.controllers.CollectionResponse
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
    val additionalPhoto1: String?,
    val additionalPhoto2: String?,
    val additionalPhoto3: String?,
    val additionalPhoto4: String?,
    val additionalPhoto5: String?,
    val additionalPhoto6: String?
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
        scientificName = "${this.taxonomy.genus} ${this.taxonomy.species}",
        additionalPhoto1 = this.additionalPhoto1,
        additionalPhoto2 = this.additionalPhoto2,
        additionalPhoto3 = this.additionalPhoto3,
        additionalPhoto4 = this.additionalPhoto4,
        additionalPhoto5 = this.additionalPhoto5,
        additionalPhoto6 = this.additionalPhoto6
    )
}