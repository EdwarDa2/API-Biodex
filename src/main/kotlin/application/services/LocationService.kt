package com.Biodex.application.services

import com.Biodex.domain.models.Location
import com.Biodex.domain.models.renewLocation
import com.Biodex.domain.repositorys.LocationRepository

class LocationService (
    private val locationRepository: LocationRepository
) {
    fun getLocation(id: Int): Location? {
        return locationRepository.getLocation(id)
    }
    fun saveLocation(location: renewLocation): Location? {
        return locationRepository.saveLocation(location)
    }
    fun updateLocation(id: Int,location: renewLocation): Location? {
        return locationRepository.updateLocation(id, location)
    }

    fun getLocationByAttributes(
        country: String, state: String, municipality: String, locality: String,
        latitude_degrees: Int, latitude_minutes: Int, latitude_seconds: Double,
        longitude_degrees: Int, longitude_minutes: Int, longitude_seconds: Double,
        altitude: Double
    ): Location? {
        return locationRepository.findByAttributes(
            country, state, municipality, locality,
            latitude_degrees, latitude_minutes, latitude_seconds,
            longitude_degrees, longitude_minutes, longitude_seconds,
            altitude
        )
    }
}