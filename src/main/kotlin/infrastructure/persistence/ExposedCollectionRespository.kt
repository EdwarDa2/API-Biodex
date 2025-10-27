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
            try {
                val row = (CollectionTable leftJoin SpecimensTable)
                    .selectAll()
                    .where { CollectionTable.id eq id }
                    .singleOrNull()

                if (row == null) {
                    println("❌ La consulta no devolvió nada. Retornando null.")
                    println("------------------------------------------")
                    return@transaction null
                }

                println("✅ Fila encontrada. Mapeando a objeto Specimen...")
                 CollectionTable
                    .select { CollectionTable.id eq id }
                    .map { it.toCollection() }

                val collection = row.toCollection()
                println("🎉 ¡Mapeo exitoso!")
                println("------------------------------------------")
                return@transaction collection

            } catch (e: Exception) { // <-- AÑADIR CATCH
                println("🚨🚨🚨 ERROR DURANTE EL MAPEO 🚨🚨🚨")
                println("La excepción fue: ${e.message}") // Imprime el mensaje de error
                e.printStackTrace() // Imprime el rastro completo del error
                println("------------------------------------------")
                return@transaction null // Devuelve null porque hubo un error
            }

        }
    }
    override fun allCollections(): List<Collection> {
        return transaction {
            CollectionTable.selectAll().map { it.toCollection() }
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
            createdAt = this[CollectionTable.createdAt]
        )
    }
}
