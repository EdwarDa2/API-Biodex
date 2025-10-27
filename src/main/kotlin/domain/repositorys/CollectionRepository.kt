package com.Biodex.domain.repositorys

import com.Biodex.domain.models.Collection
import com.Biodex.domain.models.renewCollection

interface CollectionRepository {
    fun findById(id: Int): Collection?
    fun allCollections(): List<Collection>
    fun createCollection(collection: renewCollection): Collection?
    fun updateCollection(id:Int,collection: renewCollection): Collection?
    fun deleteCollection(id: Int): Boolean
}