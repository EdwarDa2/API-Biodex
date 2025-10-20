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
    val idLocation = integer("location").references(SpecimensLocationTable.id)
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
object  SpecimensLocationTable : Table("locations") {
    val id = integer("id").autoIncrement()
    val country = varchar("country", 255)
    val state  = varchar("state", 255)
    val municipality =  varchar("municipality", 255)
    val locality =  varchar("locality", 255)
    val latitude_degrees = integer("latitude_degrees")
    val latitude_minutes =integer("latitude_minutes")
    val latitude_seconds = double("latitude_seconds")
    val longitude_degrees =  integer("longitude_degrees")
    val longitude_minutes =integer("longitude_minutes")
    val longitude_seconds = double("longitude_seconds")
    val altitude = double("altitude")
    override val primaryKey = PrimaryKey(id)
}