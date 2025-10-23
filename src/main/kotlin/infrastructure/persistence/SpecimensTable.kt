package infrastructure.persistence

import com.Biodex.infrastructure.persistence.SpecimensLocationTable
import com.Biodex.infrastructure.persistence.TaxonomyTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date


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
