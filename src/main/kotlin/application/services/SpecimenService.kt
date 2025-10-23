package application.services

import domain.models.Specimen
import domain.repositorys.NewSpecimenData
import domain.repositorys.SpecimenRepository
import domain.repositorys.UpdateSpecimenData

class SpecimenService(
    private val SpecimenRepository: SpecimenRepository
) {
    fun findSpecimenById(id: Int): Specimen? {
        return SpecimenRepository.findById(id)
    }

    fun createNewSpecimen(data: NewSpecimenData): Specimen {
        return SpecimenRepository.save(data)
    }

    fun updateSpecimen(id: Int, data: UpdateSpecimenData): Specimen? {
        return SpecimenRepository.update(id, data)
    }

    fun deleteSpecimen(id: Int): Boolean {
        return SpecimenRepository.delete(id)
    }
}

