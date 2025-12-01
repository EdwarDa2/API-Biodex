package com.Biodex.application.services

import com.Biodex.domain.models.Request
import com.Biodex.domain.models.RequestStatus
import com.Biodex.domain.models.RequestType
import com.Biodex.domain.models.UserRoleEnum
import com.Biodex.domain.repositorys.RequestRepository
import com.Biodex.domain.repositorys.UserRepository
import com.Biodex.interfaces.dto.CreateRequestDTO
import com.Biodex.interfaces.dto.RequestResponse
import com.Biodex.interfaces.dto.UpdateRequestStatusDTO
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class RequestService(
    private val requestRepository: RequestRepository,
    private val userRepository: UserRepository
) {

    suspend fun createRequest(userId: Int, dto: CreateRequestDTO): RequestResponse {
        // Validar que el usuario que solicita exista
        val requester = userRepository.findById(userId)
            ?: throw IllegalArgumentException("Usuario no encontrado")

        // Validar que el manager exista y sea realmente manager
        val manager = userRepository.findById(dto.managerId)
            ?: throw IllegalArgumentException("Manager no encontrado")

        if (manager.role != UserRoleEnum.MANAGER) {
            throw IllegalArgumentException("Solo se pueden enviar solicitudes a managers")
        }

        // Validar tipo de solicitud
        val requestType = try {
            RequestType.valueOf(dto.requestType.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Tipo de solicitud inválido. Debe ser 'information' o 'sample'")
        }

        val request = Request(
            researcherId = userId,
            managerId = dto.managerId,
            specimenId = dto.specimenId,
            requestDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            description = dto.description,
            requestType = requestType,
            status = RequestStatus.PENDING
        )

        val created = requestRepository.create(request)
        return RequestResponse.fromDomain(created)
    }

    suspend fun getMySentRequests(userId: Int): List<RequestResponse> {
        val requests = requestRepository.findSentByUser(userId)
        return requests.map { RequestResponse.fromDomain(it) }
    }

    suspend fun getMyReceivedRequests(userId: Int): List<RequestResponse> {
        // Validar que sea manager
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("Usuario no encontrado")

        if (user.role != UserRoleEnum.MANAGER) {
            throw IllegalArgumentException("Solo los managers pueden ver solicitudes recibidas")
        }

        val requests = requestRepository.findReceivedByManager(userId)
        return requests.map { RequestResponse.fromDomain(it) }
    }

    suspend fun getRequestsByStatus(userId: Int, status: String): List<RequestResponse> {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("Usuario no encontrado")

        if (user.role != UserRoleEnum.MANAGER) {
            throw IllegalArgumentException("Solo los managers pueden filtrar por estado")
        }

        val requestStatus = try {
            RequestStatus.valueOf(status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Estado inválido. Debe ser 'pending', 'approved' o 'rejected'")
        }

        val requests = requestRepository.findByStatus(userId, requestStatus)
        return requests.map { RequestResponse.fromDomain(it) }
    }

    suspend fun updateRequestStatus(
        requestId: Int,
        userId: Int,
        dto: UpdateRequestStatusDTO
    ): RequestResponse {
        // Validar que el request existe
        val request = requestRepository.findById(requestId)
            ?: throw IllegalArgumentException("Solicitud no encontrada")

        // Validar que el usuario es el manager de la solicitud
        if (request.managerId != userId) {
            throw IllegalArgumentException("No tienes permiso para actualizar esta solicitud")
        }

        // Validar que sea manager
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("Usuario no encontrado")

        if (user.role != UserRoleEnum.MANAGER) {
            throw IllegalArgumentException("Solo los managers pueden actualizar solicitudes")
        }

        // Validar estado
        val newStatus = try {
            RequestStatus.valueOf(dto.status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Estado inválido. Debe ser 'approved' o 'rejected'")
        }

        if (newStatus == RequestStatus.PENDING) {
            throw IllegalArgumentException("No se puede cambiar a estado 'pending'")
        }

        val updated = requestRepository.updateStatus(requestId, newStatus, dto.managerComment)
            ?: throw IllegalStateException("Error al actualizar la solicitud")

        return RequestResponse.fromDomain(updated)
    }

    suspend fun getRequestById(requestId: Int, userId: Int): RequestResponse {
        val request = requestRepository.findById(requestId)
            ?: throw IllegalArgumentException("Solicitud no encontrada")

        // Validar que el usuario es parte de la solicitud
        if (request.researcherId != userId && request.managerId != userId) {
            throw IllegalArgumentException("No tienes permiso para ver esta solicitud")
        }

        return RequestResponse.fromDomain(request)
    }
}