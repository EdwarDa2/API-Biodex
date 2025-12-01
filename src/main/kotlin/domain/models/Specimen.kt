package domain.models

import com.Biodex.domain.models.Location
import com.Biodex.domain.models.SpecimenImage
import com.Biodex.domain.models.Taxonomy
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Specimen(
     val id: Int,
     val idCollection: Int?,
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
     val additionalPhoto1: String?,
     val additionalPhoto2: String?,
     val additionalPhoto3: String?,
     val additionalPhoto4: String?,
     val additionalPhoto5: String?,
     val additionalPhoto6: String?
)
@Serializable
data class NewSpecimenData(
     val idCollection: Int,
     val commonName: String,
     val idTaxonomy: Int,
     val collectionDate: LocalDate,
     val mainPhoto: String?,
     val collector: String,
     val idLocation: Int,
     val individualsCount: Int,
     val determinationYear: Int,
     val determinador: String,
     val sex: String,
     val vegetationType: String,
     val collectionMethod: String,
     val notes: String?,
     val additionalPhoto1: String? = null,
     val additionalPhoto2: String? = null,
     val additionalPhoto3: String? = null,
     val additionalPhoto4: String? = null,
     val additionalPhoto5: String? = null,
     val additionalPhoto6: String? = null)
@Serializable
data class UpdateSpecimenData(
     val idCollection: Int?,
     val commonName: String?,
     val idTaxonomy: Int?,
     val collectionDate: LocalDate?,
     val mainPhoto: String?,
     val collector: String?,
     val idLocation: Int?,
     val individualsCount: Int?,
     val determinationYear: Int?,
     val determinador: String?,
     val sex: String?,
     val vegetationType: String?,
     val collectionMethod: String?,
     val notes: String?,
     val additionalPhoto1: String? = null,
     val additionalPhoto2: String? = null,
     val additionalPhoto3: String? = null,
     val additionalPhoto4: String? = null,
     val additionalPhoto5: String? = null,
     val additionalPhoto6: String? = null)
