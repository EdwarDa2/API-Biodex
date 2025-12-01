package com.Biodex.infrastructure.persistence

import com.Biodex.domain.models.User
import com.Biodex.domain.models.UserRoleEnum
import com.Biodex.domain.repositorys.UserRepository
import infrastructure.config.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 50).uniqueIndex()
    val password = varchar("password", 255)
    val role = enumerationByName("role", 10, UserRoleEnum::class)
    val institution = varchar("institution", 50)
    val description = varchar("description", 250)
    val photo = varchar("photo", 250)

    override val primaryKey = PrimaryKey(id)
}

class ExposedUserRepository : UserRepository {

    override suspend fun findByEmail(email: String): User? = dbQuery {
        Users.select { Users.email eq email }
            .mapNotNull { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun findById(id: Int): User? = dbQuery {
        Users.select { Users.id eq id }
            .mapNotNull { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun create(user: User): User = dbQuery {
        val insertedId = Users.insert {
            it[name] = user.name
            it[lastName] = user.lastName
            it[email] = user.email
            it[password] = user.password
            it[role] = user.role
            it[institution] = user.institution
            it[description] = user.description
            it[photo] = user.photo
        }[Users.id]

        user.copy(id = insertedId)
    }

    override suspend fun update(user: User): User? = dbQuery {
        val updated = Users.update({ Users.id eq user.id!! }) {
            it[name] = user.name
            it[lastName] = user.lastName
            it[email] = user.email
            it[institution] = user.institution
            it[description] = user.description
            it[photo] = user.photo
        }

        if (updated > 0) user else null
    }

    override suspend fun existsByEmail(email: String): Boolean = dbQuery {
        Users.select { Users.email eq email }.count() > 0
    }

    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[Users.id],
            name = row[Users.name],
            lastName = row[Users.lastName],
            email = row[Users.email],
            password = row[Users.password],
            role = row[Users.role],
            institution = row[Users.institution],
            description = row[Users.description],
            photo = row[Users.photo]
        )
    }
}