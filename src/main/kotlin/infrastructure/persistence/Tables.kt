package infrastructure.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object TaxonomyTable : Table("taxonomy") {
    val id = integer("id").autoIncrement()
    val family = varchar("family", 255)
    val genus = varchar("genus", 255)
    val species = varchar("species", 255)
    val category = varchar("category", 100)
    override val primaryKey = PrimaryKey(id)
}

object SpecimensTable : Table("specimens") {
    val id = integer("id").autoIncrement()
    val idCollection = integer("id_collection")
    val commonName = varchar("common_name", 255)
    val idTaxonomy = integer("id_taxonomy").references(TaxonomyTable.id)
    val collectionDate = date("collection_date")
    val mainPhoto = varchar("main_photo", 1024).nullable()
    val collector = varchar("collector", 255)
    val location = integer("location")
    val individualsCount = integer("individuals_count")
    val determinationYear = integer("determination_year")
    val determinador = varchar("determinador", 255)
    val sex = varchar("sex", 50)
    val vegetationType = varchar("vegetation_type", 255)
    val collectionMethod = varchar("collection_method", 255)
    val notes = text("notes").nullable()
    override val primaryKey = PrimaryKey(id)
}

object SpecimenImagesTable : Table("specimen_images") {
    val id = integer("id").autoIncrement()
    val idSpecimen = integer("id_specimen").references(SpecimensTable.id)
    val fileName = varchar("file_name", 255)
    val fileUrl = varchar("file_url", 1024)
    val displayOrder = integer("display_order")
    override val primaryKey = PrimaryKey(id)
}