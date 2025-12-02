package infrastructure.persistence

import com.Biodex.domain.models.Location
import domain.models.*
import domain.repositorys.SpecimenRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import com.Biodex.domain.models.SpecimenImage
import com.Biodex.infrastructure.persistence.SpecimenImagesTable
import com.Biodex.infrastructure.persistence.LocationTable
import com.Biodex.infrastructure.persistence.toLocation
import com.Biodex.infrastructure.persistence.TaxonomyTable
import com.Biodex.infrastructure.persistence.CollectionTable
import com.Biodex.infrastructure.persistence.toTaxonomy
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ExposedSpecimenRepository : SpecimenRepository {

    override fun findById(id: Int): Specimen? {
        println("------------- INICIO BÚSQUEDA -------------")
        println("🔎 Buscando espécimen con ID recibido: $id")

        return transaction {
            try {
                // Hacemos el join con las tablas que necesita 'toSpecimen'
                val row = (SpecimensTable leftJoin TaxonomyTable leftJoin LocationTable)
                    .selectAll()
                    .where { SpecimensTable.id eq id }
                    .singleOrNull()

                if (row == null) {
                    println("❌ La consulta no devolvió nada. Retornando null.")
                    println("------------------------------------------")
                    return@transaction null
                }

                println("✅ Fila encontrada. Mapeando a objeto Specimen...")
                val specimen = row.toSpecimen() // Llama al mapper principal
                println("🎉 ¡Mapeo exitoso!")
                println("------------------------------------------")
                return@transaction specimen // Devuelve el objeto creado

            } catch (e: Exception) {
                println("🚨🚨🚨 ERROR DURANTE EL MAPEO 🚨🚨🚨")
                println("La excepción fue: ${e.message}")
                e.printStackTrace()
                println("------------------------------------------")
                return@transaction null
            }
        }
    }

    override fun findAllByCollectionId(collectionId: Int): List<Specimen> {
        return transaction {
            // 1. Trae todas las filas de especímenes de esa colección (con joins)
            val specimenRows = (SpecimensTable leftJoin TaxonomyTable leftJoin LocationTable)
                .select { SpecimensTable.idCollection eq collectionId } // <-- El filtro
                .toList()

            if (specimenRows.isEmpty()) {
                return@transaction emptyList()
            }

            // 3. Ensambla los especímenes con sus imágenes
            specimenRows.map { row ->
                row.toSpecimen() // Usa tu mapeador 'toSpecimen'
            }
        }
    }

    override fun findAll(): List<Specimen> {
        return transaction {

            val specimenRows = (SpecimensTable leftJoin TaxonomyTable leftJoin LocationTable)
                .selectAll()
                .toList()

            specimenRows.map { row ->
                row.toSpecimen()
            }
        }
    }

    override fun save(specimenData: NewSpecimenData): Specimen {
        val newId = transaction {
            SpecimensTable.insert {
                it[idCollection] = specimenData.idCollection
                it[commonName] = specimenData.commonName
                it[idTaxonomy] = specimenData.idTaxonomy
                it[collectionDate] = specimenData.collectionDate
                it[mainPhoto] = specimenData.mainPhoto
                it[collector] = specimenData.collector
                it[idLocation] = specimenData.idLocation
                it[individualsCount] = specimenData.individualsCount
                it[determinationYear] = specimenData.determinationYear
                it[determinador] = specimenData.determinador
                it[sex] = specimenData.sex
                it[vegetationType] = specimenData.vegetationType
                it[collectionMethod] = specimenData.collectionMethod
                it[notes] = specimenData.notes
                it[additionalPhoto1] = specimenData.additionalPhoto1
                it[additionalPhoto2] = specimenData.additionalPhoto2
                it[additionalPhoto3] = specimenData.additionalPhoto3
                it[additionalPhoto4] = specimenData.additionalPhoto4
                it[additionalPhoto5] = specimenData.additionalPhoto5
                it[additionalPhoto6] = specimenData.additionalPhoto6
            } get SpecimensTable.id
        }
        return findById(newId)!!
    }

    override fun update(id: Int, specimenData: UpdateSpecimenData): Specimen? {
        val updatedRows = transaction {
            SpecimensTable.update({ SpecimensTable.id eq id }) {
                specimenData.idCollection?.let { idCollection -> it[SpecimensTable.idCollection] = idCollection }
                specimenData.commonName?.let { commonName -> it[SpecimensTable.commonName] = commonName }
                specimenData.idTaxonomy?.let { idTaxonomy -> it[SpecimensTable.idTaxonomy] = idTaxonomy }
                specimenData.collectionDate?.let { collectionDate -> it[SpecimensTable.collectionDate] = collectionDate }
                specimenData.mainPhoto?.let { mainPhoto -> it[SpecimensTable.mainPhoto] = mainPhoto }
                specimenData.collector?.let { collector -> it[SpecimensTable.collector] = collector }
                specimenData.idLocation?.let { idLocation -> it[SpecimensTable.idLocation] = idLocation }
                specimenData.individualsCount?.let { individualsCount -> it[SpecimensTable.individualsCount] = individualsCount }
                specimenData.determinationYear?.let { determinationYear -> it[SpecimensTable.determinationYear] = determinationYear }
                specimenData.determinador?.let { determinador -> it[SpecimensTable.determinador] = determinador }
                specimenData.sex?.let { sex -> it[SpecimensTable.sex] = sex }
                specimenData.vegetationType?.let { vegetationType -> it[SpecimensTable.vegetationType] = vegetationType }
                specimenData.collectionMethod?.let { collectionMethod -> it[SpecimensTable.collectionMethod] = collectionMethod }
                specimenData.notes?.let { notes -> it[SpecimensTable.notes] = notes }
                specimenData.additionalPhoto1?.let { additionalPhoto1 -> it[SpecimensTable.additionalPhoto1] = additionalPhoto1 }
                specimenData.additionalPhoto2?.let { additionalPhoto2 -> it[SpecimensTable.additionalPhoto2] = additionalPhoto2 }
                specimenData.additionalPhoto3?.let { additionalPhoto3 -> it[SpecimensTable.additionalPhoto3] = additionalPhoto3 }
                specimenData.additionalPhoto4?.let { additionalPhoto4 -> it[SpecimensTable.additionalPhoto4] = additionalPhoto4 }
                specimenData.additionalPhoto5?.let { additionalPhoto5 -> it[SpecimensTable.additionalPhoto5] = additionalPhoto5 }
                specimenData.additionalPhoto6?.let { additionalPhoto6 -> it[SpecimensTable.additionalPhoto6] = additionalPhoto6 }
            }
        }
        return if (updatedRows > 0) {
            findById(id)
        } else {
            null
        }
    }

    override fun delete(id: Int): Boolean {
        return transaction {
            SpecimensTable.deleteWhere { SpecimensTable.id eq id } > 0
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
public fun ResultRow.toSpecimen(): Specimen = Specimen(
    id = this[SpecimensTable.id],
    idCollection = this[SpecimensTable.idCollection],
    commonName = this[SpecimensTable.commonName],
    collectionDate = this[SpecimensTable.collectionDate],
    mainPhoto = this[SpecimensTable.mainPhoto],
    collector = this[SpecimensTable.collector],
    individualsCount = this[SpecimensTable.individualsCount],
    determinationYear = this[SpecimensTable.determinationYear],
    determinador = this[SpecimensTable.determinador],
    sex = this[SpecimensTable.sex],
    vegetationType = this[SpecimensTable.vegetationType],
    collectionMethod = this[SpecimensTable.collectionMethod],
    notes = this[SpecimensTable.notes],
    taxonomy = this.toTaxonomy(),
    location = this.toLocation(),
    additionalPhoto1 = this[SpecimensTable.additionalPhoto1],
    additionalPhoto2 = this[SpecimensTable.additionalPhoto2],
    additionalPhoto3 = this[SpecimensTable.additionalPhoto3],
    additionalPhoto4 = this[SpecimensTable.additionalPhoto4],
    additionalPhoto5 = this[SpecimensTable.additionalPhoto5],
    additionalPhoto6 = this[SpecimensTable.additionalPhoto6]
)

