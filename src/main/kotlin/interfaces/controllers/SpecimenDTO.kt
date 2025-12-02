package interfaces.controllers

import com.Biodex.domain.models.Collection
import com.Biodex.domain.models.SpecimenImage
import com.Biodex.interfaces.controllers.CollectionResponse
import domain.models.Specimen
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
data class CreateSpecimenRequest(
    val idCollection: Int,
    val commonName: String,
    val idTaxonomy: Int,
    val collectionDate: String,
    val collector: String,
    val idLocation: Int,
    val individualsCount: Int,
    val determinationYear: Int,
    val determinador: String,
    val sex: String,
    val vegetationType: String,
    val collectionMethod: String,
    val notes: String?,
    // Fotos
    val mainPhoto: String?,
    val additionalPhoto1: String? = null,
    val additionalPhoto2: String? = null,
    val additionalPhoto3: String? = null,
    val additionalPhoto4: String? = null,
    val additionalPhoto5: String? = null,
    val additionalPhoto6: String? = null
)

@Serializable
data class UpdateSpecimenRequest(
    val idCollection: Int? = null,
    val idTaxonomy: Int? = null,
    val idLocation: Int? = null,
    val commonName: String? = null,
    val collectionDate: String? = null,
    val collector: String? = null,
    val individualsCount: Int? = null,
    val determinationYear: Int? = null,
    val determinador: String? = null,
    val sex: String? = null,
    val vegetationType: String? = null,
    val collectionMethod: String? = null,
    val notes: String? = null,
    val mainPhoto: String? = null,
    val additionalPhoto1: String? = null,
    val additionalPhoto2: String? = null,
    val additionalPhoto3: String? = null,
    val additionalPhoto4: String? = null,
    val additionalPhoto5: String? = null,
    val additionalPhoto6: String? = null
)

// --- SUB-DTOS (Para recuperar los datos anidados) ---
@Serializable
data class TaxonomyDto(
    val id: Int,
    val family: String,
    val genus: String,
    val species: String,
    val category: String
)

@Serializable
data class LocationDto(
    val id: Int,
    val country: String,
    val state: String,
    val municipality: String,
    val locality: String,
    val latitude_degrees: Int,
    val latitude_minutes: Int,
    val latitude_seconds: Double,
    val longitude_degrees: Int,
    val longitude_minutes: Int,
    val longitude_seconds: Double,
    val altitude: Double
)

// --- RESPONSE (Completa con todos los datos) ---
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

    // ✅ AQUÍ RECUPERAMOS LOS OBJETOS COMPLETOS
    val taxonomy: TaxonomyDto,
    val location: LocationDto?,

    // Fotos
    val additionalPhoto1: String?,
    val additionalPhoto2: String?,
    val additionalPhoto3: String?,
    val additionalPhoto4: String?,
    val additionalPhoto5: String?,
    val additionalPhoto6: String?
)

// --- MAPPER ---
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

        // Mapeamos Taxonomía
        taxonomy = TaxonomyDto(
            id = this.taxonomy.id,
            family = this.taxonomy.family, // Asegúrate que tu modelo Taxonomy tenga estos campos
            genus = this.taxonomy.genus,
            species = this.taxonomy.species,
            category = this.taxonomy.category
        ),

        // Mapeamos Ubicación
        location = this.location?.let {
            LocationDto(
                id = it.id,
                country = it.country,
                state = it.state,
                municipality = it.municipality,
                locality = it.locality,
                latitude_degrees = it.latitude_degrees,
                latitude_minutes = it.latitude_minutes,
                latitude_seconds = it.latitude_seconds,
                longitude_degrees = it.longitude_degrees,
                longitude_minutes = it.longitude_minutes,
                longitude_seconds = it.longitude_seconds,
                altitude = it.altitude
            )
        },

        additionalPhoto1 = this.additionalPhoto1,
        additionalPhoto2 = this.additionalPhoto2,
        additionalPhoto3 = this.additionalPhoto3,
        additionalPhoto4 = this.additionalPhoto4,
        additionalPhoto5 = this.additionalPhoto5,
        additionalPhoto6 = this.additionalPhoto6
    )
}