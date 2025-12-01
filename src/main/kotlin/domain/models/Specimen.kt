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

     // Fotos principales y adicionales como URLs directas
     val mainPhoto: String?,

     val collector: String,
     val location: Location?, // Asumo que tienes la clase Location en este paquete
     val individualsCount: Int,
     val determinationYear: Int,
     val determinador: String,
     val sex: String,
     val vegetationType: String,
     val collectionMethod: String,
     val notes: String?,
     val taxonomy: Taxonomy, // Asumo que tienes la clase Taxonomy en este paquete

     // Las 6 fotos adicionales
     val additionalPhoto1: String?,
     val additionalPhoto2: String?,
     val additionalPhoto3: String?,
     val additionalPhoto4: String?,
     val additionalPhoto5: String?,
     val additionalPhoto6: String?
)

// --- MODELO DE CREACIÓN (Lo que entra a la BD) ---
@Serializable
data class NewSpecimenData(
     val idCollection: Int,
     val commonName: String,
     val idTaxonomy: Int,
     val collectionDate: LocalDate,

     // Recibimos la URL de Cloudinary aquí
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

     // Recibimos las URLs de Cloudinary aquí
     val additionalPhoto1: String? = null,
     val additionalPhoto2: String? = null,
     val additionalPhoto3: String? = null,
     val additionalPhoto4: String? = null,
     val additionalPhoto5: String? = null,
     val additionalPhoto6: String? = null
)

// --- MODELO DE ACTUALIZACIÓN (Lo que edita la BD) ---
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
     val additionalPhoto6: String? = null
)