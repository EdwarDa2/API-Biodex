package com.Biodex.infrastructure.persistence

import com.Biodex.domain.models.Exhibition
import com.Biodex.domain.models.ExhibitionContent
import com.Biodex.domain.models.renewExhibition
import com.Biodex.domain.models.renewExhibitionContent
import com.Biodex.domain.repositorys.ExhibitionContentRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExposedExhibitionContentRepository : ExhibitionContentRepository {
    override fun getExhibitionContentId(id: Int): ExhibitionContent? {
        return transaction {
            try {
                val row = ExhibitionContentTable
                    .select { ExhibitionContentTable.id eq id }
                    .singleOrNull()

                if (row == null) {
                    println("❌ La consulta no devolvió nada. Retornando null.")
                    println("------------------------------------------")
                    return@transaction null
                }

                println("✅ Fila encontrada. Mapeando...")
                val exhibitionContent = row.toExhibitionContent()
                println("🎉 ¡Mapeo exitoso!")
                println("------------------------------------------")
                return@transaction exhibitionContent

            } catch (e: Exception) {
                println("🚨🚨🚨 ERROR DURANTE EL MAPEO 🚨🚨🚨")
                println("La excepción fue: ${e.message}")
                e.printStackTrace()
                println("------------------------------------------")
                return@transaction null
            }
        }
    }

    override fun createExhibitionContent(content: renewExhibitionContent): ExhibitionContent? {
        val newId = transaction {
            ExhibitionContentTable.insert {
                it[idExhibition] = content.idExhibition
                it[contentType] = content.contentType
                it[textContent] = content.textContent
                it[imageUrl] = content.imageUrl
                it[displayOrder] = content.displayOrder
            } get ExhibitionContentTable.id
        }
        return getExhibitionContentId(newId)
    }

    override fun updateExhibitionContent(id: Int, content: renewExhibitionContent): ExhibitionContent? {
        val updateRow = transaction {
            ExhibitionContentTable.update({ ExhibitionContentTable.id eq id }) {
                it[idExhibition] = content.idExhibition
                it[contentType] = content.contentType
                it[textContent] = content.textContent
                it[imageUrl] = content.imageUrl
                it[displayOrder] = content.displayOrder
            }
        }
        return if (updateRow > 0) {
            getExhibitionContentId(id)
        } else {
            null
        }
    }

    override fun deleteAllByExhibitionId(id: Int) {
        return transaction {
            ExhibitionContentTable.deleteWhere { ExhibitionContentTable.id eq id } > 0
        }
    }

    override fun delete(id: Int): Boolean { // Implementa la función de la interfaz
        return transaction {
            // Borra por el ID del CONTENIDO y devuelve true si se borró algo
            ExhibitionContentTable.deleteWhere { ExhibitionContentTable.id eq id } > 0
        }
    }

}
public fun ResultRow.toExhibitionContent(): ExhibitionContent = ExhibitionContent(
    id = this[ExhibitionContentTable.id],
    idExhibition = this[ExhibitionContentTable.idExhibition],
    contentType = this[ExhibitionContentTable.contentType],
    textContent = this[ExhibitionContentTable.textContent],
    imageUrl = this[ExhibitionContentTable.imageUrl],
    displayOrder = this[ExhibitionContentTable.displayOrder],
)