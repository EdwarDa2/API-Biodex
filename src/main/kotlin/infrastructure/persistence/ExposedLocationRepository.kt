package com.Biodex.infrastructure.persistence

import com.Biodex.domain.models.Location
import com.Biodex.domain.models.renewLocation
import com.Biodex.domain.repositorys.LocationRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ExposedLocationRepository: LocationRepository {

    override fun getLocation(id: Int): Location? {
        return transaction {
            try {
                val row = LocationTable
                    .select { LocationTable.id eq id }
                    .singleOrNull()

                if (row == null) {
                    println("❌ La consulta no devolvió nada. Retornando null.")
                    println("------------------------------------------")
                    return@transaction null
                }

                println("✅ Fila encontrada. Mapeando...")
                val taxonomy = row.toLocation()
                println("🎉 ¡Mapeo exitoso!")
                println("------------------------------------------")
                return@transaction taxonomy

            } catch (e: Exception) {
                println("🚨🚨🚨 ERROR DURANTE EL MAPEO 🚨🚨🚨")
                println("La excepción fue: ${e.message}")
                e.printStackTrace()
                println("------------------------------------------")
                return@transaction null
            }
        }
    }

    override fun saveLocation(location: renewLocation): Location? {
        val newId = transaction {
            LocationTable.insert {
                it[country] = location.country
                it[state] = location.state
                it[municipality] = location.municipality
                it[locality] = location.locality
                it[latitude_degrees] = location.latitude_degrees
                it[latitude_minutes] = location.latitude_minutes
                it[latitude_seconds] = location.latitude_seconds
                it[longitude_degrees] = location.longitude_degrees
                it[longitude_minutes] = location.longitude_minutes
                it[longitude_seconds] = location.longitude_seconds
                it[altitude] = location.altitude
            } get LocationTable.id
        }
        return getLocation(newId)
    }

    override fun updateLocation(id: Int, location: renewLocation): Location? {
        val updateRow = transaction {
            LocationTable.update({ LocationTable.id eq id }) {
                it[country] = location.country
                it[state] = location.state
                it[municipality] = location.municipality
                it[locality] = location.locality
                it[latitude_degrees] = location.latitude_degrees
                it[latitude_minutes] = location.latitude_minutes
                it[latitude_seconds] = location.latitude_seconds
                it[longitude_degrees] = location.longitude_degrees
                it[longitude_minutes] = location.longitude_minutes
                it[longitude_seconds] = location.longitude_seconds
                it[altitude] = location.altitude
            }
        }
        return if(updateRow > 0) {
            getLocation(id)
        } else{
            null
        }
    }
}

public fun ResultRow.toLocation(): Location? {
    if (this[LocationTable.id] == null) {
        return null
    }
    return Location(
        id = this[LocationTable.id],
        country = this[LocationTable.country],
        state = this[LocationTable.state],
        municipality = this[LocationTable.municipality],
        locality = this[LocationTable.locality],
        latitude_degrees = this[LocationTable.latitude_degrees],
        latitude_minutes = this[LocationTable.latitude_minutes],
        latitude_seconds = this[LocationTable.latitude_seconds],
        longitude_degrees = this[LocationTable.longitude_degrees],
        longitude_minutes = this[LocationTable.longitude_minutes],
        longitude_seconds = this[LocationTable.longitude_seconds],
        altitude = this[LocationTable.altitude]
    )
}