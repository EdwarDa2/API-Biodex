package domain.repositorys

import com.Biodex.domain.models.Collection
import domain.models.NewSpecimenData
import domain.models.Specimen
import domain.models.UpdateSpecimenData


interface SpecimenRepository {
    fun findById(id: Int): Specimen?
    fun findAllByCollectionId(id: Int): List<Specimen>?
    fun findAll(): List<Specimen>
    fun save(specimenData: NewSpecimenData): Specimen
    fun delete(id: Int): Boolean
    fun update(id: Int, specimenData: UpdateSpecimenData): Specimen?

}
