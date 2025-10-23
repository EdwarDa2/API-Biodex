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
}