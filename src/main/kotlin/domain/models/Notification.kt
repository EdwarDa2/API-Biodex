package com.Biodex.domain.models

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

enum class NotificationTypeEnum { NEW_REQUEST, REQUEST_UPDATE, COMMENT }

@Serializable
data class Notification(
    val id: Int,
    val idUser: Int,
    val idRequest: Int,
    val notificationType: String,
    val message: String,
    val notificationDate: LocalDate,
    val isRead: Boolean
)

@Serializable
data class NewNotification(
    val idUser: Int,
    val idRequest: Int,
    val notificationType: String,
    val message: String
    // notificationDate e isRead se generan automáticamente
)

@Serializable
data class MarkNotificationAsRead(
    val isRead: Boolean = true
)

@Serializable
data class NotificationWithDetails(
    val id: Int,
    val idUser: Int,
    val idRequest: Int,
    val notificationType: String,
    val message: String,
    val notificationDate: String,
    val isRead: Boolean,
    // Datos adicionales del request relacionado
    val requestDescription: String?,
    val specimenName: String?,
    val researcherName: String?
)