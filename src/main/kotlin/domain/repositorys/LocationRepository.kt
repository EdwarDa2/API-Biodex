package com.Biodex.domain.repositorys

import com.Biodex.domain.models.Location
import com.Biodex.domain.models.renewLocation

interface LocationRepository {
    fun getLocation(id:Int):  Location?
    fun saveLocation(location: renewLocation):  Location?
    fun updateLocation(id: Int,location: renewLocation):   Location?
}