package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.Location
import kotlinx.serialization.Serializable

@Serializable
data  class LocationResponse(
    val country: String,
    val state : String,
    val municipality: String,
    val locality: String,
    val latitude_degrees:Int,
    val latitude_minutes:Int,
    val latitude_seconds: Double,
    val longitude_degrees:Int,
    val longitude_minutes:Int,
    val longitude_seconds: Double,
    val altitude: Double
)
fun Location.toResponse(): LocationResponse {
    return LocationResponse(
        country =  country,
        state =  state,
        municipality =  municipality,
        locality =   locality,
        latitude_degrees = latitude_degrees,
        latitude_minutes =  latitude_minutes,
        latitude_seconds = latitude_seconds,
        longitude_degrees = longitude_degrees,
        longitude_minutes =  longitude_minutes,
        longitude_seconds = longitude_seconds,
        altitude = altitude
    )
}