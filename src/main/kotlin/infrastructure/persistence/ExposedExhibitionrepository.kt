package com.Biodex.infrastructure.persistence

import com.Biodex.domain.models.Exhibition
import com.Biodex.domain.models.renewExhibition
import com.Biodex.domain.repositorys.ExhibitionRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExposedExhibitionrepository: ExhibitionRepository {
    override fun getExhibitionId(id: Int): Exhibition? {
        println("🔎 [Repo Exhibiciones] Buscando exhibición con ID: $id")
        return transaction {
            ExhibitionTable
                .select { ExhibitionTable.id eq id }
                .singleOrNull()
                ?.toExhibition()
        }
    }

    override fun crateExhibition(exhibition: renewExhibition): Exhibition? {
        val newId = transaction {
            ExhibitionTable.insert {
                it[idManager] =  exhibition.idManager
                it[title] =  exhibition.title
                it[ExhibitionTable.description] = exhibition.description
                it[category] =  exhibition.category
                it[createdAt] = exhibition.createdAt
                it[coverImageUrl] = exhibition.coverImageUrl
            } get ExhibitionTable.id
        }
        return getExhibitionId(newId)
    }

    override fun updateExhibition(id: Int, exhibition: renewExhibition): Exhibition? {
        val updateRow = transaction {
            ExhibitionTable.update({ ExhibitionTable.id eq id }) {
                it[idManager] =  exhibition.idManager
                it[title] =  exhibition.title
                it[ExhibitionTable.description] = exhibition.description
                it[category] =  exhibition.category
                it[createdAt] = exhibition.createdAt
                it[coverImageUrl] = exhibition.coverImageUrl
            }
        }
        return if (updateRow > 0) {
            getExhibitionId(id)
        } else {
            null
        }
    }

    override fun deleteExhibition(id: Int): Boolean {
        return transaction {
            ExhibitionTable.deleteWhere { ExhibitionTable.id eq id } > 0
        }
    }

    override fun getExhibitionsByManagerId(idManager: Int): List<Exhibition> {
        return transaction {
            ExhibitionTable
                .select { ExhibitionTable.idManager eq idManager }
                .map { it.toExhibition() }
        }
    }

    private  fun ResultRow.toExhibition(): Exhibition = Exhibition(
        id = this[ExhibitionTable.id],
        idManager = this[ExhibitionTable.idManager],
        title = this[ExhibitionTable.title],
        description = this[ExhibitionTable.description],
        category = this[ExhibitionTable.category],
        createdAt = this[ExhibitionTable.createdAt],
        coverImageUrl = this[ExhibitionTable.coverImageUrl],
        content = emptyList()
    )
}