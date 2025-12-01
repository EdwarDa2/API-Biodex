package com.Biodex.application.services

import com.Biodex.domain.models.User
import com.Biodex.domain.models.UserRoleEnum
import com.Biodex.domain.repositorys.UserRepository
import com.Biodex.infrastructure.config.JWTConfig
import com.Biodex.infrastructure.security.PasswordEncoder
import com.Biodex.interfaces.controllers.*

class UserService(private val userRepository: UserRepository) {

    suspend fun register(request: UserRegister): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("El email ya está registrado")
        }

        val role = try {
            UserRoleEnum.valueOf(request.role.uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Rol inválido. Debe ser 'manager' o 'researcher'")
        }

        val user = User(
            name = request.name,
            lastName = request.lastName,
            email = request.email,
            password = PasswordEncoder.encode(request.password),
            role = role,
            institution = request.institution,
            description = request.description,
            photo = request.photo.ifEmpty { "default.jpg" }
        )

        val createdUser = userRepository.create(user)
        return UserResponse.fromDomain(createdUser)
    }

    suspend fun login(request: UserLogin): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Credenciales inválidas")

        if (!PasswordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Credenciales inválidas")
        }

        val token = JWTConfig.generateToken(
            userId = user.id!!,
            email = user.email,
            role = user.role.name
        )

        return LoginResponse(
            token = token,
            user = UserResponse.fromDomain(user)
        )
    }

    suspend fun getProfile(userId: Int): UserResponse {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("Usuario no encontrado")

        return UserResponse.fromDomain(user)
    }

    suspend fun updateProfile(userId: Int, request: UpdateUser): UserResponse {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("Usuario no encontrado")

        if (request.email != null && request.email != user.email) {
            if (userRepository.existsByEmail(request.email)) {
                throw IllegalArgumentException("El email ya está en uso")
            }
        }

        val updatedUser = user.copy(
            name = request.name ?: user.name,
            lastName = request.lastName ?: user.lastName,
            email = request.email ?: user.email,
            institution = request.institution ?: user.institution,
            description = request.description ?: user.description,
            photo = request.photo ?: user.photo
        )

        val result = userRepository.update(updatedUser)
            ?: throw IllegalStateException("Error al actualizar el perfil")

        return UserResponse.fromDomain(result)
    }

    suspend fun deleteAccount(userId: Int): Boolean {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("Usuario no encontrado")

        return userRepository.delete(userId)
    }
}