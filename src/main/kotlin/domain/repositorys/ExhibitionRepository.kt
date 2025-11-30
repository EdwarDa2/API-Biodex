package com.Biodex.domain.repositorys

import com.Biodex.domain.models.Exhibition
import com.Biodex.domain.models.renewExhibition

interface ExhibitionRepository {
    fun getExhibitionId(id: Int): Exhibition?
    fun crateExhibition(exhibition: renewExhibition): Exhibition?
    fun updateExhibition(id: Int,exhibition: renewExhibition): Exhibition?
    fun deleteExhibition(id: Int):Boolean
    fun getExhibitionsByManagerId(idManager: Int): List<Exhibition>
}