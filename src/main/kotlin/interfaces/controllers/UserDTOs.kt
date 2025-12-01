package com.Biodex.interfaces.controllers

import com.Biodex.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserRegister(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String,
    val institution: String,
    val description: String,
    val photo: String = "default.jpg"
)

@Serializable
data class UserLogin(
    val email: String,
    val password: String
)

@Serializable
data class UpdateUser(
    val name: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val institution: String? = null,
    val description: String? = null,
    val photo: String? = null
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
) {
    companion object {
        fun fromDomain(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                name = user.name,
                lastName = user.lastName,
                email = user.email,
                role = user.role.name.lowercase(),
                institution = user.institution,
                description = user.description,
                photo = user.photo
            )
        }
    }
}

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserResponse
)