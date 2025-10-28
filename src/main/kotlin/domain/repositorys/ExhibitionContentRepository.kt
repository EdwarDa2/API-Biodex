package com.Biodex.domain.repositorys


import com.Biodex.domain.models.ExhibitionContent
import com.Biodex.domain.models.renewExhibitionContent

interface ExhibitionContentRepository {
    fun getExhibitionContentId(id: Int): ExhibitionContent?
    fun createExhibitionContent(content: renewExhibitionContent): ExhibitionContent?
    fun updateExhibitionContent(id: Int, content: renewExhibitionContent): ExhibitionContent?
    fun deleteAllByExhibitionId(id: Int)
    fun delete(id: Int): Boolean

}