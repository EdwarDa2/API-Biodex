package com.Biodex.infrastructure.persistence

import com.Biodex.domain.models.renewCollection
import com.Biodex.domain.repositorys.CollectionRepository
import com.Biodex.domain.models.Collection
import infrastructure.persistence.SpecimensTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import kotlin.Int

class ExposedCollectionRespository : CollectionRepository {

    override fun findById(id: Int): Collection? {
        println("------------- INICIO BÚSQUEDA -------------")
        println("🔎 Buscando espécimen con ID recibido: $id")

        return transaction {
            CollectionTable
                .select { CollectionTable.id eq id }
                .singleOrNull()
                ?.toCollection()
        }
    }
    override fun allCollections(): List<Collection> {
        return transaction {
            CollectionTable.selectAll().map { it.toCollection() }
        }
    }
    override fun findByManagerId(idManager: Int): List<Collection> {
        return transaction {
            CollectionTable.select { CollectionTable.idManager eq idManager }
                .map { it.toCollection() }
        }
    }

    override fun createCollection(collection: renewCollection): Collection? {
        val newId = transaction {
            CollectionTable.insert {
                it[idManager] = collection.idManager
                it[name] = collection.name
                it[description] = collection.description
                it[category] = collection.category
                it[createdAt] = collection.createdAt
                it[imageUrl] = collection.imageUrl
            } get CollectionTable.id
        }
        return findById(newId)!!
    }

    override fun updateCollection(id: Int, collection: renewCollection): Collection? {
        val updateRow = transaction {
            CollectionTable.update({ CollectionTable.id eq id }) {
                it[idManager] = collection.idManager
                it[name] = collection.name
                it[description] = collection.description
                it[category] = collection.category
                it[createdAt] = collection.createdAt
                it[imageUrl] = collection.imageUrl
            }
        }
        return if (updateRow > 0) {
            findById(id)
        } else {
            null
        }
    }

    override fun deleteCollection(id: Int): Boolean {
        return transaction {
            CollectionTable.deleteWhere { CollectionTable.id eq id } > 0
        }
    }
    private fun ResultRow.toCollection(): Collection {
        return Collection(
            id = this[CollectionTable.id],
            idManager = this[CollectionTable.idManager],
            name = this[CollectionTable.name],
            description = this[CollectionTable.description],
            category = this[CollectionTable.category],
            createdAt = this[CollectionTable.createdAt],
            imageUrl = this[CollectionTable.imageUrl]
            )
        }
}
