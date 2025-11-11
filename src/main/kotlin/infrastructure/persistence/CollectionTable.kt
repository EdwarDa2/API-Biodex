package com.Biodex.infrastructure.persistence


import infrastructure.persistence.SpecimensTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object CollectionTable: Table("collections") {
    val id = integer("id").autoIncrement()
    val idManager = integer("id_manager")
    val name = varchar("name", 255)
    val description = varchar("description", 255)
    val category = varchar("category", 255)
    val createdAt = date("created_at")
    val imageUrl = varchar("image_url", 255).nullable()
    override val primaryKey = PrimaryKey(SpecimensTable.id)
}