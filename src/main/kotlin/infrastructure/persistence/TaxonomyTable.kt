package com.Biodex.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object TaxonomyTable : Table("taxonomy") {
    val id = integer("id").autoIncrement()
    val family = varchar("family", 255)
    val genus = varchar("genus", 255)
    val species = varchar("species", 255)
    val category = varchar("category", 100)
    override val primaryKey = PrimaryKey(id)
}