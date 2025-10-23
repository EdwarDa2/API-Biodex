package com.Biodex.domain.repositorys

import com.Biodex.domain.models.SpecimenImage


interface SpecimenImagesRepository{
    fun addImage(image: SpecimenImage): SpecimenImage?
    fun getImage(id: Int): SpecimenImage?
    fun deleteImage(id: Int): Boolean

}

