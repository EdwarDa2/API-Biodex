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
import com.Biodex.infrastructure.persistence.TaxonomyTable
import com.Biodex.infrastructure.persistence.toLocation
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
                // Busca las imágenes por separado
                val images = SpecimenImagesTable
                    .select { SpecimenImagesTable.idSpecimen eq id }
                    .map { it.toSpecimenImage() } // Llama al mapper de imágenes

                val specimen = row.toSpecimen(images) // Llama al mapper principal
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

            // 2. Trae las imágenes SÓLO de esos especímenes
            val specimenIds = specimenRows.map { it[SpecimensTable.id] }
            val imagesBySpecimenId = SpecimenImagesTable
                .select { SpecimenImagesTable.idSpecimen inList specimenIds }
                .map { it.toSpecimenImage() }
                .groupBy { it.idSpecimen }

            // 3. Ensambla los especímenes con sus imágenes
            specimenRows.map { row ->
                val specimenId = row[SpecimensTable.id]
                val images = imagesBySpecimenId[specimenId] ?: emptyList()
                row.toSpecimen(images) // Usa tu mapeador 'toSpecimen'
            }
        }
    }

    override fun findAll(): List<Specimen> {
        return transaction {

            val allImages = SpecimenImagesTable
                .selectAll()
                .map { it.toSpecimenImage() }
                .groupBy { it.idSpecimen }


            val specimenRows = (SpecimensTable leftJoin TaxonomyTable leftJoin LocationTable)
                .selectAll()
                .toList()

            specimenRows.map { row ->
                val specimenId = row[SpecimensTable.id]
                val imagesForThisSpecimen = allImages[specimenId] ?: emptyList()

                row.toSpecimen(imagesForThisSpecimen)
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
            } get SpecimensTable.id
        }
        return findById(newId)!!
    }

    override fun update(id: Int, specimenData: UpdateSpecimenData): Specimen? {
        val updatedRows = transaction {
            SpecimensTable.update({ SpecimensTable.id eq id }) {
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
public fun ResultRow.toSpecimen(images: List<SpecimenImage>): Specimen = Specimen(
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
    images = images
)

