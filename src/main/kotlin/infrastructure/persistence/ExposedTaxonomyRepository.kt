package com.Biodex.infrastructure.persistence

import com.Biodex.domain.models.NewTaxonomyData
import com.Biodex.domain.models.Taxonomy
import com.Biodex.domain.repositorys.TaxonomyRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExposedTaxonomyRepository : TaxonomyRepository {
    override fun getTaxonomy(id: Int): Taxonomy? {
        return transaction {
            try {
                val row = TaxonomyTable
                    .select { TaxonomyTable.id eq id }
                    .singleOrNull()

                if (row == null) {
                    println("❌ La consulta no devolvió nada. Retornando null.")
                    println("------------------------------------------")
                    return@transaction null
                }

                println("✅ Fila encontrada. Mapeando...")
                val taxonomy = row.toTaxonomy()
                println("🎉 ¡Mapeo exitoso!")
                println("------------------------------------------")
                return@transaction taxonomy

            } catch (e: Exception) {
                println("🚨🚨🚨 ERROR DURANTE EL MAPEO 🚨🚨🚨")
                println("La excepción fue: ${e.message}")
                e.printStackTrace()
                println("------------------------------------------")
                return@transaction null
            }
        }
    }

    override fun createTaxonomy(taxonomy: NewTaxonomyData): Taxonomy? {
        val newId = transaction {
            TaxonomyTable.insert {
                it[family] = taxonomy.family
                it[genus] = taxonomy.genus
                it[species] = taxonomy.species
                it[category] = taxonomy.category
            } get TaxonomyTable.id
        }
        return getTaxonomy(newId)
    }

    override fun updateTaxonomy(id: Int, taxonomy: NewTaxonomyData): Taxonomy? {
        val updateRow = transaction {
            TaxonomyTable.update({ TaxonomyTable.id eq id }) {
                it[family] = taxonomy.family
                it[genus] = taxonomy.genus
                it[species] = taxonomy.species
                it[category] = taxonomy.category
            }
        }
        return if (updateRow > 0) {
            getTaxonomy(id)
        } else {
            null
        }
    }
}

public fun ResultRow.toTaxonomy(): Taxonomy = Taxonomy(
    id = this[TaxonomyTable.id],
    family = this[TaxonomyTable.family],
    genus = this[TaxonomyTable.genus],
    species = this[TaxonomyTable.species],
    category = this[TaxonomyTable.category]
)