package infrastructure.persistence

import domain.models.*
import domain.repositorys.NewSpecimenData
import domain.repositorys.SpecimenRepository
import domain.repositorys.UpdateSpecimenData
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedSpecimenRepository : SpecimenRepository {

    override fun findById(id: Int): Specimen? {
        println("------------- INICIO BÚSQUEDA -------------")
        println("🔎 Buscando espécimen con ID recibido: $id")

        return transaction {
            try { // <-- AÑADIR TRY
                val row = (SpecimensTable leftJoin TaxonomyTable)
                    .selectAll()
                    .where { SpecimensTable.id eq id }
                    .singleOrNull()

                if (row == null) {
                    println("❌ La consulta no devolvió nada. Retornando null.")
                    println("------------------------------------------")
                    return@transaction null
                }

                println("✅ Fila encontrada. Mapeando a objeto Specimen...")
                val images = SpecimenImagesTable
                    .select { SpecimenImagesTable.idSpecimen eq id }
                    .map { it.toSpecimenImage() }

                val specimen = row.toSpecimen(images) // Mapeo
                println("🎉 ¡Mapeo exitoso!")
                println("------------------------------------------")
                return@transaction specimen // Devolver el objeto creado

            } catch (e: Exception) { // <-- AÑADIR CATCH
                println("🚨🚨🚨 ERROR DURANTE EL MAPEO 🚨🚨🚨")
                println("La excepción fue: ${e.message}") // Imprime el mensaje de error
                e.printStackTrace() // Imprime el rastro completo del error
                println("------------------------------------------")
                return@transaction null // Devuelve null porque hubo un error
            }
        }
    }

    override fun findAll(): List<Specimen> {
        TODO("Not yet implemented")
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
                it[location] = specimenData.location
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
                it[location] = specimenData.location
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



    private fun ResultRow.toSpecimen(images: List<SpecimenImage>): Specimen = Specimen(
        id = this[SpecimensTable.id],
        idCollection = this[SpecimensTable.idCollection],
        commonName = this[SpecimensTable.commonName],
        collectionDate = this[SpecimensTable.collectionDate],
        mainPhoto = this[SpecimensTable.mainPhoto],
        collector = this[SpecimensTable.collector],
        location = this[SpecimensTable.location],
        individualsCount = this[SpecimensTable.individualsCount],
        determinationYear = this[SpecimensTable.determinationYear],
        determinador = this[SpecimensTable.determinador],
        sex = this[SpecimensTable.sex],
        vegetationType = this[SpecimensTable.vegetationType],
        collectionMethod = this[SpecimensTable.collectionMethod],
        notes = this[SpecimensTable.notes],
        taxonomy = this.toTaxonomy(),
        images = images
    )

    private fun ResultRow.toTaxonomy(): Taxonomy = Taxonomy(
        id = this[TaxonomyTable.id],
        family = this[TaxonomyTable.family],
        genus = this[TaxonomyTable.genus],
        species = this[TaxonomyTable.species],
        category = this[TaxonomyTable.category]
    )

    private fun ResultRow.toSpecimenImage(): SpecimenImage = SpecimenImage(
        id = this[SpecimenImagesTable.id],
        idSpecimen = this[SpecimenImagesTable.idSpecimen],
        fileName = this[SpecimenImagesTable.fileName],
        fileUrl = this[SpecimenImagesTable.fileUrl],
        displayOrder = this[SpecimenImagesTable.displayOrder]
    )
}