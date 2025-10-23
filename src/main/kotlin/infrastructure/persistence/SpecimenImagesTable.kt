package com.Biodex.infrastructure.persistence

import infrastructure.persistence.SpecimensTable
import org.jetbrains.exposed.sql.Table

object SpecimenImagesTable : Table("specimen_images") {
    val id = integer("id").autoIncrement()
    val idSpecimen = integer("id_specimen").references(SpecimensTable.id)
    val fileName = varchar("file_name", 255)
    val fileUrl = varchar("file_url", 1024)
    val displayOrder = integer("display_order")
    override val primaryKey = PrimaryKey(id)
}