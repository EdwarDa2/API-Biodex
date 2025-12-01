package com.Biodex.domain.models

import kotlinx.datetime.LocalDateTime

enum class RequestStatus {
    PENDING, APPROVED, REJECTED
}

enum class RequestType {
    INFORMATION, SAMPLE
}

data class Request(
    val id: Int? = null,
    val researcherId: Int,
    val managerId: Int,
    val specimenId: Int,           
    val requestDate: LocalDateTime,
    val description: String,
    val requestType: RequestType,
    val status: RequestStatus,
    val managerComment: String? = null
)