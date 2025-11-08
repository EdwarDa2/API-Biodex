package com.Biodex.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

enum class RequestTypeEnum { INFORMATION, SAMPLE }

enum class RequestStatusEnum { PENDING, APPROVED, REJECTED }

@Serializable
data class Request(
    val id: Int,
    val idResearcher: Int,
    val idManager: Int,
    val idSpecimen: Int,
    val requestDate: LocalDate,
    val description: String,
    val requestType: String,
    val status: String,
    val managerComment: String?
)

@Serializable
data class NewRequest(
    val idResearcher: Int,
    val idManager: Int,
    val idSpecimen: Int,
    val description: String,
    val requestType: String,
    val status: String = "PENDING" // Valor por defecto
)

@Serializable
data class UpdateRequestStatus(
    val status: String,
    val managerComment: String?
)