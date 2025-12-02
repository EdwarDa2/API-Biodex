package com.Biodex.domain.repositorys

import com.Biodex.domain.models.NewTaxonomyData
import com.Biodex.domain.models.Taxonomy

interface TaxonomyRepository{
    fun getTaxonomy(id: Int): Taxonomy?
    fun createTaxonomy(taxonomy: NewTaxonomyData):  Taxonomy?
    fun updateTaxonomy(id: Int,taxonomy: NewTaxonomyData): Taxonomy?
    fun findByAttributes(family: String, genus: String, species: String, category: String): Taxonomy?
}