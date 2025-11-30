package com.Biodex.application.services

import com.Biodex.domain.models.ExhibitionContent
import com.Biodex.domain.models.renewExhibitionContent
import com.Biodex.domain.repositorys.ExhibitionContentRepository

class ExhibitionContentService (
    private val exhibitionContentRepository: ExhibitionContentRepository
){
    fun getExhibitionContentId(id: Int): ExhibitionContent?{
        return exhibitionContentRepository.getExhibitionContentId(id)
    }
    fun createExhibitionContent(content: renewExhibitionContent): ExhibitionContent?{
        if (content.contentType == "TEXT" && content.textContent == null) {
            throw IllegalArgumentException("textContent no puede ser nulo cuando contentType es TEXT")
        }
        return exhibitionContentRepository.createExhibitionContent(content)
    }
    fun updateExhibitionContent(id: Int,content: renewExhibitionContent): ExhibitionContent? {
        return exhibitionContentRepository.updateExhibitionContent(id, content);
    }
    fun deleteContent(id: Int): Boolean { // Nombre más claro y devuelve Boolean
        // Llama a la función 'delete' del repo, no a 'deleteAllBy...'
        return exhibitionContentRepository.delete(id) // <-- Lógica Corregida
    }

}