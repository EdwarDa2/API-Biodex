package com.Biodex.infrastructure.persistence

import com.Biodex.domain.models.Request
import com.Biodex.domain.models.RequestStatus
import com.Biodex.domain.models.RequestType
import com.Biodex.domain.repositorys.RequestRepository
import infrastructure.config.DatabaseFactory.dbQuery
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


object RequestsTable : Table("requests") {
    val id = integer("id").autoIncrement()
    val researcherId = integer("id_researcher").references(Users.id)
    val managerId = integer("id_manager").references(Users.id)
    val specimenId = integer("id_specimen")
    val requestDate = datetime("request_date")
    val description = varchar("description", 250)
    val requestType = enumerationByName("request_type", 20, RequestType::class)
    val status = enumerationByName("status", 20, RequestStatus::class)
    val managerComment = varchar("manager_comment", 250).nullable()

    override val primaryKey = PrimaryKey(id)
}

class ExposedRequestRepository : RequestRepository {

    override suspend fun create(request: Request): Request = dbQuery {
        val insertedId = RequestsTable.insert {
            it[researcherId] = request.researcherId
            it[managerId] = request.managerId
            it[specimenId] = request.specimenId
            it[requestDate] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            it[description] = request.description
            it[requestType] = request.requestType
            it[status] = RequestStatus.PENDING
            it[managerComment] = request.managerComment
        }[RequestsTable.id]

        request.copy(id = insertedId, status = RequestStatus.PENDING)
    }

    override suspend fun findById(id: Int): Request? = dbQuery {
        RequestsTable.select { RequestsTable.id eq id }
            .mapNotNull { rowToRequest(it) }
            .singleOrNull()
    }

    override suspend fun findByResearcher(researcherId: Int): List<Request> = dbQuery {
        RequestsTable.select { RequestsTable.researcherId eq researcherId }
            .map { rowToRequest(it) }
    }

    override suspend fun findByManager(managerId: Int): List<Request> = dbQuery {
        RequestsTable.select { RequestsTable.managerId eq managerId }
            .map { rowToRequest(it) }
    }

    override suspend fun findSentByUser(userId: Int): List<Request> = dbQuery {
        RequestsTable.select { RequestsTable.researcherId eq userId }
            .map { rowToRequest(it) }
    }

    override suspend fun findReceivedByManager(managerId: Int): List<Request> = dbQuery {
        RequestsTable.select { RequestsTable.managerId eq managerId }
            .map { rowToRequest(it) }
    }

    override suspend fun updateStatus(
        id: Int,
        status: RequestStatus,
        managerComment: String?
    ): Request? = dbQuery {
        val updated = RequestsTable.update({ RequestsTable.id eq id }) {
            it[RequestsTable.status] = status
            it[RequestsTable.managerComment] = managerComment
        }

        if (updated > 0) findById(id) else null
    }

    override suspend fun findByStatus(managerId: Int, status: RequestStatus): List<Request> = dbQuery {
        RequestsTable.select {
            (RequestsTable.managerId eq managerId) and (RequestsTable.status eq status)
        }.map { rowToRequest(it) }
    }

    private fun rowToRequest(row: ResultRow): Request {
        return Request(
            id = row[RequestsTable.id],
            researcherId = row[RequestsTable.researcherId],
            managerId = row[RequestsTable.managerId],
            specimenId = row[RequestsTable.specimenId],
            requestDate = row[RequestsTable.requestDate],
            description = row[RequestsTable.description],
            requestType = row[RequestsTable.requestType],
            status = row[RequestsTable.status],
            managerComment = row[RequestsTable.managerComment]
        )
    }
}