package domain.models

import com.Biodex.domain.models.Location
import com.Biodex.domain.models.SpecimenImage
import com.Biodex.domain.models.Taxonomy
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
     val location: Location?,
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
