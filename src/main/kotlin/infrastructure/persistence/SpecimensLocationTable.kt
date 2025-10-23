package com.Biodex.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

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