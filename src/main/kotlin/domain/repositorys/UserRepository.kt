package com.Biodex.domain.repositorys

import com.Biodex.domain.models.ChangePassword
import com.Biodex.domain.models.UpdateUser
import com.Biodex.domain.models.User
import com.Biodex.domain.models.UserLogin
import com.Biodex.domain.models.UserRegister
import com.Biodex.domain.models.UserResponse

interface UserRepository {
    fun findById(id: Int): UserResponse?
    fun allUsers(): List<UserResponse>
    fun findByEmail(email: String): User? // Devuelve User para validar password
    fun findByRole(role: String): List<UserResponse>
    fun createUser(user: UserRegister): UserResponse?
    fun updateUser(id: Int, user: UpdateUser): UserResponse?
    fun changePassword(id: Int, passwords: ChangePassword): Boolean
    fun deleteUser(id: Int): Boolean
    fun login(credentials: UserLogin): UserResponse? // Valida y devuelve usuario
}