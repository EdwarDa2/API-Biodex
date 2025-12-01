package com.Biodex.domain.repositorys

import com.Biodex.domain.models.User

interface UserRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: Int): User?
    suspend fun create(user: User): User
    suspend fun update(user: User): User?
    suspend fun existsByEmail(email: String): Boolean
    suspend fun delete(id: Int): Boolean
}