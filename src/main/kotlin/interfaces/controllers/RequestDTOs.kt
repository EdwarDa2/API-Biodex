package com.Biodex.interfaces.dto

import com.Biodex.domain.models.Request
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestDTO(
    val managerId: Int,
    val specimenId: Int,
    val description: String,
    val requestType: String
)

@Serializable
data class UpdateRequestStatusDTO(
    val status: String,
    val managerComment: String? = null
)

@Serializable
data class RequestResponse(
    val id: Int,
    val researcherId: Int,
    val researcherName: String? = null,
    val managerId: Int,
    val managerName: String? = null,
    val specimenId: Int,
    val specimenName: String? = null,
    val requestDate: String,
    val description: String,
    val requestType: String,
    val status: String,
    val managerComment: String? = null
) {
    companion object {
        fun fromDomain(request: Request): RequestResponse {
            return RequestResponse(
                id = request.id!!,
                researcherId = request.researcherId,
                managerId = request.managerId,
                specimenId = request.specimenId,
                requestDate = request.requestDate.toString(),
                description = request.description,
                requestType = request.requestType.name.lowercase(),
                status = request.status.name.lowercase(),
                managerComment = request.managerComment
            )
        }
    }
}