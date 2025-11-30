package com.Biodex.infrastructure.persistence


import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.Table

object ExhibitionTable : Table("exhibitions"){
    val id = integer("id").autoIncrement()
    val idManager = integer("id_manager")
    val title = varchar("title", 255)
    val description = varchar("description", 255)
    val category = varchar("category", 255)
    val createdAt = date("created_at")
    val coverImageUrl = varchar("cover_image_url", 255).nullable()
    override val primaryKey = PrimaryKey(id)
}