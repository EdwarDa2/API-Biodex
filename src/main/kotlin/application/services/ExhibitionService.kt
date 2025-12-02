package com.Biodex.application.services

import com.Biodex.domain.models.Exhibition
import com.Biodex.domain.models.renewExhibition
import com.Biodex.domain.repositorys.ExhibitionContentRepository
import com.Biodex.domain.repositorys.ExhibitionRepository

class ExhibitionService (
    private val exhibitionRepository: ExhibitionRepository,
    private val contentRepository: ExhibitionContentRepository
){
    fun getExhibitionId(id: Int): Exhibition?{
        val exhibition = exhibitionRepository.getExhibitionId(id)
        return exhibition?.copy(content = contentRepository.getExhibitionContentByExhibitionId(id))
    }
    fun crateExhibition(exhibition: renewExhibition): Exhibition?{
        return exhibitionRepository.crateExhibition(exhibition)
    }
    fun updateExhibition(id: Int,exhibition: renewExhibition): Exhibition?{
        return exhibitionRepository.updateExhibition(id, exhibition)
    }
    fun deleteExhibition(id: Int): Boolean {

        contentRepository.deleteAllByExhibitionId(id)

        return exhibitionRepository.deleteExhibition(id)
    }
    fun getExhibitionsByManagerId(idManager: Int): List<Exhibition> {
        return exhibitionRepository.getExhibitionsByManagerId(idManager)
    }

    fun getAllExhibitions(): List<Exhibition> {
        return exhibitionRepository.getAllExhibitions()
    }
}