package com.Biodex.application.services

import com.Biodex.domain.models.SpecimenImage
import com.Biodex.domain.repositorys.SpecimenImagesRepository

class SpecimenImageService(
    private val specimenImagesRepository: SpecimenImagesRepository
) {
    fun addImage(image: SpecimenImage): SpecimenImage? {
        return specimenImagesRepository.addImage(image)
    }

    fun getImage(id: Int): SpecimenImage? {
        return specimenImagesRepository.getImage(id)
    }

    fun deleteImage(id : Int): Boolean {
        return specimenImagesRepository.deleteImage(id)
    }

}