package com.Biodex.infrastructure.security

import org.mindrot.jbcrypt.BCrypt

object PasswordEncoder {
    fun encode(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return try {
            BCrypt.checkpw(rawPassword, encodedPassword)
        } catch (e: Exception) {
            false
        }
    }
}