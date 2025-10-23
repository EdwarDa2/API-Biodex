package domain.repositorys

import domain.models.Specimen
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

interface SpecimenRepository {
    fun findById(id: Int): Specimen?
    fun findAll(): List<Specimen>
    fun save(specimenData: NewSpecimenData): Specimen
    fun delete(id: Int): Boolean
    fun update(id: Int, specimenData: UpdateSpecimenData): Specimen?

}

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
    val notes: String?
)
@Serializable
data class UpdateSpecimenData(
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
    val notes: String?
)
