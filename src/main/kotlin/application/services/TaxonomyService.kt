package com.Biodex.application.services

import com.Biodex.domain.models.NewTaxonomyData
import com.Biodex.domain.models.Taxonomy
import com.Biodex.domain.repositorys.TaxonomyRepository

class TaxonomyService (
    private val taxonomyRepository: TaxonomyRepository
) {
    fun getTaxonomy(id: Int): Taxonomy? {
        return taxonomyRepository.getTaxonomy(id)
    }
    fun createTaxonomy(taxonomy: NewTaxonomyData): Taxonomy? {
        return taxonomyRepository.createTaxonomy(taxonomy)
    }
    fun updateTaxonomy(id: Int, taxonomy: NewTaxonomyData): Taxonomy? {
        return taxonomyRepository.updateTaxonomy(id,taxonomy)
    }
}