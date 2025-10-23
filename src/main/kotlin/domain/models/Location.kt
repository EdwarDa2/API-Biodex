package com.Biodex.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Location(
    val id: Int,
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
