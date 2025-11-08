package com.Biodex.domain.models

import kotlinx.serialization.Serializable

enum class UserRoleEnum { MANAGER, RESEARCHER }

@Serializable
data class User(
    val id: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val password: String, // Nunca devolver esto al frontend
    val role: String,
    val institution: String,
    val description: String,
    val photo: String
)

@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val role: String,
    val institution: String,
    val description: String,
    val photo: String
    //Sin password
)

@Serializable
data class UserRegister(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String,
    val institution: String,
    val description: String,
    val photo: String = "" // Opcional al registrarse
)

@Serializable
data class UserLogin(
    val email: String,
    val password: String
)

@Serializable
data class UpdateUser(
    val name: String?,
    val lastName: String?,
    val institution: String?,
    val description: String?,
    val photo: String?
)

@Serializable
data class ChangePassword(
    val currentPassword: String,
    val newPassword: String
)