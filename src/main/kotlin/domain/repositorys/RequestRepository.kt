package com.Biodex.domain.repositorys

import com.Biodex.domain.models.Request
import com.Biodex.domain.models.RequestStatus

interface RequestRepository {
    suspend fun create(request: Request): Request
    suspend fun findById(id: Int): Request?
    suspend fun findByResearcher(researcherId: Int): List<Request>
    suspend fun findByManager(managerId: Int): List<Request>
    suspend fun findSentByUser(userId: Int): List<Request>
    suspend fun findReceivedByManager(managerId: Int): List<Request>
    suspend fun updateStatus(id: Int, status: RequestStatus, managerComment: String?): Request?
    suspend fun findByStatus(managerId: Int, status: RequestStatus): List<Request>
}