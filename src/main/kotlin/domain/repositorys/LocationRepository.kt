package com.Biodex.domain.repositorys

import com.Biodex.domain.models.Location
import com.Biodex.domain.models.renewLocation

interface LocationRepository {
    fun getLocation(id:Int):  Location?
    fun saveLocation(location: renewLocation):  Location?
    fun updateLocation(id: Int,location: renewLocation):   Location?
    fun findByAttributes(
        country: String, state: String, municipality: String, locality: String,
        latitude_degrees: Int, latitude_minutes: Int, latitude_seconds: Double,
        longitude_degrees: Int, longitude_minutes: Int, longitude_seconds: Double,
        altitude: Double
    ): Location?
}