package com.Biodex.infrastructure.persistence

import com.Biodex.domain.models.SpecimenImage
import com.Biodex.domain.repositorys.SpecimenImagesRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedSpecimenImageRepository : SpecimenImagesRepository {


    override fun addImage(image: SpecimenImage): SpecimenImage? {
        return transaction {

            val newId = SpecimenImagesTable.insert {
                it[displayOrder] = image.displayOrder
                it[fileName] = image.fileName
                it[fileUrl] = image.fileUrl
                it[idSpecimen] = image.idSpecimen
            } get SpecimenImagesTable.id

            return@transaction image.copy(id = newId)
        }
    }
    override fun getImage(id: Int): SpecimenImage? {
        return transaction {
            try {
                val row = SpecimenImagesTable
                    .select { SpecimenImagesTable.id eq id }
                    .singleOrNull()

                if (row == null) {
                    println("❌ La consulta no devolvió nada. Retornando null.")
                    println("------------------------------------------")
                    return@transaction null
                }


                println("✅ Fila encontrada. Mapeando...")
                val image = row.toSpecimenImage()
                println("🎉 ¡Mapeo exitoso!")
                println("------------------------------------------")
                return@transaction image

            } catch (e: Exception) {
                println("🚨🚨🚨 ERROR DURANTE EL MAPEO 🚨🚨🚨")
                println("La excepción fue: ${e.message}")
                e.printStackTrace()
                println("------------------------------------------")
                return@transaction null
            }
        }
    }

    override fun deleteImage(id: Int): Boolean {
        return transaction {
            val rowsDeleted = SpecimenImagesTable.deleteWhere {
                SpecimenImagesTable.id eq id
            }
            return@transaction rowsDeleted > 0
        }
    }

    private fun ResultRow.toSpecimenImage(): SpecimenImage = SpecimenImage(
        id = this[SpecimenImagesTable.id],
        idSpecimen = this[SpecimenImagesTable.idSpecimen],
        fileName = this[SpecimenImagesTable.fileName],
        fileUrl = this[SpecimenImagesTable.fileUrl],
        displayOrder = this[SpecimenImagesTable.displayOrder]
    )
}