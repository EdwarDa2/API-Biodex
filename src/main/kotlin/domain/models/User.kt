package com.Biodex.domain.models

enum class UserRoleEnum {
    MANAGER,
    RESEARCHER
}

data class User(
    val id: Int? = null,
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: UserRoleEnum,
    val institution: String,
    val description: String,
    val photo: String
)