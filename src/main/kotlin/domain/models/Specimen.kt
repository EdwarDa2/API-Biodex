package domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Specimen(
     val id: Int,
     val idCollection: Int,
     val commonName: String,
     val collectionDate: LocalDate,
     val mainPhoto: String?,
     val collector: String,
     val location: domain.models.Location?,
     val individualsCount: Int,
     val determinationYear: Int,
     val determinador: String,
     val sex: String,
     val vegetationType: String,
     val collectionMethod: String,
     val notes: String?,
     val taxonomy: Taxonomy,
     val images: List<SpecimenImage>
)

@Serializable
data class Taxonomy(
     val id: Int,
     val family: String,
     val genus: String,
     val species: String,
     val category: String
)

@Serializable
data class SpecimenImage(
     val id: Int,
     val idSpecimen: Int,
     val fileName: String,
     val fileUrl: String,
     val displayOrder: Int
)
@Serializable
data class Location(
     val id: Int,
     val country: String,
     val state : String,
     val municipality: String,
     val locality: String,
     val latitude_degrees:Int,
     val latitude_minutes:Int,
     val latitude_seconds: Double,
     val longitude_degrees:Int,
     val longitude_minutes:Int,
     val longitude_seconds: Double,
     val altitude: Double
)