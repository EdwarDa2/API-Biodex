package com.Biodex.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object ExhibitionContentTable :  Table("exhibition_content") {
    val id = integer("id").autoIncrement()
    val idExhibition = integer("id_exhibition").references(ExhibitionTable.id)
    val contentType = varchar("content_type", 50)
    val textContent = varchar("text_content", 255).nullable()
    val imageUrl = varchar("image_url", 255).nullable()
    val displayOrder = integer("display_order")
    override val primaryKey = PrimaryKey(id)
}