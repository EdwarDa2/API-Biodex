package com.Biodex.domain.repositorys

import com.Biodex.domain.models.NewRequest
import com.Biodex.domain.models.Request
import com.Biodex.domain.models.UpdateRequestStatus

interface RequestRepository {
    fun findById(id: Int): Request?
    fun allRequests(): List<Request>
    fun findByResearcher(idResearcher: Int): List<Request>
    fun findByManager(idManager: Int): List<Request>
    fun findBySpecimen(idSpecimen: Int): List<Request>
    fun findByStatus(status: String): List<Request>
    fun createRequest(request: NewRequest): Request?
    fun updateRequestStatus(id: Int, update: UpdateRequestStatus): Request?
    fun deleteRequest(id: Int): Boolean
}