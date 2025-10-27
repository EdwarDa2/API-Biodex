package com.Biodex.application.services

import com.Biodex.domain.models.Collection
import com.Biodex.domain.models.renewCollection
import com.Biodex.domain.repositorys.CollectionRepository
import com.Biodex.interfaces.controllers.CollectionResponse
import com.Biodex.interfaces.controllers.toResponse
import domain.repositorys.SpecimenRepository
import kotlin.collections.map

class CollectionService(
    private val collectionRepository: CollectionRepository,
    private val specimenRepository: SpecimenRepository
) {

        fun getCollectionByIdWithSpecimens(id: Int): CollectionResponse? {
            val collection = collectionRepository.findById(id) ?: return null

            val specimens = specimenRepository.findAllByCollectionId(id)

            // Ahora 'specimens' SÍ es una List<Specimen> y el .map funcionará
            return CollectionResponse(
                id = collection.id,
                idManager = collection.idManager,
                name = collection.name,
                description = collection.description,
                category = collection.category,
                createdAt = collection.createdAt,
                specimens = specimens?.map { it.toResponse() }!!
            )
        }

    fun getAllCollectionsWithSpecimens(): List<CollectionResponse> {

        val allCollections = collectionRepository.allCollections()
        val allSpecimens = specimenRepository.findAll()
        val specimensByCollectionId = allSpecimens.groupBy { it.idCollection }

        return allCollections.map { collection ->

            val specimensForThisCollection = specimensByCollectionId[collection.id] ?: emptyList()

            CollectionResponse(
                id = collection.id,
                name = collection.name,
                description = collection.description,
                idManager = collection.idManager,
                category = collection.category,
                createdAt = collection.createdAt,
                specimens = specimensForThisCollection.map { it.toResponse() }
            )
        }
    }

    fun createCollection(collection: renewCollection): Collection? {
        return collectionRepository.createCollection(collection)
    }

    fun updateCollection(id: Int, collection: renewCollection): Collection? {
        return collectionRepository.updateCollection(id, collection)
    }

    fun deleteCollection(id: Int): Boolean {
        return collectionRepository.deleteCollection(id)
    }
}